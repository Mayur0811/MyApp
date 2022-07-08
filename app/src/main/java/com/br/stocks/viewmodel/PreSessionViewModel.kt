package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.PreSessionResponse
import com.br.stocks.models.StockDataResponse
import com.br.stocks.room.DataBaseRepository
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreSessionViewModel @Inject constructor(
    private var apiRepository: ApiRepository,
    var dataBaseRepository: DataBaseRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _preSession: MutableStateFlow<NetworkResult<PreSessionResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val preSession: MutableStateFlow<NetworkResult<PreSessionResponse>?> = _preSession

    fun getPreSession(session: String) = viewModelScope.launch {
        apiRepository.getPreSession(session).collect {
            _preSession.value = it
        }
    }

}