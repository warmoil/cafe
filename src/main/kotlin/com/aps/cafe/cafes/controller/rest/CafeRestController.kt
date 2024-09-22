package com.aps.cafe.cafes.controller.rest

import com.aps.cafe.cafes.controller.dto.CafeCreateDto
import com.aps.cafe.cafes.controller.dto.CafeUpdateDto
import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.service.impl.CafeSearchService
import com.aps.cafe.cafes.service.CafeService
import com.aps.cafe.common.CommonResponseEntity
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cafes")
@Validated
class CafeRestController(
    private val cafeService: CafeService,
    private val searchService: CafeSearchService,
) {

//    @PostConstruct
//    fun init() {
//        val cafes = cafeService.getAll()
//        searchService.init(cafes)
//    }

    // 카페 이름 중복확인
    @GetMapping("/check-duplicate")
    fun checkDuplicate(@Valid @RequestParam @Min(2) @NotBlank name: String): ResponseEntity<CommonResponseEntity<Boolean>> {
        return ResponseEntity.ok(CommonResponseEntity(cafeService.isExistsByName(name)))
    }

    // 생성
    @PostMapping
    // todo 이후 jwt 토큰 도입후 유저정보 자동으로 가져오게 수정
    fun create(@RequestParam userId: String, @Valid @RequestBody cafe: CafeCreateDto): ResponseEntity<CafeModel> {
        cafeService.create(
            CafeModel(
                name = cafe.name,
                description = cafe.designation,
                ownerId = userId,
                ownerNickname = cafe.ownerNickname,
            )
        )
        return ResponseEntity<CafeModel>(HttpStatus.CREATED)
    }
    // 정보 수정

    @PatchMapping("/{id}")
    fun update(
        @RequestParam userId: String,
        @Valid @RequestBody cafe: CafeUpdateDto,
        @PathVariable id: Long
    ): ResponseEntity<HttpStatus> {
        cafeService.update(
            userId, CafeModel(
                id = id,
                name = cafe.name,
                description = cafe.designation,
                ownerId = userId,
                ownerNickname = userId
            )
        )
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    // 조회
    // todo 추후 searchService 로 변경
    @GetMapping("/search")
    fun search(@RequestParam keyword: String): ResponseEntity<List<CafeModel>> {
        val result = cafeService.getByCafeName(keyword)
        return ResponseEntity.ok(
            if (result == null) emptyList() else listOf(result)
        )
    }


    // 삭제
    @DeleteMapping("/{id}")
    fun delete(@RequestParam userId: String, @PathVariable id: Long): ResponseEntity<HttpStatus> {
        cafeService.delete(userId = userId, id = id)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }
}