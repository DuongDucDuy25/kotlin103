package fpoly.example.asm.Main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Auth.ui.theme.ASMTheme
import fpoly.example.asm.R

class DemoCard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ASMTheme { // Sử dụng theme của bạn
                // Gọi hàm CardWithMultip để hiển thị
                CardWithMultip()
            }
        }
    }
}

@Composable
fun CardWithMultip() {
    val paddingModifier = Modifier.padding(10.dp)
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.padding(10.dp),

    ) {
        Column(
            modifier = paddingModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.anhkhuonmat), // Thay bằng ID hình ảnh của bạn
                contentDescription = "SanPham",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape) // Hình ảnh tròn
                    .border(1.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Duong Duc Duy",
                color = Color.Blue,
                fontSize = 18.sp // Thêm kích thước chữ nếu cần
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "31000",
                color = Color.Red,
                fontSize = 16.sp // Thêm kích thước chữ nếu cần
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(250, 74, 12),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Mua",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview8() {
    ASMTheme { // Sử dụng theme của bạn
        CardWithMultip()
    }
}
