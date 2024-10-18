package fpoly.example.asm.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Product (
    @SerializedName("_id") val productId: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("price") val price: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
) : Serializable

