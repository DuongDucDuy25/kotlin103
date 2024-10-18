package fpoly.example.asm.Main

import AddtoCartRequest
import ApiResponse
import CartItem
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import fpoly.example.asm.Interface.API_PRODUCT
import fpoly.example.asm.Model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChiTietSanPham : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "detail/{productId}/{name}/{image_url}/{price}/{description}/{category}"
            ) {
                composable(
                    "detail/{productId}/{name}/{image_url}/{price}/{description}/{category}",
                    arguments = listOf(
                        navArgument("productId") { type = NavType.StringType },
                        navArgument("name") { type = NavType.StringType },
                        navArgument("image_url") { type = NavType.StringType },
                        navArgument("price") { type = NavType.StringType },
                        navArgument("description") { type = NavType.StringType },
                        navArgument("category") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    // Lấy các tham số từ backStackEntry
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val imageUrl = backStackEntry.arguments?.getString("image_url") ?: ""
                    val price = backStackEntry.arguments?.getString("price") ?: ""
                    val description = backStackEntry.arguments?.getString("description") ?: ""
                    val category = backStackEntry.arguments?.getString("category") ?: ""

                    // Kiểm tra và tạo đối tượng sản phẩm
                    val product = Product(
                        productId = productId,
                        name = name,
                        image_url = imageUrl,
                        price = price,
                        description = description,
                        category = category
                    )
                    Log.d("IDSanPham", "ID sản phầm : ${productId}")

                    // Gọi màn hình chi tiết sản phẩm với sản phẩm đã lấy được
                    ChiTietSanPhamScreen(product)
                }
            }
        }
    }
}

@Composable
fun ChiTietSanPhamScreen(product: Product) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val token = getToken(context)
    var message by remember { mutableStateOf("") } // Biến lưu thông báo

    Log.d("ChiTietSanPhamScreen", "ID sản phẩm: ${product.productId}")

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                // Nút quay lại (Back)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Button(
                        onClick = { /* Xử lý quay lại */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text(text = "Quay lại", fontSize = 16.sp)
                    }
                }

                // Hình ảnh sản phẩm
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFEAEAEA),
                                    Color(0xFFEEEEEE)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AsyncImage(
                        model = product.image_url,
                        contentDescription = "Hình ảnh sản phẩm",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tên sản phẩm
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Giá sản phẩm
                Text(
                    text = "Giá: ${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(0xFFFF6347),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mô tả sản phẩm
                Text(
                    text = product.description,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Loại sản phẩm
                Text(
                    text = product.category,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                // Nút thêm vào giỏ hàng
                Button(
                    onClick = {
                        Log.d("ADD_TO_CART_BUTTON", "Nút 'Thêm vào giỏ hàng' đã được nhấn") // Thêm logd tại đây
                        val cartItem = CartItem(id = product.productId, product = product, quantity = 1)
                        if (token != null) {
                            addToCart(cartItem, token) { msg ->
                                message = msg // Lưu thông báo
                                Log.d("ThemGioHang", "Thêm đã đc thực hiện")
                            }
                        } else {
                            Log.e("TOKEN_ERROR", "Không tìm thấy token")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6347),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                ) {
                    Text(text = "Thêm vào giỏ hàng", fontSize = 18.sp)
                }

                // Hiển thị thông báo
                if (message.isNotEmpty()) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

fun addToCart(cartItem: CartItem, token: String, onResult: (String) -> Unit) {
    Log.d("ADD_TO_CART", "Token: $token")
    Log.d("ADD_TO_CART", "CartItem: $cartItem")

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(API_PRODUCT::class.java)

    if (token.isNotEmpty()) {
        Log.d("tokenthemgiohang", "token : ${token}")
        val request = AddtoCartRequest(
            productId = cartItem.product.productId,
            quantity = cartItem.quantity
        )

        // Log thông tin yêu cầu
        Log.d("ADD_TO_CART_REQUEST", "Request: productID=${request.productId}, quantity=${request.quantity}")

        service.addToCart(request, "Bearer $token").enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val msg = response.body()?.msg ?: "Thêm vào giỏ hàng thành công!"
                    Log.d("ADD_TO_CART_RESPONSE", msg)
                    onResult(msg) // Gọi callback với thông báo
                } else {
                    Log.e("ADD_TO_CART_RESPONSE", "Error: ${response.code()} - ${response.message()}")
                    onResult("Lỗi: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                val message = "Lỗi: ${t.message}"
                Log.d("ADD_TO_CART_FAILURE", message)
                onResult(message) // Gọi callback với thông báo lỗi
            }
        })
    } else {
        Log.d("ADD_TO_CART", "Bạn chưa đăng nhập")
        onResult("Bạn chưa đăng nhập") // Gọi callback nếu không có token
    }
}

private fun getToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("auth_token", null)
}

@Preview(showBackground = true)
@Composable
fun PreviewChiTietSanPhamScreen() {
    ChiTietSanPhamScreen(
        Product(
            productId = "1",
            name = "Sản phẩm mẫu",
            image_url = "https://example.com/sample.png",
            price = "100.000 VNĐ",
            description = "Đây là mô tả sản phẩm mẫu.",
            category = "Danh mục mẫu"
        )
    )
}
