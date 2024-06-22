package cl.itau.challenge

import cl.itau.challenge.data.di.dataModule
import cl.itau.challenge.domain.di.domainModule
import cl.itau.challenge.presentation.di.presentationModule
import io.mockk.mockkClass
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule

open class BaseUnitTest: KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    open fun setUp() { }
    open fun tearDown() { }

    @Before
    open fun initTest() {
        startKoin {
            modules(appModuleTest, dataModule, domainModule, presentationModule)
        }

        setUp()
    }

    @After
    open fun finishTest() {
        stopKoin()
        tearDown()
    }
}