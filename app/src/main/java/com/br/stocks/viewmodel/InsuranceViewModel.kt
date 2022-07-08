package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.InsuranceNewsResponse
import com.br.stocks.models.PreSessionResponse
import com.br.stocks.room.DataBaseRepository
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsuranceViewModel @Inject constructor(
    private var apiRepository: ApiRepository,
    var dataBaseRepository: DataBaseRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _insuranceNewsResponse: MutableStateFlow<NetworkResult<InsuranceNewsResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val insuranceNewsResponse: MutableStateFlow<NetworkResult<InsuranceNewsResponse>?> =
        _insuranceNewsResponse


    fun getInsuranceNews(url: String) = viewModelScope.launch {
        apiRepository.getInsuranceNews(url).collect {
            _insuranceNewsResponse.value = it
        }
    }


}