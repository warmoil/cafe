package com.aps.cafe.cafes.service.impl

import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.repository.CafeRepository
import com.aps.cafe.cafes.service.CafeService
import org.springframework.stereotype.Service

@Service
class CafeServiceImpl(
    private val cafeRepository: CafeRepository
) : CafeService {
    override fun isExists(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun create(model: CafeModel): CafeModel {
        TODO("Not yet implemented")
    }

    override fun update(userId: String, model: CafeModel): CafeModel {
        TODO("Not yet implemented")
    }

    override fun delete(userId: String, id: Long) {
        TODO("Not yet implemented")
    }

    override fun getByCafeName(cafeName: String): CafeModel? {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<CafeModel> {
        TODO("Not yet implemented")
    }
}