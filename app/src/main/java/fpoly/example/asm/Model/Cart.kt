import com.google.gson.annotations.SerializedName
import fpoly.example.asm.Model.Product
import java.io.Serializable

// Data class cho từng sản phẩm trong giỏ hàng với số lượng
data class AddtoCartRequest(
    val productId: String,
    val quantity: Int
) : Serializable

data class ApiResponse(
    val msg: String
)

// Data class cho giỏ hàng
data class CartItem(
    @SerializedName("_id") val id: String,
    val product: Product,
    val quantity: Int
) : Serializable
