package fpoly.example.asm.Interface

import fpoly.example.asm.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface API_USER {
    @POST("/users/reg")
    fun registerUser(@Body user: User): Call<User>

    @POST("/users/login")
    fun loginUser(@Body user: User): Call<User>
}
