package com.aps.cafe.cafes.service.impl

import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.repository.CafeRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockKExtension::class)
class CafeServiceImplTest {
    @MockK
    private lateinit var cafeRepository: CafeRepository

    @InjectMockKs
    private lateinit var service: CafeServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private val existsCafe = CafeModel(
        id = 1,
        name = "existsName",
        description = "exists cafe!",
        ownerId = "ex",
        ownerNickname = "exNick",
    )

    private val notExistsCafe = CafeModel(
        name = "notExistsName",
        description = "notExists cafe!",
        ownerId = "notExistsOwnerId",
        ownerNickname = "notExistsOwnerNick",
    )


    @Test
    @DisplayName("존재하는 이름일경우 true 를 반환")
    fun ifExists() {
        // given
        every { cafeRepository.existsByCafeName(existsCafe.name) } returns (true)

        // when
        val isExists = service.isExistsByName(existsCafe.name)

        //then
        assertTrue(isExists)
    }

    @Test
    @DisplayName("존재하지 않는 이름일경우 false 를 반환")
    fun ifNotExists() {
        // given
        every { cafeRepository.existsByCafeName(notExistsCafe.name) } returns (false)

        // when
        val isExists = service.isExistsByName(notExistsCafe.name)

        //then
        assertFalse(isExists)
    }


    @Test
    @DisplayName("이미존재하는 카페이름으로 생성요청할경우 에러를 반환")
    fun reqCreateCafeIfExistsName() {
        // given
        every { cafeRepository.existsByCafeName(existsCafe.name) } returns (true)

        // when,then
        val exception = assertThrows<ResponseStatusException> {
            service.create(existsCafe)
        }
        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 카페를 생성할경우 생성 성공")
    fun reqCreateCafeIfNotExistsName() {
        // given
        every { cafeRepository.existsByCafeName(notExistsCafe.name) } returns (false)
        every { cafeRepository.create(notExistsCafe) }.returns(notExistsCafe)
        // when
        val result = service.create(notExistsCafe)
        // then
        assertEquals(notExistsCafe, result)
    }


    @Test
    @DisplayName("존재하지 않는 카페를 변경하려 할경우 에러반환")
    fun updateNotExistsCafe() {
        // given
        every { cafeRepository.getByCafeId(notExistsCafe.id) } returns (null)

        // when, then
        val exception = assertThrows<ResponseStatusException> {
            service.update(userId = notExistsCafe.ownerId, model = notExistsCafe)
        }
        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }

    @Test
    @DisplayName("카페 주인이 아닌사람이 카페를 변경하려 할경우 에러를 반환")
    fun updateCafeNotOwner() {
        // given
        every { cafeRepository.getByCafeId(existsCafe.id) } returns (existsCafe)

        // when,then
        val exception = assertThrows<ResponseStatusException> {
            service.update(userId = notExistsCafe.ownerId, model = existsCafe)
        }
        assertEquals(HttpStatus.FORBIDDEN, exception.statusCode)
    }

    @Test
    @DisplayName("카페 이름이 이미 존재하는 카페이름으로 변경하려 하면 에러를 반환")
    fun updateCafeNameExistsNameCafe() {
        // given
        every { cafeRepository.getByCafeId(existsCafe.id) }.returns(existsCafe)
        every { cafeRepository.existsByCafeName(existsCafe.name) }.returns(true)
        // when,then
        val exception = assertThrows<ResponseStatusException> {
            service.update(userId = existsCafe.ownerId, model = existsCafe)
        }
        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
    }

    @Test
    @DisplayName("카페주인이 카페이름을 변경하는데 카페이름이 존재하지않은 않는 카페일경우 성공")
    fun updateCafeNameNotExistsNameCafe() {
        // given
        every { cafeRepository.getByCafeName(notExistsCafe.name) }.returns(null)
        every { cafeRepository.getByCafeId(existsCafe.id) }.returns(existsCafe)
        every { cafeRepository.existsByCafeName(notExistsCafe.name) }.returns(false)
        every { cafeRepository.update(model = existsCafe.copy(name = notExistsCafe.name)) }.returns(
            existsCafe.copy(
                name = notExistsCafe.name,
            )
        )
        // when
        val result = service.update(
            userId = existsCafe.ownerId, model = existsCafe.copy(
                name = notExistsCafe.name,
            )
        )
        // then
        assertEquals(existsCafe.copy(name = notExistsCafe.name), result)
    }

    @Test
    @DisplayName("카페주인이 아닌사람이 카페를 삭제하려 할경우 에러를 반환")
    fun deleteCafeIfNotOwner() {
        // given
        every { cafeRepository.getByCafeId(existsCafe.id) }.returns(existsCafe)
        // when,then
        val result = assertThrows<ResponseStatusException> {
            service.delete(userId = notExistsCafe.ownerId, id = existsCafe.id)
        }
        assertEquals(HttpStatus.FORBIDDEN, result.statusCode)
    }

    @Test
    @DisplayName("카페주인이 카페를 삭제하려할경우 성공")
    fun deleteCafeIfOwner() {
        // given
        every { cafeRepository.getByCafeId(existsCafe.id) }.returns(existsCafe)
        every { cafeRepository.delete(existsCafe.id) }.returns(Unit)
        // when
        assertDoesNotThrow {
            service.delete(userId = existsCafe.ownerId, id = existsCafe.id)
        }
    }


    @Test
    @DisplayName("카페이름이 있을때 잘 가져오는지 확인")
    fun getByCafeNameExistsCafe() {
        // given
        every { cafeRepository.getByCafeName(existsCafe.name) }.returns(existsCafe)

        // when
        val result = service.getByCafeName(existsCafe.name)

        // then
        assertEquals(existsCafe, result)
    }

    @Test
    @DisplayName("존재하지 않는 카페를 가져올때 빈컨텐츠를 리턴")
    fun getByCafeNameNotExistsCafe() {
        // given
        every { cafeRepository.getByCafeName(notExistsCafe.name) }.returns(null)

        // when
        val result = service.getByCafeName(notExistsCafe.name)

        // then
        assertNull(result)
    }


    @Test
    @DisplayName("존재하는 카페를 전부 주는지 갯수확인")
    fun getAll() {
        val expectList = (1..10).map {
            existsCafe.copy(
                name = existsCafe.name + it.toString(),
                description = existsCafe.description + it.toString()
            )
        }.toList()
        // given
        every { cafeRepository.getAll() }.returns(expectList)

        // when
        val result = service.getAll()

        // then
        assertEquals(expectList, result)
    }
}