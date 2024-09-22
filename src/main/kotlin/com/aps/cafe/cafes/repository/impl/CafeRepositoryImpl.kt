package com.aps.cafe.cafes.repository.impl

import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.repository.CafeRepository
import org.springframework.stereotype.Repository

@Repository
class CafeRepositoryImpl : CafeRepository {
    override fun existsByCafeName(cafeName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getByCafeName(cafeName: String): CafeModel? {
        TODO("Not yet implemented")
    }

    override fun getByCafeId(id: Long): CafeModel? {
        TODO("Not yet implemented")
    }

    override fun create(model: CafeModel): CafeModel {
        TODO("Not yet implemented")
    }

    override fun update(model: CafeModel): CafeModel {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<CafeModel> {
        TODO("Not yet implemented")
    }
}