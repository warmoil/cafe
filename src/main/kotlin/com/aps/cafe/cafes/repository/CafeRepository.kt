package com.aps.cafe.cafes.repository

import com.aps.cafe.cafes.model.CafeModel

interface CafeRepository {
    fun existsByCafeName(cafeName: String): Boolean
    fun getByCafeName(cafeName: String): CafeModel?
    fun getByCafeId(id: Long): CafeModel?
    fun create(model: CafeModel): CafeModel
    fun update(model: CafeModel): CafeModel
    fun delete(id: Long)
    fun getAll(): List<CafeModel>
}