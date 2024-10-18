package fpoly.example.asm.Main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Main.ui.theme.ASMTheme

class LichSuDonHang : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ASMTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
//                        bottomBar = { BottomNavigationBar() }
                )
                { innerPadding ->

                    OrderHistoryScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class OrderHistoryItem(
    val status: String,
    val date: String,
    val time: String,
    val itemCount: String,
    val totalPrice: String,
    val isCancelled: Boolean = false
)

@Composable
fun OrderHistoryScreen(modifier: Modifier = Modifier) {
    val orderList = listOf(
        OrderHistoryItem("Đơn hàng đã chấp nhận", "10/03/2023", "9:20", "3 món", "98k"),
        OrderHistoryItem("Đơn hàng đã bị hủy", "10/03/2023", "9:20", "3 món", "98k", isCancelled = true),
        OrderHistoryItem("Đơn hàng đã được giao", "10/03/2023", "9:20", "3 món", "98k"),
        // Thêm nhiều đơn hàng nếu cần
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Màu nền ngoài cùng
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Lịch sử",
            style = LocalTextStyle.current.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            color = Color.DarkGray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // LazyColumn to display the list of orders
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()

                .padding(8.dp)
        ) {
            items(orderList) { order ->
                OrderItem(order = order)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun OrderItem(order: OrderHistoryItem) {
    val statusColor = if (order.isCancelled) Color.Red else Color.White
    val statusTextStyle = LocalTextStyle.current.copy(
        color = statusColor,
        fontWeight = FontWeight.Bold
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(175, 175, 175, 255))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = order.status, style = statusTextStyle)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${order.date}  ${order.time}", color = Color.White)
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(text = order.itemCount, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = order.totalPrice, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderHistoryScreenPreview() {
    ASMTheme {
        OrderHistoryScreen()
    }
}
