package com.example.meetplanner.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MeetingDetailsProperty (
    val name: String,
    val description: String
) : Parcelable
