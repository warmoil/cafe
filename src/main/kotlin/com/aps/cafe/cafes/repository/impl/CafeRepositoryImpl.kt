package com.aps.cafe.cafes.repository.impl

import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.repository.CafeRepository
import org.springframework.stereotype.Repository

@Repository
class CafeRepositoryImpl: CafeRepository {
    override fun existsByCafeName(cafeId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun create(model: CafeModel): CafeModel {
        TODO("Not yet implemented")
    }

    override fun update(model: CafeModel): CafeModel {
        TODO("Not yet implemented")
    }

    override fun delete(model: CafeModel) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<CafeModel> {
        TODO("Not yet implemented")
    }
}