package com.aps.cafe.cafes.repository

import com.aps.cafe.cafes.model.CafeModel

interface CafeRepository {
    fun existsByCafeName(cafeId: String): Boolean
    fun create(model: CafeModel): CafeModel
    fun update(model: CafeModel): CafeModel
    fun delete(model: CafeModel)
    fun getAll(): List<CafeModel>
}