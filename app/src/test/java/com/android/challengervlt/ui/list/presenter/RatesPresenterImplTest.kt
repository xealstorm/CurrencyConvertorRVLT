package com.android.challengervlt.ui.list.presenter

import com.android.challengervlt.data.CurrencyDao
import com.android.challengervlt.data.CurrencyRepositoryImpl
import com.android.challengervlt.data.RateRepositoryImpl
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.network.NetworkService
import com.android.challengervlt.network.NetworkServiceImpl
import com.android.challengervlt.network.model.ItemsResponse
import com.android.challengervlt.ui.list.view.RatesView
import com.android.challengervlt.util.format.TimeFormatter
import com.android.challengervlt.util.scedulers.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RatesPresenterImplTest {
    @Mock
    internal var networkService: NetworkService = Mockito.mock(NetworkServiceImpl::class.java)
    @Mock
    internal var currencyRepository: CurrencyRepositoryImpl =
        Mockito.mock(CurrencyRepositoryImpl::class.java)
    @Mock
    internal var rateRepository: RateRepositoryImpl = Mockito.mock(RateRepositoryImpl::class.java)

    @Mock
    internal var view: RatesView? = null

    private lateinit var testScheduler: TestScheduler

    var presenter: RatesPresenterImpl? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        presenter = RatesPresenterImpl(
            networkService,
            currencyRepository,
            rateRepository,
            testSchedulerProvider
        )
        presenter!!.setView(view!!)
    }

    @Test
    fun `network call is made when getRates is triggered`() {
        Mockito.doReturn(Single.just(ItemsResponse())).`when`(networkService)
            .getRates(BASE_CURRENCY_MOCK)
        presenter!!.loadRates(BASE_CURRENCY_MOCK)
        testScheduler.triggerActions()
        Mockito.verify(networkService)!!.getRates(BASE_CURRENCY_MOCK)
    }

    @Test
    fun `updateList is triggered after the network call returns a result`() {
        Mockito.doReturn(Single.just(ItemsResponse())).`when`(networkService)
            .getRates(BASE_CURRENCY_MOCK)
        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.loadRates(BASE_CURRENCY_MOCK)
        testScheduler.triggerActions()
        Mockito.verify(spyPresenter)?.updateList(listOf())
    }

    @Test
    fun `nothing is put to db when empty result comes from the network`() {
        Mockito.doReturn(Single.just(ItemsResponse())).`when`(networkService)
            .getRates(BASE_CURRENCY_MOCK)
        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.loadRates(BASE_CURRENCY_MOCK)
        testScheduler.triggerActions()
        Mockito.verify(rateRepository, Mockito.times(0))!!.addItem(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyLong(),
            ArgumentMatchers.anyDouble()
        )
    }

    @Test
    fun `all items from the network are put into db`() {
        Mockito.doReturn(
            Single.just(
                ItemsResponse(
                    BASE_CURRENCY_MOCK,
                    DATE_MOCK,
                    RATE_NETWORK_MOCK
                )
            )
        )
            .`when`(networkService)
            .getRates(BASE_CURRENCY_MOCK)

        Mockito.doReturn(CURRENCIES_MOCK)
            .`when`(currencyRepository).getItems()

        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.loadRates(BASE_CURRENCY_MOCK)
        testScheduler.triggerActions()
        Mockito.verify(rateRepository, Mockito.times(3))!!.addItem(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyLong(),
            ArgumentMatchers.anyDouble()
        )
    }

    @Test
    fun `the items from the network are inserted to db unchanged`() {
        Mockito.doReturn(
            Single.just(
                ItemsResponse(
                    BASE_CURRENCY_MOCK,
                    DATE_MOCK,
                    RATE_NETWORK_MOCK
                )
            )
        )
            .`when`(networkService).getRates(BASE_CURRENCY_MOCK)
        Mockito.doReturn(CURRENCIES_MOCK)
            .`when`(currencyRepository).getItems()

        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.loadRates(BASE_CURRENCY_MOCK)
        testScheduler.triggerActions()
        Mockito.verify(rateRepository, Mockito.times(1))!!.addItem(
            RATE_NETWORK_MOCK.keys.first()!!,
            BASE_CURRENCY_MOCK,
            TimeFormatter.dateDeFormatted(DATE_MOCK)!!.millis,
            RATE_NETWORK_MOCK.values.first()!!
        )
    }

    @Test
    fun `the currencies are requested if they are not present in db`() {
        Mockito.doReturn(false)
            .`when`(currencyRepository).hasAny()
        Mockito.doReturn(Single.just(CURRENCY_NETWORK_MOCK))
            .`when`(networkService)
            .getCurrencies()
        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.loadItems(BASE_CURRENCY_MOCK)
        Mockito.verify(spyPresenter, Mockito.times(1))
            .loadCurrenciesAndRates(BASE_CURRENCY_MOCK)
    }

    @Test
    fun `the currencies are not requested if they are present in db`() {
        Mockito.doReturn(true)
            .`when`(currencyRepository).hasAny()
        Mockito.doReturn(Single.just(CURRENCY_NETWORK_MOCK))
            .`when`(networkService)
            .getCurrencies()
        Mockito.doReturn(
            Single.just(
                ItemsResponse(
                    BASE_CURRENCY_MOCK,
                    DATE_MOCK,
                    RATE_NETWORK_MOCK
                )
            )
        )
            .`when`(networkService).getRates(BASE_CURRENCY_MOCK)
        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.loadItems(BASE_CURRENCY_MOCK)
        Mockito.verify(spyPresenter, Mockito.times(0))
            .loadCurrenciesAndRates(BASE_CURRENCY_MOCK)
    }

    @Test
    fun `update items in UI if they are returned`() {
        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.updateList(CURRENCY_ITEMS_MOCK)
        Mockito.verify(spyPresenter.getView())!!.updateItems(CURRENCY_ITEMS_MOCK)
    }

    @Test
    fun `clear UI if no items are returned`() {
        val spyPresenter = Mockito.spy(presenter)
        spyPresenter!!.updateList(listOf())
        Mockito.verify(spyPresenter.getView())!!.resetList()
    }

    companion object {
        const val BASE_CURRENCY_MOCK = "EUR"

        const val DATE_MOCK = "2020-02-15"

        val RATE_NETWORK_MOCK: Map<String?, Double?> = mapOf("USD" to 0.9, "AUS" to 1.2)

        val CURRENCY_NETWORK_MOCK: Map<String?, String?> =
            mapOf("USD" to "United States Dollar", "AUS" to "Australian Dollar")

        val CURRENCIES_MOCK: List<CurrencyDao> = listOf(
            CurrencyDao("EUR", "Euro"),
            CurrencyDao("USD", "United States Dollar"),
            CurrencyDao("AUS", "Australian Dollar")
        )

        val CURRENCY_ITEMS_MOCK: List<CurrencyItem> = listOf(
            CurrencyItem("EUR", "Euro", 1.0),
            CurrencyItem("USD", "United States Dollar", 0.9),
            CurrencyItem("AUS", "Australian Dollar", 1.2)
        )
    }
}