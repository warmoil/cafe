package com.aps.cafe.cafes.service.impl

import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.repository.CafeRepository
import com.aps.cafe.cafes.service.CafeService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CafeServiceImpl(
    private val cafeRepository: CafeRepository
) : CafeService {
    override fun isExistsByName(name: String): Boolean {
        return cafeRepository.existsByCafeName(name)
    }

    override fun create(model: CafeModel): CafeModel {
        if (cafeRepository.existsByCafeName(model.name)) throw ResponseStatusException(
            HttpStatus.CONFLICT,
            "이미 존재하는 카페 이름입니다"
        )
        return cafeRepository.create(model)
    }

    override fun update(userId: String, model: CafeModel): CafeModel {
        val findCafe = cafeRepository.getByCafeId(model.id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (findCafe.ownerId != userId) throw ResponseStatusException(HttpStatus.FORBIDDEN, "누구세요?")

        if (cafeRepository.existsByCafeName(model.name)) throw ResponseStatusException(
            HttpStatus.CONFLICT,
            "이미 존재하는 카페이름입니다."
        )

        return cafeRepository.update(model)
    }

    override fun delete(userId: String, id: Long) {
        val findCafe = cafeRepository.getByCafeId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (findCafe.ownerId != userId) throw ResponseStatusException(HttpStatus.FORBIDDEN, "누구세요?")
        cafeRepository.delete(id)
    }

    override fun getByCafeName(cafeName: String): CafeModel? {
        return cafeRepository.getByCafeName(cafeName)
    }

    override fun getAll(): List<CafeModel> {
        return cafeRepository.getAll()
    }
}