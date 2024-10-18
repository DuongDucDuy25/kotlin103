package fpoly.example.asm.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Main.DanhSachSanPham
import fpoly.example.asm.Model.User
import fpoly.example.asm.R
import fpoly.example.asm.Service.API_CLIENT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DangNhap : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DangNhapScreen()
        }
    }
}

@Composable
fun DangNhapScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF2F2F2)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                drawCircle(
                    color = Color(0xFFFFA07A),
                    radius = 150f,
                    center = Offset(x = -10f, y = 50f)
                )
                drawCircle(
                    color = Color(0xFFFF6347),
                    radius = 80f,
                    center = Offset(x = 90f, y = -100f)
                )
                drawCircle(
                    color = Color(0xFFEF8B78),
                    radius = 300f,
                    center = Offset(x = 1000f, y = 100f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Đăng Nhập",
                    style = TextStyle(fontSize = 28.sp, color = Color.Black),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Trường E-mail
                Column {
                    Text(
                        text = "Email",
                        fontSize = 16.sp,
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                // Trường mật khẩu
                Column {
                    Text(
                        text = "Mật khẩu",
                        fontSize = 16.sp,
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Mật khẩu") },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                // Biểu tượng hiện/ẩn mật khẩu
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                // Nút Đăng nhập
                Text(
                    text = "Quên mật khẩu?",
                    color = Color(0xFFFF6F61),
                    modifier = Modifier.clickable { /* Chuyển đến màn hình đăng nhập */ }
                )
                Spacer(modifier = Modifier.height(24.dp))


                Button(
                    onClick = { loginUser(email,password,context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F61))
                ) {
                    Text(text = "Đăng Nhập", color = Color.White, fontSize = (20.sp))
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Bạn đã có tài khoản?", color = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Đăng ký",
                        color = Color(0xFFFF6F61),
                        modifier = Modifier.clickable {
                            val intent = Intent(context, DangKy::class.java)
                            context.startActivity(intent)
                        }

                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* Xử lý đăng nhập Facebook */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .shadow(2.dp, RoundedCornerShape(25.dp)) // Thêm hiệu ứng đổ bóng
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.fb), // Thay bằng ID hình ảnh Facebook của bạn
                                contentDescription = "Facebook",
                                modifier = Modifier.size(24.dp) // Kích thước của hình ảnh
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Khoảng cách giữa hình ảnh và chữ
                            Text(text = "FACEBOOK", color = Color.Black)
                        }
                    }

                    Button(
                        onClick = {  },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .shadow(2.dp, RoundedCornerShape(25.dp)) // Thêm hiệu ứng đổ bóng
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google), // Thay bằng ID hình ảnh Google của bạn
                                contentDescription = "Google",
                                modifier = Modifier.size(24.dp) // Kích thước của hình ảnh
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Khoảng cách giữa hình ảnh và chữ
                            Text(text = "GOOGLE", color = Color.Black)


                        }
                    }
                }
            }
        }
    }
}



fun loginUser(email: String, password: String, context: android.content.Context) {
    Log.d("LOGIN_DEBUG", "Email: $email, Password: $password")

    if (email.isBlank() || password.isBlank()) {
        Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show()
        return
    }

    val user = User(userID = "", name = "", email = email, password = password)

    API_CLIENT.apiService.loginUser(user).enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            Log.d("LOGIN_DEBUG", "API Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    Log.d("LOGIN_DEBUG", "Login successful: $userResponse")

                    // Kiểm tra và log token từ API
                    if (userResponse.token != null) {
                        Log.d("LOGIN_DEBUG", "Token from API: ${userResponse.token}")
                    } else {
                        Log.d("LOGIN_DEBUG", "Token is null")
                    }

                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show()


                    // Lưu token vào SharedPreferences
                    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("auth_token", userResponse.token) // Giả sử userResponse có token
                    editor.putString("userId",userResponse.userID)
                    editor.apply()


                    // Chuyển sang màn hình tiếp theo
                    val intent = Intent(context, DanhSachSanPham::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                } else {
                    Log.d("LOGIN_DEBUG", "Invalid user data from API")
                    Toast.makeText(context, "Dữ liệu người dùng không hợp lệ", Toast.LENGTH_LONG).show()
                }
            } else {
                Log.e("LOGIN_DEBUG", "Error code: ${response.code()}")
                when (response.code()) {
                    401 -> Toast.makeText(context, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show()
                    500 -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(context, "Lỗi không xác định", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Log.e("LOGIN_DEBUG", "Failure: ${t.message}")
            Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}




@Preview(showSystemUi = true)
@Composable
fun GreetingPreview7() {
    DangNhapScreen()
}
