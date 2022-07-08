package com.br.stocks.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.br.stocks.R
import com.br.stocks.adapter.EventAdapter
import com.br.stocks.databinding.FragmentCalendarBinding
import com.br.stocks.models.EventResponse
import com.br.stocks.ui.WebViewActivity
import com.br.stocks.utils.CommonUtils
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.text.SimpleDateFormat


@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private val events: MutableList<EventDay> = ArrayList()
    private var regex = Regex("\\[1-9][0-9]{2}")
    private var eventData: ArrayList<EventResponse> = ArrayList()
    private var eventResponse: ArrayList<EventResponse> = ArrayList()
    private var dateResponse: ArrayList<String> = ArrayList()
    private val input = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH)
    private val output = SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH)
    lateinit var layoutManager: LinearLayoutManager
    private var eventResponseDataList: ArrayList<EventResponse> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        layoutManager = LinearLayoutManager(requireContext())

        eventAdapter = EventAdapter(eventResponseDataList)

        if (isInternetConnected(requireContext())) {
            callEventApi()
        } else {
            requireContext().toast(getString(R.string.no_internet))
        }

        initView()
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    private fun initView() {
        setMinimumDate()
        binding.calendarView.setOnDayClickListener {
            if (it.imageDrawable != null) {
                eventData.clear()
                val date=input.parse(it.calendar.time.toString())
                for (i in eventResponse.indices) {
                    if ((eventResponse[i].meetingDate).equals(date?.let { it1 -> output.format(it1) })) {
                        eventData.add(eventResponse[i])
                    }
                }
                initEventAdapter(eventData)

            } else {
                eventData.clear()
                initEventAdapter(eventData)
            }
        }

        eventAdapter.onEventItemClick = {
            if (it.url != null) {
                val i = Intent(requireContext(), WebViewActivity::class.java)
                i.putExtra("url", it.url)
                i.putExtra("title", it.longName)
                startActivity(i)
            }
        }

    }

    private fun setMinimumDate() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        binding.calendarView.setMinimumDate(calendar)
    }

    private fun showTodayEvent(eventResponse: List<EventResponse>?) {
        val today = Calendar.getInstance()
        binding.calendarView.setDate(today)
        if (eventResponse != null) {
            val date=input.parse(today.time.toString())
            for (i in eventResponse.indices) {
                try {
                    if ((eventResponse[i].meetingDate).equals(date?.let { it1 -> output.format(it1) })) {
                        eventData.add(eventResponse[i])
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            initEventAdapter(eventData)
        }
    }

    private fun callEventApi() {
        val dateRange = CommonUtils.getDateRange()
        val fromDate = CommonUtils.getDate(dateRange.first)
        val toDate = CommonUtils.getDate(dateRange.second)
        val string = StringBuilder()
        string.append(Constant.FirstPart).append(fromDate).append(Constant.SecondPart)
            .append(toDate)
        val url = string.toString()
        calendarViewModel.getEvents(url)
        lifecycleScope.launchWhenCreated {
            calendarViewModel.events.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.d("TAG", "callEventApi: done")
                        if (it.data != null) {
                            eventResponse = it.data
                            for (i in eventResponse.indices) {
                                val date = eventResponse[i].meetingDate
                                if (!dateResponse.contains(date)) {
                                    if (date != null) {
                                        dateResponse.add(date)
                                    }
                                }
                            }
                            setEvents()
                            binding.progressCircular.visibility = ProgressBar.GONE
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.d("TAG", "callEventApi: error")
                        binding.progressCircular.visibility = ProgressBar.GONE
                    }
                    is NetworkResult.Loading -> {
                        binding.progressCircular.visibility = ProgressBar.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }


    private fun setEvents() {
        for (i in dateResponse.indices) {
            val stringDate = dateResponse[i].split(" ")
            val day = stringDate[0].toInt()
            val year = stringDate[2].toInt()
            val month = if (stringDate[1].matches(regex)) {
                stringDate[1].toInt() - 1
            } else {
                CommonUtils.getMonth(stringDate[1])
            }
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            events.add(EventDay(calendar, R.drawable.dot))
        }
        binding.calendarView.setEvents(events)
        showTodayEvent(eventResponse)
    }

    private fun initEventAdapter(eventResponseData: ArrayList<EventResponse>) {
        binding.rcvEvent.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rcvEvent.adapter = eventAdapter
        binding.progressCircular.visibility = ProgressBar.GONE

        loadData(eventResponseData)
        if (eventResponseData.size > 9 && layoutManager.findLastCompletelyVisibleItemPosition() != eventResponseData.size) {
            scrollLister(eventResponseData)
        }
    }

    private fun loadData(eventResponseData: ArrayList<EventResponse>) {
        eventResponseDataList.clear()
        if (eventResponseData.size < 9) {
            for (i in eventResponseData.indices) {
                eventResponseDataList.add(eventResponseData[i])
            }
        } else {
            for (i in 0 until 9) {
                eventResponseDataList.add(eventResponseData[i])
            }
        }
        eventAdapter.notifyDataSetChanged()
    }

    private fun scrollLister(eventResponseData: ArrayList<EventResponse>) {
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                if (eventResponseDataList.size != eventResponseData.size) {
                    binding.progressBar.visibility = ProgressBar.VISIBLE
                    val nextLimit = eventResponseDataList.size + 10
                    val finalLimit = if (nextLimit > eventResponseData.size) {
                        eventResponseDataList.size + ((eventResponseData.size + 1) % 10)
                    } else {
                        nextLimit
                    }
                    for (i in eventResponseDataList.size until finalLimit) {
                        eventResponseDataList.add(eventResponseData[i])
                    }
                    eventAdapter.notifyItemInserted(finalLimit)
                    Handler().postDelayed({
                        binding.progressBar.visibility = ProgressBar.GONE
                    }, 1000)
                }
            }
        })
    }

    companion object {
        fun newInstance() = CalendarFragment()
    }

}