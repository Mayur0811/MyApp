package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.PostData
import com.br.stocks.models.StockDataResponse
import com.br.stocks.room.DataBaseRepository
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var apiRepository: ApiRepository,
    var dataBaseRepository: DataBaseRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _stock: MutableStateFlow<NetworkResult<StockDataResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val stockData: MutableStateFlow<NetworkResult<StockDataResponse>?> = _stock

    private var _getStockDataFromRoom: MutableLiveData<StockDataResponse> = MutableLiveData()
    var getStockDataFromRoom: MutableLiveData<StockDataResponse> = _getStockDataFromRoom

    private var _getPostData:MutableStateFlow<NetworkResult<PostData>?> = MutableStateFlow(NetworkResult.Loading())
    var getPostData:MutableStateFlow<NetworkResult<PostData>?> = _getPostData

    fun getStockData(url: String) = viewModelScope.launch {
        apiRepository.getStockData(url).collect {
            _stock.value = it
            dataBaseRepository.delete()
            _stock.value!!.data?.let { it1 -> dataBaseRepository.insertStockData(it1) }
            getStockDataFromRoom()
        }
    }

    fun getStockDataFromRoom() = viewModelScope.launch {
        dataBaseRepository.getStockData().collect {
            _getStockDataFromRoom.value = it
        }
    }

    fun getPostData(post_id:String)=viewModelScope.launch {
        apiRepository.getPost(post_id).collect{
            _getPostData.value=it
        }
    }
}
