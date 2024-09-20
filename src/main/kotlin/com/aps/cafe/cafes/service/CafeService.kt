package com.aps.cafe.cafes.service

import com.aps.cafe.cafes.entity.CafeEntity
import com.aps.cafe.cafes.model.CafeModel

interface CafeService {
    fun isExists(name: String): Boolean

    fun create(model: CafeModel): CafeModel

    fun update(userId: String, model: CafeModel): CafeModel

    fun delete(userId: String, id: Long)

    fun getAll(): List<CafeModel>

}