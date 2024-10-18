package fpoly.example.asm.Main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpoly.example.asm.Main.ui.theme.ASMTheme

class GuiDonHang : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ASMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DeliveryScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DeliveryScreen(modifier: Modifier = Modifier) {
    var selectedMethod by remember { mutableStateOf("Door delivery") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gửi đơn hàng",
            style = LocalTextStyle.current.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Address section
        Text(
            text = "địa chỉ nhận hàng",
            style = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        AddressCard(
            name = "Dương Đức Duy",
            address = "số 7 ngách 71 ngõ 50 Mễ Trì Thượng Nam Từ Liêm Hà Nội",
            phoneNumber = "0865289908"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Delivery method section
        Text(
            text = "Phương thức thanh toán.",
            style = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Delivery options
        DeliveryMethodOption(
            selectedMethod = selectedMethod,
            onMethodSelected = { selectedMethod = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Total section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tổng Giá :",
                style = LocalTextStyle.current.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "23,000",
                style = LocalTextStyle.current.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Payment button
        Button(
            onClick = { /* TODO: Handle payment action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(250, 74, 12),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Thanh toan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AddressCard(name: String, address: String, phoneNumber: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = address, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = phoneNumber, fontSize = 14.sp)
        }
    }
}

@Composable
fun DeliveryMethodOption(selectedMethod: String, onMethodSelected: (String) -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMethodSelected("Chuyen khoan") }
                .padding(vertical = 8.dp)
        ) {
            RadioButton(
                selected = selectedMethod == "Chuyen khoan",
                onClick = { onMethodSelected("Chuyen khoan") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Chuyen khoan", fontSize = 16.sp)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMethodSelected("Thanh toan khi nhan hang") }
                .padding(vertical = 8.dp)
        ) {
            RadioButton(
                selected = selectedMethod == "Thanh toan khi nhan hang",
                onClick = { onMethodSelected("Thanh toan khi nhan hang") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Thanh toan khi nhan hang", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryScreenPreview() {
    ASMTheme {
        DeliveryScreen()
    }
}
