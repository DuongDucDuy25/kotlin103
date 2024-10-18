package fpoly.example.asm.Main

import ShippingAddress
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Main.ui.theme.ASMTheme
import fpoly.example.asm.Model.User
import fpoly.example.asm.R

class ThongTinNguoiDung : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ASMTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    ThongTinNguoiDungScreen(padding)
                }
            }
        }
    }
}

@Composable
fun ThongTinNguoiDungScreen(padding: PaddingValues) {
    // Sample user data
    val name = "Dương Đức Duy"
    val email = "duy@gmail.com"
    val address = "Thắng trí, Minh Trí, Sóc Sơn, Hà Nội"
    val phone = "0865289908"

    // Get the current context
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // User profile UI
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.anhkhuonmat),
                        contentDescription = "Hình Ảnh",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Xin Chào Ruy!", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(30.dp))

                // User info card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Họ và Tên: $name",
                            fontSize = 16.sp,
                            color = Color(121, 121, 121, 255),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Email: $email",
                            fontSize = 16.sp,
                            color = Color(121, 121, 121, 255),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Nơi Ở: $address",
                            fontSize = 16.sp,
                            color = Color(121, 121, 121, 255),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Số điện thoại: $phone",
                            fontSize = 16.sp,
                            color = Color(121, 121, 121, 255),
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = {
                                // Use LocalContext to get context and create an Intent
                                val intent = Intent(context, SuaThongTinTaiKhoan::class.java).apply {
                                    putExtra("name", name)
                                    putExtra("email", email)
                                    putExtra("address", address)
                                    putExtra("phone", phone)
                                }
                                context.startActivity(intent)
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
    }
}

@Preview(showSystemUi = true)
@Composable
fun ThongTinNguoiDungPreview() {
    lateinit var user: User
    lateinit var shippingAddress: ShippingAddress
    ThongTinNguoiDungScreen(padding = PaddingValues(16.dp))
}
