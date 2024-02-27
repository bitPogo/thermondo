/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlDriverMock
import io.bitpogo.keather.data.location.LocationQueries
import io.bitpogo.keather.data.weather.ClientProviderMock
import io.bitpogo.keather.data.weather.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.database.WeatherQueries
import io.bitpogo.keather.data.weather.kmock
import io.bitpogo.keather.http.networking.NetworkingContract
import io.bitpogo.keather.http.networking.RequestBuilderMock
import io.bitpogo.keather.interactor.repository.RepositoryContract
import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.isNot

@OptIn(KMockExperimental::class)
@KMock(
    SqlDriver::class,
    WeatherRepositoryContract.ClientProvider::class,
    NetworkingContract.RequestBuilder::class,
)
class KoinSpec {
    @Test
    @JsName("fn0")
    fun `It contains an WeatherRepositoryStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherRepository(),
                module {
                    single { LocationQueries(kmock<SqlDriverMock>()) }
                    single { WeatherQueries(kmock<SqlDriverMock>()) }
                },
            )
        }

        // When
        val store: WeatherRepositoryContract.Store? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn1")
    fun `It contains an ClientProvider`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherRepository(),
            )
        }

        // When
        val store: WeatherRepositoryContract.ClientProvider? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn2")
    fun `It contains a WeatherRepositoryContractApi`() {
        // Given
        val provider: ClientProviderMock = kmock()
        provider._provide returns kmock<RequestBuilderMock>()

        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveWeatherRepository(),
                module {
                    single<WeatherRepositoryContract.ClientProvider> { provider }
                },
            )
        }

        // When
        val store: WeatherRepositoryContract.Api? = koin.koin.getOrNull()

        // Then
        store isNot null
        verify { provider._provide.hasBeenCalled() }
    }

    @Test
    @JsName("fn3")
    fun `It contains an LocationRepositoryContract`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherRepository(),
                module {
                    single { LocationQueries(kmock<SqlDriverMock>()) }
                    single { WeatherQueries(kmock<SqlDriverMock>()) }
                    single<CoroutineDispatcher>(named("io")) { StandardTestDispatcher() }
                },
            )
        }

        // When
        val store: RepositoryContract.WeatherRepository? = koin.koin.getOrNull()

        // Then
        store isNot null
    }
}
