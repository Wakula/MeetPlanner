package com.example.meetplanner.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.meetplanner.network.MeetingDetailsProperty

class DetailsViewModel(property: MeetingDetailsProperty, app: Application) : AndroidViewModel(app) {
    private val _property = MutableLiveData<MeetingDetailsProperty>()
    val property: LiveData<MeetingDetailsProperty>
        get() = _property
    init {
        _property.value = property
    }
}