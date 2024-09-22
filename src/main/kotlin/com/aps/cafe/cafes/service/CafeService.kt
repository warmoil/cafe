package com.aps.cafe.cafes.service

import com.aps.cafe.cafes.model.CafeModel

interface CafeService {
    fun isExistsByName(name: String): Boolean

    fun create(model: CafeModel): CafeModel

    fun update(userId: String, model: CafeModel): CafeModel

    fun delete(userId: String, id: Long)

    fun getByCafeName(cafeName: String): CafeModel?

    fun getAll(): List<CafeModel>

}