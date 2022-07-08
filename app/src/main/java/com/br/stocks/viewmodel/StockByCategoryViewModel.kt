package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.StockByCategoryResponse
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockByCategoryViewModel @Inject constructor(private var apiRepository: ApiRepository,application: Application):AndroidViewModel(application) {

    private val _stockByCategoryResponse: MutableStateFlow<NetworkResult<StockByCategoryResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val stockByCategoryResponse: MutableStateFlow<NetworkResult<StockByCategoryResponse>?> =
        _stockByCategoryResponse

    fun getStockByCategory(url: String) = viewModelScope.launch {
        apiRepository.getStockByCategory(url).collect {
            _stockByCategoryResponse.value = it
        }
    }
}