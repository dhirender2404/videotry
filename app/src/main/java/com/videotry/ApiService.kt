package com.videotry

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("api/notes/create")
    fun uploadVideo(
        @Header("Authorization") authToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("classId") classid : RequestBody,
        @Part("sectionId") sectionId : RequestBody,
        @Part("subjectId") subjectId : RequestBody,
        @Part("chapterName") chapterName : RequestBody,
        @Part("topicName") topicName : RequestBody,
        @Part("title") title : RequestBody,
    ): Call<ApiResponse>
}