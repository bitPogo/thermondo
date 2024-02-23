/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.entity

import kotlin.jvm.JvmInline

@JvmInline
value class Longitude(val lat: Double)

@JvmInline
value class Latitude(val lat: Double)

data class Location(
    val latitude: Latitude,
    val longitude: Longitude,
)
