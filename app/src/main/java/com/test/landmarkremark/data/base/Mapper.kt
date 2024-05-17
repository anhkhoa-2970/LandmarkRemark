package com.test.landmarkremark.data.base

interface Mapper<E, M> {
	fun fromEntity(from: E): M
}


