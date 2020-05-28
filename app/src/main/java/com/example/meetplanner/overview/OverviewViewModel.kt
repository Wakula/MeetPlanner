package com.example.meetplanner.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetplanner.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OverviewViewModel : ViewModel() {
    private val _properties = MutableLiveData<List<OverviewMeetingProperty>>()
    private val _selectedProperty = MutableLiveData<MeetingDetailsProperty>()

    val authenticationFailed = MutableLiveData<Boolean>()
    val properties: LiveData<List<OverviewMeetingProperty>>
        get() = _properties
    val selectedProperty: LiveData<MeetingDetailsProperty>
        get() = _selectedProperty
    init {
        authenticationFailed.value = false
    }

    fun getMeetings() {
        val accessToken = Tokens.access
        MeetPlannerApi.retrofitService.getMeetings(
            "Bearer $accessToken"
        ).enqueue(object: Callback<List<OverviewMeetingProperty>> {
            override fun onFailure(call: Call<List<OverviewMeetingProperty>>?, t: Throwable?) {
                    Log.v("Failure", t?.message)
            }

            override fun onResponse(call: Call<List<OverviewMeetingProperty>>?, response: Response<List<OverviewMeetingProperty>>?) {
                Log.v("code", response!!.code().toString())
                when(response?.code()) {
                    200 -> {
                        _properties.value = response?.body()
                    }
                    else -> {
                        authenticationFailed.value = true
                        _properties.value = null
                    }
                }
            }
        })
    }
    fun displayPropertyDetails(property: OverviewMeetingProperty) {
        val accessToken = Tokens.access
        MeetPlannerApi.retrofitService.getMeetingDetails(
            "Bearer $accessToken", property.id
        ).enqueue(object: Callback<MeetingDetailsProperty> {
            override fun onFailure(call: Call<MeetingDetailsProperty>, t: Throwable) {
                Log.v("Failure", t?.message)
            }

            override fun onResponse(
                call: Call<MeetingDetailsProperty>,
                response: Response<MeetingDetailsProperty>
            ) {
                if (response.code() == 200) {
                    _selectedProperty.value = response.body()
                }
            }
        })
    }
    fun displayDetailsComplete() {
        _selectedProperty.value = null
    }
    fun restAuthenticationFailed() {
        authenticationFailed.value = false
    }
}