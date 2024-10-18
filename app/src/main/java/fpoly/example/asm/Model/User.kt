package fpoly.example.asm.Model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val userID: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("token") val token: String? = null

)
