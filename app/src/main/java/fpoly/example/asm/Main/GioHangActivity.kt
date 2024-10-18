package fpoly.example.asm.Main

import CartItem
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import fpoly.example.asm.Interface.API_PRODUCT
import fpoly.example.asm.Main.ui.theme.ASMTheme
import fpoly.example.asm.Model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GioHangActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Scaffold(
//                bottomBar = { BottomNavigationBar() }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    GioHangScreen(navController)
                }
            }
        }
    }
}

@Composable
fun GioHangScreen(navController: NavHostController) {
    var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
    var message by remember { mutableStateOf("") }
    var selectedItems by remember { mutableStateOf(mutableSetOf<CartItem>()) }
    var totalPrice by remember { mutableStateOf(0.0) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)


        if (token != null) {
            fetchCartItems(token) { items, errorMessage ->
                cartItems = items
                message = errorMessage ?: "Giỏ hàng của bạn đang trống."
            }
        } else {
            message = "Bạn chưa đăng nhập."
        }
    }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Giỏ hàng của bạn",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (cartItems.isNotEmpty()) {
                        cartItems.forEach { cartItem ->
                            var isChecked by remember { mutableStateOf(false) }
                            FoodItemCard(
                                cartItem = cartItem,
                                isChecked = isChecked,
                                onCheckedChange = { checked ->
                                    isChecked = checked
                                    if (checked) {
                                        selectedItems.add(cartItem)
                                        totalPrice += cartItem.product.price.toDouble() * cartItem.quantity
                                    } else {
                                        selectedItems.remove(cartItem)
                                        totalPrice -= cartItem.product.price.toDouble() * cartItem.quantity
                                    }
                                }
                            )
                        }
                    } else {
                        Text(message, Modifier.padding(16.dp))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tổng giá: ${totalPrice} VND",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFF6347)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val gson = Gson()
                            val selectedItemsJson = gson.toJson(selectedItems)

                            navController.navigate("payment/${Uri.encode(selectedItemsJson)}/${Uri.encode(totalPrice.toString())}") {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo("cart") { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6347)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Thanh toán", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    )
}

private fun fetchCartItems(token: String, onResult: (List<CartItem>, String?) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(API_PRODUCT::class.java)

    service.getCart("Bearer $token").enqueue(object : Callback<List<CartItem>> {
        override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
            if (response.isSuccessful) {
                val cartItems = response.body() ?: emptyList()
                onResult(cartItems, null)

                cartItems.forEach { item ->
                    Log.d("CartItem", "Sản phẩm ID: ${item.product.productId}, Số lượng: ${item.quantity}")
                }
            } else {
                val errorMessage = "Lỗi khi lấy giỏ hàng: ${response.code()} - ${response.message()}"
                Log.e("API_GioHang", errorMessage)
                onResult(emptyList(), errorMessage)
            }
        }

        override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
            val errorMessage = "Không thể lấy giỏ hàng: ${t.message}"
            Log.e("API_GioHang", errorMessage)
            onResult(emptyList(), errorMessage)
        }
    })
}

@Composable
fun FoodItemCard(
    cartItem: CartItem,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var quantity by remember { mutableStateOf(cartItem.quantity) }
    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf(0.0) }
    var productImage by remember { mutableStateOf("") }

    LaunchedEffect(cartItem.product.productId) {
        fetchProductDetails(cartItem.product.productId) { product ->
            productName = product.name
            productPrice = product.price.toDouble()
            productImage = product.image_url
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = productImage,
                    contentDescription = productName,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = productName, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "$productPrice VND",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFFFF6347),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(it) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = quantity.toString(), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
private fun fetchProductDetails(productId: String, onResult: (Product) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(API_PRODUCT::class.java)
    service.getProductID(productId).enqueue(object : Callback<Product> {
        override fun onResponse(call: Call<Product>, response: Response<Product>) {
            if (response.isSuccessful) {
                response.body()?.let { onResult(it) }
            } else {
                Log.e("API_ChiTietSanPham", "Lỗi khi lấy chi tiết sản phẩm: ${response.code()} - ${response.message()}")
            }
        }

        override fun onFailure(call: Call<Product>, t: Throwable) {
            Log.e("API_ChiTietSanPham", "Không thể lấy chi tiết sản phẩm: ${t.message}")
        }
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewGioHang() {
//    GioHangScreen()
}
