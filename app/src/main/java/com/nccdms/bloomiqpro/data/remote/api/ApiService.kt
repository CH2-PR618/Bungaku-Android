package com.nccdms.bloomiqpro.data.remote.api

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest,
        @Header("Cookies") sessionId:String?
    ): Response<RegisterResponse>

    @GET("user/me")
    suspend fun getUserProfile(
        @Header("Cookie") sessionId: String?
    ): Response<ProfileResponse>

    @GET("flowers")
    suspend fun getFlowers(
        @Header("Cookie") sessionId: String?
    ): Response<List<FlowerResponseItem>>

    @GET("flower/{flowerName}")
    suspend fun getFlowerDetails(
        @Path("flowerName") flowerName: String,
        @Header("Cookie") sessionId: String?
    ): Response<FlowerDetailResponse>

    @PUT("user/update")
    suspend fun updateName(
        @Body request: UpdateNameRequest,
        @Header("Cookie") sessionId: String?
    ): Response<UpdateResponse>

    @PUT("user/update")
    suspend fun updateEmail(
        @Body request: UpdateEmailRequest,
        @Header("Cookie") sessionId: String?
    ): Response<UpdateResponse>

    @PUT("user/update")
    suspend fun updatePassword(
        @Body request: UpdatePasswordRequest,
        @Header("Cookie") sessionId: String?
    ): Response<UpdateResponse>

    @DELETE("logout")
    suspend fun logout():Response<LogoutResponse>

}