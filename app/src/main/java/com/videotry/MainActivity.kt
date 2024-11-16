package com.videotry
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.videotry.RetrofitClient.apiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var selectedVideoUri: Uri
    private lateinit var submitbt: Button
    private lateinit var txt8:TextView
    private lateinit var videoFile : File
    private lateinit var videoRequestBody: RequestBody
    private lateinit var file: MultipartBody.Part
    private val selectVideoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedVideoUri = it
            submitbt.isEnabled = true
            videoFile = File(getRealPathFromURI(selectedVideoUri))
            var filename = videoFile.name
            txt8.text = "Video selected : \n$filename"
            videoRequestBody = RequestBody.create("video/*".toMediaType(), videoFile)
            file = MultipartBody.Part.createFormData("file", videoFile.name, videoRequestBody)


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bt1: Button = findViewById(R.id.bt1)
        submitbt = findViewById(R.id.submitbt)
        txt8 = findViewById(R.id.txt8)


        bt1.setOnClickListener {
            selectVideoLauncher.launch("video/*")

        }

        submitbt.setOnClickListener {
            uploadVideo()
        }
    }

    private fun uploadVideo() {
        val classId: String = "644b88c8937584a14c908803"
        val sectionId: String="644b8907937584a14c90885c"
        val subjectId: String = "644b8b6c937584a14c9089be"
        val chapterName:String = "abc"
        val topicName:String = "abcdef"
        val title: String = "title for testing 1"
        val description:String = "description for testing"

        val authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI4MTkiLCJpYXQiOjE3MzEzMDUyNzV9.cwuviwYCPeTRMADVvIL3treUSeCqVOwdAvsZOTBQcGk"


        apiService.uploadVideo(authToken, file, description.toRequestBody(),classId.toRequestBody() ,sectionId.toRequestBody(),subjectId.toRequestBody(),chapterName.toRequestBody(),topicName.toRequestBody(),title.toRequestBody() ).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Upload Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath ?: ""
    }
}