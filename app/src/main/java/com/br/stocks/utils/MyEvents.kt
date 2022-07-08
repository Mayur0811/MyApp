package com.br.stocks.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.EventDay
import com.br.stocks.models.EventResponse
import java.util.*
import kotlin.collections.ArrayList

class MyEventDay : EventDay, Parcelable {
    private var data:ArrayList<EventResponse>?=null
    private var eventResponse = ArrayList<EventResponse>()

    constructor(day: Calendar, imageResource: Int, data:ArrayList<EventResponse>) : super(day, imageResource) {
        this.data = data
    }

    fun getData():ArrayList<EventResponse>? {
        return data
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private constructor(`in`: Parcel) : super(`in`.readSerializable() as Calendar?,
        `in`.readInt()) {
        data = readArrayList(eventResponse)
    }

    @SuppressLint("RestrictedApi")
    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeSerializable(calendar)
        parcel.writeInt((imageDrawable as Int))
        parcel.writeArray(arrayOf(data))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyEventDay> {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun createFromParcel(parcel: Parcel): MyEventDay {
            return MyEventDay(parcel)
        }

        override fun newArray(size: Int): Array<MyEventDay?> {
            return arrayOfNulls(size)
        }
    }

    private fun readArrayList(eventResponse: ArrayList<EventResponse>): ArrayList<EventResponse> {
        return eventResponse
    }
}


