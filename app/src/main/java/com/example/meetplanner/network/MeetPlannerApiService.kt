package com.example.meetplanner.network

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.meetplanner.authentication.AuthViewModel
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.time.LocalDateTime

class TokenAuthenticatior: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = TokensProperty(access="", refresh=requireNotNull(Tokens.refresh))
        MeetPlannerApi.retrofitService.refreshAccessToken(token).enqueue(object: Callback<TokensProperty> {
            override fun onFailure(call: Call<TokensProperty>?, t: Throwable?) {
                Tokens.clear()
            }
            override fun onResponse(call: Call<TokensProperty>?, response: retrofit2.Response<TokensProperty>?) {
                Tokens.access = response?.body()?.access
            }
        })
        if (Tokens.access == "") {
            return null
        }
        val newAccessToken = "Bearer ${Tokens.access}"
        return response.request().newBuilder()
            .header("Authorization", newAccessToken)
            .build()
    }
}

class LocalDateTimeAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String {
        return value.toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}

private const val BASE_URL = "http://192.168.1.103:8000/api/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(LocalDateTimeAdapter())
    .build()

val client = OkHttpClient.Builder()
    .authenticator(TokenAuthenticatior())
    .retryOnConnectionFailure(false)
    .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .baseUrl(BASE_URL)
        .build()

interface MeetPlannerApiService {
    @GET("meetings/")
    fun getMeetings(@Header("Authorization") accessToken: String):
            Call<List<OverviewMeetingProperty>>
    @POST("sign-up/")
    fun signUp(@Body userProperty: UserProperty):
            Call<UserProperty>
    @POST("access-token/")
    fun login(@Body userProperty: UserProperty):
            Call<TokensProperty>
    @POST("refresh-token/")
    fun refreshAccessToken(@Body refreshToken: TokensProperty):
            Call<TokensProperty>
    @GET("meetings/{id}/")
    fun getMeetingDetails(
        @Header("Authorization") accessToken: String,
        @Path("id") meetingId: Int):
            Call<MeetingDetailsProperty>
    @POST("meetings/")
    fun createMeeting(
        @Header("Authorization") accessToken: String,
        @Body meeting: MeetingDetailsProperty):
            Call<MeetingDetailsProperty>
}

object MeetPlannerApi {
    val retrofitService : MeetPlannerApiService by lazy {
        retrofit.create(MeetPlannerApiService::class.java)
    }
}
