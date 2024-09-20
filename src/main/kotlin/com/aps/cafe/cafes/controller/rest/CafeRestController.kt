package com.aps.cafe.cafes.controller.rest

import com.aps.cafe.cafes.controller.dto.CafeCreateDto
import com.aps.cafe.cafes.controller.dto.CafeUpdateDto
import com.aps.cafe.cafes.model.CafeModel
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cafes")
class CafeRestController {

    // 카페 이름 중복확인
    @GetMapping("/check-duplicate")
    fun checkDuplicate(): ResponseEntity<HttpStatus> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }

    // 생성
    @PostMapping
    fun create(@Valid @RequestBody cafe: CafeCreateDto): ResponseEntity<CafeModel> {
        return ResponseEntity<CafeModel>(HttpStatus.CREATED)
    }
    // 정보 수정

    @PatchMapping("/{id}")
    fun update(@Valid @RequestBody cafe: CafeUpdateDto, @PathVariable id: Long): ResponseEntity<HttpStatus> {
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    // 조회
    @GetMapping("/search")
    fun search(@RequestParam keyword: String): ResponseEntity<List<CafeModel>> {
        return ResponseEntity<List<CafeModel>>(HttpStatus.OK)
    }


    // 삭제
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<HttpStatus> {
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }
}