package com.example.meetplanner.authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetplanner.network.MeetPlannerApi
import com.example.meetplanner.network.Tokens
import com.example.meetplanner.network.TokensProperty
import com.example.meetplanner.network.UserProperty
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    enum class AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED  ,
        INVALID_AUTHENTICATION
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val authenticationState = MutableLiveData<AuthenticationState>()

    init {
        Log.v("HERE", Tokens.access.toString())
        if (Tokens.access != "") {
            Log.v("AUTHENTICATED", "yes")
            authenticationState.value = AuthenticationState.AUTHENTICATED

        } else {
            Log.v("Token", Tokens.access.toString())
            authenticationState.value = AuthenticationState.UNAUTHENTICATED
        }
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun login(email: String, password: String) {
        val userProperty = UserProperty(email=email, password=password)
        MeetPlannerApi.retrofitService.login(userProperty).enqueue(object: Callback<TokensProperty> {
            override fun onFailure(call: Call<TokensProperty>?, t: Throwable?) {
                Log.v("Failure", t?.message)
            }
            override fun onResponse(call: Call<TokensProperty>?, response: Response<TokensProperty>?) {
                when (response?.code()) {
                    200 -> handleSuccessfulLogin(requireNotNull(response.body()))
                    401 -> {
                        val error = JSONObject(response?.errorBody()?.string())
                        _errorMessage.value = error.getString("detail")
                        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                    }
                    500 -> {
                        _errorMessage.value = "Internal server error"
                        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                    }
                }
            }
        })
    }

    fun register(email: String, password: String) {
        Log.v("Pass", password)
        val userProperty = UserProperty(email=email, password=password)
        MeetPlannerApi.retrofitService.signUp(userProperty).enqueue(object: Callback<UserProperty> {
            override fun onFailure(call: Call<UserProperty>?, t: Throwable?) {
                Log.v("Failure", t?.message)
            }
            override fun onResponse(call: Call<UserProperty>?, response: Response<UserProperty>?) {
                when (response?.code()) {
                    201 -> login(email, password)
                    400 -> {
                        val error = JSONObject(response?.errorBody()?.string())
                        _errorMessage.value = error.getJSONArray("email")[0].toString()
                        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                    }
                    500 -> {
                        _errorMessage.value = "Internal server error"
                        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                    }
                }
            }
        })
    }

    fun logout() {
        Log.v("Logout", "works")
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
        Tokens.clear()
    }

    private fun handleSuccessfulLogin(tokensProperty: TokensProperty) {
        Tokens.access = tokensProperty.access
        Tokens.refresh = tokensProperty.refresh
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }

}
