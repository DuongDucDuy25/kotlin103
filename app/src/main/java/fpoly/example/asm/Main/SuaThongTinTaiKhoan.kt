package fpoly.example.asm.Main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Main.ui.theme.ASMTheme
import fpoly.example.asm.R

class SuaThongTinTaiKhoan : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val fullName = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val phoneNumber = intent.getStringExtra("phone") ?: ""
        val diaChi = intent.getStringExtra("address") ?: ""

        setContent {
            Scaffold() { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    SuaThongTinScreen(fullName, email, phoneNumber, diaChi)
                }
            }
        }
    }
}

@Composable
fun SuaThongTinScreen(
    initialFullName: String,
    initialEmail: String,
    initialPhoneNumber: String,
    initialAddress: String
) {
    var fullName by remember { mutableStateOf(initialFullName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phoneNumber by remember { mutableStateOf(initialPhoneNumber) }
    var diachi by remember { mutableStateOf(initialAddress) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF2F2F2)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Ghi Chu",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(1f)) // Khoảng trống giữa icon và hình ảnh

                    Image(
                        painter = painterResource(id = R.drawable.anhkhuonmat), // Thay bằng ID hình ảnh của bạn
                        contentDescription = "SanPham",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape) // Hình ảnh tròn
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.weight(1f)) // Khoảng trống giữa hình ảnh và icon bên phải

                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Ghi Chu",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Xin Chào Ruy!",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Trường Họ và Tên
                Column {
                    Text(
                        text = "Họ và Tên",
                        fontSize = 16.sp,
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            Log.d("SuaThongTin", "Tên đã được sửa thành: $fullName")
                        },
                        label = { Text(text = "Họ và Tên") },
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
                        text = "Email",
                        fontSize = 16.sp,
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            Log.d("SuaThongTin", "Email đã được sửa thành: $email")
                        },
                        label = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                // Trường Nơi Ở
                Column {
                    Text(
                        text = "Nơi Ở",
                        fontSize = 16.sp,
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = diachi,
                        onValueChange = {
                            diachi = it
                            Log.d("SuaThongTin", "Địa chỉ đã được sửa thành: $diachi")
                        },
                        label = { Text(text = "Nơi Ở") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                // Trường Số điện thoại
                Column {
                    Text(
                        text = "Số điện thoại",
                        fontSize = 16.sp,
                        color = Color(121, 121, 121, 255),
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            Log.d("SuaThongTin", "Số điện thoại đã được sửa thành: $phoneNumber")
                        },
                        label = { Text(text = "Số điện thoại") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }

                Button(
                    onClick = {
                        Log.d("SuaThongTin", "Thông tin đã được sửa: Tên: $fullName, Email: $email, Địa chỉ: $diachi, Số điện thoại: $phoneNumber")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7F58))
                ) {
                    Text(text = "Sửa thông tin", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}
