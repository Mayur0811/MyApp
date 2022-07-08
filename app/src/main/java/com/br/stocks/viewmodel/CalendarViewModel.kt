package com.br.stocks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.br.stocks.api.ApiRepository
import com.br.stocks.models.EventResponse
import com.br.stocks.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private var apiRepository: ApiRepository, application: Application):AndroidViewModel(application) {

    private val _event: MutableStateFlow<NetworkResult<ArrayList<EventResponse>>?> =
        MutableStateFlow(NetworkResult.Loading())
    val events: MutableStateFlow<NetworkResult<ArrayList<EventResponse>>?> = _event

    fun getEvents(url: String) = viewModelScope.launch {
        apiRepository.getEvents(url).collect {
            _event.value = it
        }
    }

}