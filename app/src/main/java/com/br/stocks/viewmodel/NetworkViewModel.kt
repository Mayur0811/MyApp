package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.*
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private var apiRepository: ApiRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _registerDevice: MutableStateFlow<NetworkResult<RegisterDeviceResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val registerDevice: MutableStateFlow<NetworkResult<RegisterDeviceResponse>?> = _registerDevice


     fun registerDevice(body: RequestBody) = viewModelScope.launch {
        apiRepository.registerDevice(body).collect {
            _registerDevice.value = it
        }
    }



}