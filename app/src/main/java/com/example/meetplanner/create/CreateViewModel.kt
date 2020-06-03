package com.example.meetplanner.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetplanner.network.MeetPlannerApi
import com.example.meetplanner.network.MeetingDetailsProperty
import com.example.meetplanner.network.Tokens
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateViewModel: ViewModel() {
    private val _meeting = MutableLiveData<MeetingDetailsProperty>()
    private val _authenticationFailed = MutableLiveData<Boolean>()

    val meeting: LiveData<MeetingDetailsProperty>
        get() = _meeting
    val authenticationFailed: LiveData<Boolean>
        get() = _authenticationFailed

    init {
        _authenticationFailed.value = false
    }

    fun createMeeting(name: String, description: String) {
        val accessToken = "Bearer ${Tokens.access}"
        val meeting = MeetingDetailsProperty(
            name, description
        )
        MeetPlannerApi.retrofitService.createMeeting(accessToken, meeting)
            .enqueue(object: Callback<MeetingDetailsProperty> {
                override fun onFailure(call: Call<MeetingDetailsProperty>?, t: Throwable?) {
                    Log.v("Failure", t?.message)
                }

                override fun onResponse(call: Call<MeetingDetailsProperty>?, response: Response<MeetingDetailsProperty>?) {
                    when(response!!.code()) {
                        201 -> {
                            _meeting.value = response.body()
                        }
                        401 -> {
                            _authenticationFailed.value = true
                        }
                    }
                }
            })
    }
    fun resetAuthenticationFailed() {
        _authenticationFailed.value = false
    }
}
