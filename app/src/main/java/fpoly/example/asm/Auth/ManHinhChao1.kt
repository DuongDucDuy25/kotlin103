package fpoly.example.asm.Auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Auth.ui.theme.ASMTheme
import fpoly.example.asm.R
import kotlinx.coroutines.delay

class ManHinhChao1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ASMTheme {
                Greeting2()
            }
        }
    }
}

@Composable
fun Greeting2() {
    val context = LocalContext.current

    // Sử dụng LaunchedEffect để thực hiện hành động sau khi Composable được hiển thị
    LaunchedEffect(Unit) {
        delay(3000L) // Đợi 3 giây
        // Chuyển sang màn hình ManHinhChao2
        val intent = Intent(context, ManHinhChao2::class.java)
        context.startActivity(intent)
    }

    // Giao diện chính của màn hình chào
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(254, 114, 76))
            .fillMaxHeight()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logofastfood), // Đảm bảo bạn đã có tệp này trong res/drawable
            contentDescription = "Fast Food Logo",
            modifier = Modifier.size(300.dp) // Kích thước hình ảnh (có thể điều chỉnh)
        )
        Text(
            text = ("Cum Tứm Đim"),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview2() {
    ASMTheme {
        Greeting2()
    }
}
