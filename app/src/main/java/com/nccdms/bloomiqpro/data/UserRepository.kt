package com.nccdms.bloomiqpro.data

import com.nccdms.bloomiqpro.data.local.pref.UserPreferences
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.api.ApiService
import com.nccdms.bloomiqpro.data.remote.request.LoginRequest
import com.nccdms.bloomiqpro.data.remote.request.RegisterRequest
import com.nccdms.bloomiqpro.data.remote.request.UpdateEmailRequest
import com.nccdms.bloomiqpro.data.remote.request.UpdateNameRequest
import com.nccdms.bloomiqpro.data.remote.request.UpdatePasswordRequest
import com.nccdms.bloomiqpro.data.remote.response.FlowerDetailResponse
import com.nccdms.bloomiqpro.data.remote.response.FlowerResponseItem
import com.nccdms.bloomiqpro.data.remote.response.LoginResponse
import com.nccdms.bloomiqpro.data.remote.response.LogoutResponse
import com.nccdms.bloomiqpro.data.remote.response.ProfileResponse
import com.nccdms.bloomiqpro.data.remote.response.RegisterResponse
import com.nccdms.bloomiqpro.data.remote.response.UpdateResponse
import okhttp3.Headers
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService
){
    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(password, email))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.let {
                    // Save session ID from response
                    saveSessionIdToUserPreferences(response.headers())
                    ApiResult.Success(it)
                } ?: ApiResult.Error("Response body is null")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            ApiResult.Error("Socket timeout error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confpassword: String,
    ): ApiResult<RegisterResponse> {
        return try {
            val sessionId = userPreferences.getSessionId()
            val response = apiService.register(RegisterRequest(
                confPassword = confpassword,
                email = email,
                password = password,
                name = name),
                sessionId)

            if (response.isSuccessful) {
                val registerResponse = response.body()
                registerResponse?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error("Response body is null")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            ApiResult.Error("Socket timeout error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun updateName(name: String): ApiResult<UpdateResponse> {
        return try {
            val sessionId = userPreferences.getSessionId()
            val response = apiService.updateName(UpdateNameRequest(name), sessionId)
            handleUpdateResponse(response)
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun updateEmail(email: String, currentPassword: String): ApiResult<UpdateResponse> {
        return try {
            val sessionId = userPreferences.getSessionId()
            val response = apiService.updateEmail(UpdateEmailRequest(email, currentPassword), sessionId)
            handleUpdateResponse(response)
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun updatePassword(newPassword: String, currentPassword: String): ApiResult<UpdateResponse> {
        return try {
            val sessionId = userPreferences.getSessionId()
            val response = apiService.updatePassword(UpdatePasswordRequest(newPassword, currentPassword), sessionId)
            handleUpdateResponse(response)
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    private fun handleUpdateResponse(response: Response<UpdateResponse>): ApiResult<UpdateResponse> {
        return if (response.isSuccessful) {
            val updateResponse = response.body()
            updateResponse?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error("Response body is null")
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            ApiResult.Error(errorMessage)
        }
    }

    suspend fun logout(): ApiResult<LogoutResponse> {
        return try {

            val response = apiService.logout()
            if (response.isSuccessful) {
                val logoutResponse = response.body()
                logoutResponse?.let {
                    userPreferences.clearSessionId()
                    ApiResult.Success(it)
                } ?: ApiResult.Error("Response body is null")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            ApiResult.Error("Socket timeout error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }
    suspend fun getUserProfile(): ApiResult<ProfileResponse> {
        return try {
            // Retrieve session ID from DataStore
            val sessionId = userPreferences.getSessionId()

            // Make API call with the session ID
            val response = apiService.getUserProfile(sessionId)

            if (response.isSuccessful) {
                val userResponse = response.body()
                userResponse?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error("Response body is null")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            ApiResult.Error("Socket timeout error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun getFlowers(): ApiResult<List<FlowerResponseItem>> {
        return try {
            val sessionId = userPreferences.getSessionId()

            // Make the API call using Retrofit
            val response = apiService.getFlowers(sessionId)

            if (response.isSuccessful) {
                // Handle successful response
                val flowerResponse = response.body()
                flowerResponse?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error("Response body is null")
            } else {
                // Handle error response
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            // Handle network errors
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            // Handle socket timeout errors
            ApiResult.Error("Socket timeout error: ${e.message}")
        } catch (e: Exception) {
            // Handle other exceptions
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun getFlowerDetails(flowerName: String): ApiResult<FlowerDetailResponse> {
        return try {
            val sessionId = userPreferences.getSessionId()
            val response = apiService.getFlowerDetails(flowerName, sessionId)
            if (response.isSuccessful) {
                // Handle successful response
                val flowerResponse = response.body()
                flowerResponse?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error("Response body is null")
            } else {
                // Handle error response
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            // Handle network errors
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            // Handle socket timeout errors
            ApiResult.Error("Socket timeout error: ${e.message}")
        } catch (e: Exception) {
            // Handle other exceptions
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    private suspend fun saveSessionIdToUserPreferences(headers: Headers) {
        val sessionId = headers["Set-Cookie"]
        sessionId?.let {
            userPreferences.saveSessionId(it)
        }
    }
}

