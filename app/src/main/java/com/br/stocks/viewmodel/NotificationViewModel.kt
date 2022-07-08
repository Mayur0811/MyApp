package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.EventResponse
import com.br.stocks.models.NotificationResponse
import com.br.stocks.models.StockByCategoryResponse
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private var apiRepository: ApiRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _notification: MutableStateFlow<NetworkResult<NotificationResponse>?> =
        MutableStateFlow(NetworkResult.Loading())
    val notification: MutableStateFlow<NetworkResult<NotificationResponse>?> = _notification

    fun getNotification(notification: String) = viewModelScope.launch {
        apiRepository.getNotification(notification).collect {
            _notification.value = it
        }
    }

}