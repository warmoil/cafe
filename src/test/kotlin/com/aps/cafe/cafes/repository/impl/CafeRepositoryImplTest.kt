package com.aps.cafe.cafes.repository.impl

import com.aps.cafe.cafes.model.CafeModel
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.ZonedDateTime

@SpringBootTest
class CafeRepositoryImplTest(
    @Autowired val repository: CafeRepositoryImpl,
) {
    private val now = ZonedDateTime.now()
    private val existsCafe = CafeModel(
        name = "test",
        description = "test cafe model",
        ownerId = "joyWorld@wrold.com",
        ownerNickname = "joyNickname",
        createdAt = now,
        updatedAt = now,
    )

    @BeforeEach
    fun setUp() {
        repository.create(existsCafe)
    }

    @AfterEach
    fun tearDown() {
        val findCafe = repository.getByCafeName(existsCafe.name) ?: return
        repository.delete(findCafe.id)
    }

    @Test
    @DisplayName("존재여부 잘체크하는지 확인")
    fun existsByCafeName() {
        val notExists = repository.existsByCafeName("joyWorld")
        assertFalse(notExists)

        val exists = repository.existsByCafeName(existsCafe.name)
        assertTrue(exists)
    }

    @Test
    @DisplayName("카페이름으로 잘 찾는지 확인")
    fun getByCafeName() {
        val notExists = repository.getByCafeName("없는이름")
        assertNull(notExists)

        val existsExpectModel = repository.getByCafeName(existsCafe.name)
        assertNotNull(existsExpectModel)
        assertEquals(existsCafe.name, existsExpectModel?.name)
        assertEquals(existsCafe.ownerId, existsExpectModel?.ownerId)
        assertEquals(existsCafe.ownerNickname, existsExpectModel?.ownerNickname)
    }

    @Test
    @DisplayName("아이디로 잘 가져오는지 확인")
    fun getByCafeId() {
        val findByName = repository.getByCafeName(existsCafe.name)
        assertNotNull(findByName)

        val findById = repository.getByCafeId(findByName?.id ?: 0)
        assertNotNull(findById)
        assertEquals(findByName, findById)
    }

    @Test
    @DisplayName("생성 확인")
    fun create() {
        // 있는데 또 생성하려할때 에러
        assertThrows<DataIntegrityViolationException> {
            repository.create(existsCafe)
        }
        val findCafe = repository.getByCafeName(existsCafe.name)
        repository.delete(findCafe?.id ?: 0)

        // 없다면 생성
        val createdCafe = repository.create(existsCafe)
        assertNotNull(createdCafe)
    }

    @Test
    @DisplayName("업데이트 확인")
    fun update() {
        val beforeCafe =
            repository.getByCafeName(existsCafe.name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val updatedCafe = repository.update(
            beforeCafe.copy(
                name = beforeCafe.name + "추가"
            )
        )
        assertNotEquals(beforeCafe, updatedCafe)
    }

    @Test
    @DisplayName("삭제 확인")
    fun delete() {
        val cafeList = repository.getAll()
        val cnt = cafeList.count()
        assertFalse(cnt == 0)

        // 삭제후 갯수가 줄었는지 확인
        repository.delete(cafeList.first().id)

        val afterRemoveList = repository.getAll()
        assertEquals(cnt - 1, afterRemoveList.count())
    }

    @Test
    @DisplayName("전체 가져오기 ")
    fun getAll() {
        val beforeList = repository.getAll()
        (1..10).forEach {
            repository.create(
                existsCafe.copy(
                    name = existsCafe.name + it.toString()
                )
            )
        }
        val afterAddList = repository.getAll()
        assertNotEquals(beforeList, afterAddList.count())
        assertEquals(beforeList.count() + 10, afterAddList.count())
    }
}