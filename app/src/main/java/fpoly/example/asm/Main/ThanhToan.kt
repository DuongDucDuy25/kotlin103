package fpoly.example.asm.Main

import CartItem
import Order
import ShippingAddress
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import fpoly.example.asm.Interface.API_PRODUCT
import fpoly.example.asm.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
class ThanhToan : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "payment/{selectedItems}/{totalPrice}"
            ) {
                composable(
                    "payment/{selectedItems}/{totalPrice}",
                    arguments = listOf(
                        navArgument("selectedItems") { type = NavType.StringType },
                        navArgument("totalPrice") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val selectedItems = backStackEntry.arguments?.getString("selectedItems") ?: ""
                    val totalPrice = backStackEntry.arguments?.getDouble("totalPrice") ?: 0.0
                    ThanhToanScreen(selectedItems, totalPrice)
                }
            }
        }
    }
}
@Composable
fun ThanhToanScreen(selectedItemsJson: String, totalPrice: Double) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val token = getToken(context)
    var message by remember { mutableStateOf("") }
    var items by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var selectedPaymentMethod by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("Dương Đức Duy") }
    var userPhone by remember { mutableStateOf("0865289908") }
    var userAddress by remember { mutableStateOf("Thắng Trí, Minh Trí, Sóc Sơn, Hà Nội") }
//    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPreferences.getString("userId", null)

    // Thêm trạng thái cho việc hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedItemsJson) {
        try {
            items = Gson().fromJson(selectedItemsJson, Array<CartItem>::class.java).toList()
        } catch (e: Exception) {
            Log.e("JSON Error", "Unable to parse JSON: ${e.message}")
            message = "Có lỗi xảy ra khi xử lý dữ liệu."
        }
    }

    Box(modifier = Modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(232, 232, 232, 255)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề của màn hình thanh toán
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Thanh Toán",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            // Danh sách sản phẩm
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    if (items.isNotEmpty()) {
                        items.forEach { item ->
                            val price = BigDecimal(item.product.price)
                            val quantity = BigDecimal(item.quantity)
                            val totalProductPrice = price.multiply(quantity)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.product.name} x${item.quantity}",
                                    modifier = Modifier.weight(1f),
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${totalProductPrice} VND",
                                    fontSize = 16.sp,
                                    color = Color.Red
                                )
                            }
                        }

                        Text(
                            text = "Tổng giá: ${totalPrice} VND",
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 16.dp, bottom = 10.dp),
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                    } else if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            color = Color.Red,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Thông tin người dùng
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Hiển thị tên
                    Text(
                        text = "Tên người dùng : $userName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    // Hiển thị SĐT
                    Text(
                        text = "Số điện thoaại : $userPhone",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    // Hiển thị địa chỉ
                    Text(
                        text = "Đia chỉ : $userAddress",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Phương thức thanh toán
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Phương thức thanh toán:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPaymentMethod == "PayPal",
                            onCheckedChange = { selectedPaymentMethod = "PayPal" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Text hiển thị "Thanh toán bằng PayPal"
                        Text("Thanh toán bằng PayPal")

                        Spacer(modifier = Modifier.width(8.dp))

                        // Thêm hình ảnh PayPal sau Text
                        Image(
                            painter = painterResource(id = R.drawable.paypal),
                            contentDescription = "PayPal logo",
                            modifier = Modifier.size(20.dp) // Điều chỉnh kích thước của hình ảnh
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPaymentMethod == "Chuyển khoản",
                            onCheckedChange = { selectedPaymentMethod = "Chuyển khoản" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Thanh toán chuyển khoản")
                        Spacer(modifier = Modifier.width(8.dp))

                        // Thêm hình ảnh PayPal sau Text
                        Image(
                            painter = painterResource(id = R.drawable.creditcard),
                            contentDescription = "PayPal logo",
                            modifier = Modifier.size(20.dp) // Điều chỉnh kích thước của hình ảnh
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPaymentMethod == "Tiền mặt",
                            onCheckedChange = { selectedPaymentMethod = "Tiền mặt" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Thanh toán bằng tiền mặt")
                        Spacer(modifier = Modifier.width(8.dp))

                        // Thêm hình ảnh PayPal sau Text
                        Image(
                            painter = painterResource(id = R.drawable.money),
                            contentDescription = "PayPal logo",
                            modifier = Modifier.size(20.dp) // Điều chỉnh kích thước của hình ảnh
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Nút xác nhận thanh toán
            Button(
                onClick = {
                    // Hiển thị dialog xác nhận
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6347)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Xác nhận thanh toán", color = Color.White, fontSize = 16.sp)
            }

            // Dialog xác nhận
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Xác nhận thanh toán") },
                    text = { Text("Bạn có chắc chắn muốn thanh toán không?") },
                    confirmButton = {
                        // Thay thế TextButton bằng Button với màu tùy chỉnh
                        Button(
                            onClick = {


                                        createOrder( items, totalPrice)
                                        Log.d("NutBam", "nút bấm đã click")
                                        Log.d("API_REQUEST", "Sending order to server: $items")
                                        Log.d("API_Price", "Authorization Token: Bearer $totalPrice")


                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC4A4))
                        ) {
                            Text("Thanh toán", color = Color.White)
                        }
                    },
                    dismissButton = {
                        // Thay thế TextButton bằng Button với màu tùy chỉnh
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB92D0))
                        ) {
                            Text("Mua tiếp", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}


private fun getToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("auth_token", null)
}

fun createOrder( items: List<CartItem>, totalPrice: Double) {
    // Khởi tạo địa chỉ giao hàng
    val shippingAddress = ShippingAddress("Hà Nội", "0865289908") // Có thể lấy từ input của người dùng
    val orderItems = items.map { cartItem ->
        CartItem(cartItem.id,product = cartItem.product, quantity = cartItem.quantity)
    }

    // Tạo đối tượng Order
    val order = Order(
        id = "", // ID sẽ được server sinh ra
        items = orderItems,
        totalAmount = BigDecimal(totalPrice),
        shippingAddress = shippingAddress,
        status = "Chờ Xác Nhận"
    )

    // Khởi tạo Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000") // Đảm bảo URL đúng cho môi trường phát triển
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(API_PRODUCT::class.java)

    // Log thông tin về API request
    Log.d("YEU_CAU_API", "Đang gửi đơn hàng đến server: $order")

    // Gọi API tạo đơn hàng
    val call = service.createOrder(order)
    call.enqueue(object : Callback<Order> {
        override fun onResponse(call: Call<Order>, response: Response<Order>) {
            // Log mã phản hồi từ server
            Log.d("PHAN_HOI_DON_HANG", "Mã phản hồi: ${response.code()}")

            if (response.isSuccessful) {
                // Xử lý thành công
                Log.d("THANH_CONG_DON_HANG", "Đơn hàng đã được tạo: ${response.body()}")
                // Có thể hiển thị thông báo thành công hoặc điều hướng đến màn hình khác
            } else {
                // Xử lý lỗi
                Log.e("LOI_DON_HANG", "Lỗi tạo đơn hàng: ${response.errorBody()?.string()}")
                Log.e("LOI_DON_HANG", "Mã phản hồi: ${response.code()}")
                Log.e("LOI_DON_HANG", "Thông báo phản hồi: ${response.message()}")

                // Thêm chi tiết để debug
                Log.e("LOI_DON_HANG", "Nội dung yêu cầu: $order")
            }
        }

        override fun onFailure(call: Call<Order>, t: Throwable) {
            Log.e("THAT_BAI_DON_HANG", "Không thể tạo đơn hàng: ${t.message}")
            Log.e("THAT_BAI_DON_HANG", "Nguyên nhân lỗi: ${t.cause}")
        }
    })
}





@Preview(showSystemUi = true)
@Composable
fun GreetingPreview10() {
    ThanhToanScreen(
        selectedItemsJson = """
            [{"product": {"name": "Sản phẩm 1", "price": "100000"}, "quantity": 2},
            {"product": {"name": "Sản phẩm 2", "price": "200000"}, "quantity": 1}]
        """.trimIndent(),
        totalPrice = 400000.0
    )
}
