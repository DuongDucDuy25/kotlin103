package fpoly.example.asm.Auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.R

class ManHinhChao2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting3()
        }
    }
}

@Composable
fun Greeting3() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF4B3A)),

    ) {
        // Text ở trên cùng màn hình
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp), // Để tạo khoảng cách từ đỉnh màn hình
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Food ", // Dòng text trên đầu
                color = Color.White,
                fontSize = 80.sp, // Cỡ chữ lớn
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ("for Everyone"),
                color = Color.White,
                fontSize = 55.sp, // Cỡ chữ lớn
                fontWeight = FontWeight.Bold
            )
        }

        // Ảnh ở dưới cùng
        Image(
            painter = painterResource(id = R.drawable.animationanh), // Đảm bảo ảnh có trong res/drawable
            contentDescription = "Fast Food Logo",
            modifier = Modifier
                .fillMaxWidth() // Chiếm toàn bộ chiều rộng màn hình
                .align(Alignment.BottomCenter)
                .size(400.dp) // Căn ảnh xuống cuối màn hình
        )

        // Nút bấm
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Căn nút ở đáy
                .padding(bottom = 50.dp) // Tạo khoảng cách 50.dp so với đáy màn hình
                .padding(horizontal = 10.dp) // Padding ngang để nút không sát lề
        ) {
            Button(
                onClick = {
                    val intent = Intent(context,DangNhap::class.java)
                    context.startActivity(intent)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Bắt đầu",
                    color = Color(0xFFFF6347), // Màu chữ cam (Tomato)
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview3() {
    Greeting3()
}
