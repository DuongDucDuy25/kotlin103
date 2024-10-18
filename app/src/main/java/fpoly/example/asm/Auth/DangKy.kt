package fpoly.example.asm.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import fpoly.example.asm.Auth.ui.theme.ASMTheme
import fpoly.example.asm.Main.DanhSachSanPham
import fpoly.example.asm.Model.User
import fpoly.example.asm.R
import fpoly.example.asm.Service.API_CLIENT

class DangKy : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ASMTheme {
                DangKyScreen()
            }
        }
    }
}


@Composable
fun DangKyScreen() {
    var fullName by remember { mutableStateOf("") }
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
                    text = "Đăng Ký",
                    style = TextStyle(fontSize = 28.sp, color = Color.Black),
                    modifier = Modifier.padding(bottom = 32.dp)
                )


                Column {
                    Text(
                        text = ("Họ và tên"),
                        fontSize = (16.sp),
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold

                    )
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text(text = "Họ tên") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                // Trường E-mail
                Column {
                    Text(
                        text = ("Email"),
                        fontSize = (16.sp),
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold

                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "E-mail") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                Column {
                    Text(
                        text = ("Mật khẩu"),
                        fontSize = (16.sp),
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

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }


                Button(
                    onClick = { SignUpUser(email,password,fullName,context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F61))
                ) {
                    Text(text = "Đăng Ký", color = Color.White, fontSize = (20.sp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Bạn đã có tài khoản?", color = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Đăng nhập",
                        color = Color(0xFFFF6F61),
                        modifier = Modifier.clickable { /* Chuyển đến màn hình đăng nhập */ }
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

fun SignUpUser(email : String , password : String , name : String, context: android.content.Context){
    Log.d("SignUp", "email : $email,password : $password, name : $name")
    if (email.isBlank()||password.isBlank()||name.isBlank()){
        Toast.makeText(context,"Vui lòng điền đầy đủ thông tin ", Toast.LENGTH_LONG).show()
        return
    }
    val user = User(
        userID = "",
        name = name,
        email = email,
        password = password
    )
    val call = API_CLIENT.apiService.registerUser(user)

    call.enqueue(object : retrofit2.Callback<User> {
        override fun onResponse(call: retrofit2.Call<User>, response: retrofit2.Response<User>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_LONG).show()
                val intent = Intent(context, DangNhap::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Đăng ký thất bại: ${response.message()}", Toast.LENGTH_LONG).show()
                Log.d("SignUp", "Đăng ký thất bại: ${response.message()}")

            }
        }

        override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
            Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview4() {
    DangKyScreen()
}
