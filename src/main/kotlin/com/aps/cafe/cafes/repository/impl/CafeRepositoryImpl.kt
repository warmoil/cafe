package com.aps.cafe.cafes.repository.impl

import com.aps.cafe.cafes.entity.CafeEntity
import com.aps.cafe.cafes.model.CafeModel
import com.aps.cafe.cafes.repository.CafeRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface CafeJPARepository : JpaRepository<CafeEntity, Long> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): CafeEntity?
}

@Repository
class CafeRepositoryImpl(
    private val cafeRepo: CafeJPARepository
) : CafeRepository {


    override fun existsByCafeName(cafeName: String): Boolean {
        return cafeRepo.existsByName(name = cafeName)
    }

    override fun getByCafeName(cafeName: String): CafeModel? {
        return cafeRepo.findByName(cafeName)?.toModel()
    }

    override fun getByCafeId(id: Long): CafeModel? {
        return cafeRepo.findById(id).orElse(null)?.toModel()
    }

    override fun create(model: CafeModel): CafeModel {
        cafeRepo.save(model.toEntity())
        return model
    }

    override fun update(model: CafeModel): CafeModel {
        cafeRepo.save(model.toEntity())
        return model
    }

    override fun delete(id: Long) {
        cafeRepo.deleteById(id)
    }

    override fun getAll(): List<CafeModel> {
        return cafeRepo.findAll().map { it.toModel() }
    }

    private fun CafeEntity.toModel() = CafeModel(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        ownerNickname = ownerNickname,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun CafeModel.toEntity() = CafeEntity(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        ownerNickname = ownerNickname,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

}