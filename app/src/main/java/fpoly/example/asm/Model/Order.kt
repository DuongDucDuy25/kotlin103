import com.google.gson.annotations.SerializedName
import fpoly.example.asm.Model.Product
import java.io.Serializable
import java.math.BigDecimal

data class ShippingAddress(
    @SerializedName("address") val address: String,
    @SerializedName("phoneNumber") val phoneNumber: String
) : Serializable


// Model cho đơn hàng
data class Order(
    @SerializedName("_id") val id: String,
    @SerializedName("items") val items: List<CartItem>,
    @SerializedName("totalAmount") val totalAmount: BigDecimal,
    @SerializedName("shippingAddress") val shippingAddress: ShippingAddress,
    @SerializedName("status") val status: String,
) : Serializable
