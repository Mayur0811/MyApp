package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.InsuranceNewsResponse
import com.br.stocks.models.NewsResponse
import com.br.stocks.models.PreSessionResponse
import com.br.stocks.models.StockDataResponse
import com.br.stocks.room.DataBaseRepository
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private var apiRepository: ApiRepository,
    var dataBaseRepository: DataBaseRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _mutualFundNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val mutualFundNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> =
        _mutualFundNewsResponse

    private val _economyNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val economyNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> = _economyNewsResponse

    private val _ipoNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val ipoNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> = _ipoNewsResponse

    private val _foreignMarketNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val foreignMarketNewsResponse: MutableStateFlow<NetworkResult<NewsResponse>?> =
        _foreignMarketNewsResponse

    fun getMutualFundNews(url: String) = viewModelScope.launch {
        apiRepository.getMutualFundNews(url).collect {
            _mutualFundNewsResponse.value = it
        }
    }

    fun getEconomyNews(url: String) = viewModelScope.launch {
        apiRepository.getEconomyNews(url).collect {
            _economyNewsResponse.value = it
        }
    }

    fun getIPONews(url: String) = viewModelScope.launch {
        apiRepository.getIPONews(url).collect {
            _ipoNewsResponse.value = it
        }
    }

    fun getForeignMarketNews(url: String) = viewModelScope.launch {
        apiRepository.getForeignMarketNews(url).collect {
            _foreignMarketNewsResponse.value = it
        }
    }


}