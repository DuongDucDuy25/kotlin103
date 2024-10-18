package fpoly.example.asm.Main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import fpoly.example.asm.Interface.API_PRODUCT
import fpoly.example.asm.R
import fpoly.example.asm.Main.ui.theme.ASMTheme
import fpoly.example.asm.Model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DanhSachSanPham : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ASMTheme {
                val navController = rememberNavController()
                BottomNavScreen(navController)
            }
        }
    }
}

@Composable
fun NavHostScreen(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = "home",modifier = Modifier.padding(paddingValues)) {
        composable("home") { MainScreen(navController) }
        // Uncomment this line when you implement the cart screen
         composable("cart") { GioHangScreen(navController) }
        composable("profile"){ ThongTinNguoiDungScreen(paddingValues) }
        composable("order"){ OrderHistoryScreen() }

        composable("detail/{productId}/{name}/{image_url}/{price}/{description}/{category}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("image_url") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""

            // Tạo đối tượng Product từ các tham số
            val product = Product(
                productId = productId,
                name = name,
                image_url = imageUrl,
                price = price,
                description = description,
                category = category
            )

            // Gọi ChiTietSanPhamScreen với đối tượng product
            ChiTietSanPhamScreen(product)
//            ThanhToanScreen(product)
        }
        composable("payment/{selectedItems}/{totalPrice}") { backStackEntry ->
            val selectedItems = backStackEntry.arguments?.getString("selectedItems") ?: ""
            val totalPrice = backStackEntry.arguments?.getString("totalPrice")?.toDouble() ?: 0.0


            ThanhToanScreen(selectedItems, totalPrice)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val searchQuery = remember { mutableStateOf("") }
    val searchResult = remember { mutableStateOf<List<Product>?>(null) }
    val isDropdownOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                isDropdownOpen.value = it.isNotEmpty()
                Log.d("MainScreen", "Search query updated: $it") // Log khi query được thay đổi
                // Call search function when query changes
                searchProducts(it, searchResult)
            },
            placeholder = { Text("Tìm kiếm sản phẩm...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    Log.d("MainScreen", "Search action triggered with query: ${searchQuery.value}") // Log khi bắt đầu tìm kiếm
                    searchProducts(searchQuery.value, searchResult)
                    isDropdownOpen.value = false // Close dropdown after search
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Tìm kiếm",
                    Modifier.size(30.dp)
                )
            }
        )

        // Dropdown displaying search results
        if (isDropdownOpen.value && !searchResult.value.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .heightIn(max = 200.dp) // Limit height of dropdown
            ) {
                searchResult.value?.let { products ->
                    items(products.size) { index ->
                        val product = products[index]
                        DropdownItem(product = product, onClick = {
                            searchQuery.value = product.name
                            isDropdownOpen.value = false // Close dropdown when item selected
                            Log.d("MainScreen", "Selected product: ${product.name}")
                        })
                    }
                }
            }
        }

        BannerSlider()   // Banner Section
        CategorySection() // Product Category Section
        ItemList(navController, searchQuery, searchResult) // Cập nhật ItemList để truyền searchQuery và searchResult
    }
}

@Composable
fun DropdownItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(text = product.name)
    }
}
@Composable
fun CategorySection() {
    val categories = listOf(
        "Món ăn",
        "Đồ ăn thêm",
        "Topping",
        "Món khác"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories.size) { index ->
            CategoryCard(categories[index])
        }
    }
}
@Composable
fun CategoryCard(name: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(65.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.comtam),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerSlider() {
    val images = listOf(
        R.drawable.comtam,
        R.drawable.comtam1,
        R.drawable.comtam2,
        R.drawable.comtam3
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { images.size }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                val painter: Painter = painterResource(id = images[page])
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
fun searchProducts(query: String, searchResult: MutableState<List<Product>?>) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")  // Chỉnh sửa base URL theo môi trường của bạn
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(API_PRODUCT::class.java)
    service.searchProducts(query).enqueue(object : Callback<List<Product>> {
        override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
            searchResult.value = response.body()
        }

        override fun onFailure(call: Call<List<Product>>, t: Throwable) {
            Log.d("API_ERROR", "Không thể tìm sản phẩm: ${t.message}")
        }
    })
}

@Composable
fun ItemList(navController: NavHostController, searchQuery: MutableState<String>, searchResult: MutableState<List<Product>?>) {
    // Trạng thái để theo dõi danh sách sản phẩm và trạng thái tải dữ liệu
    val productList = remember { mutableStateOf<List<Product>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) } // Trạng thái tải dữ liệu

    // Sử dụng LaunchedEffect để tải dữ liệu
    LaunchedEffect(Unit) {
        isLoading.value = true // Bắt đầu tải dữ liệu
        fetchProducts(productList) // Gọi hàm để tải sản phẩm
        isLoading.value = false // Sau khi dữ liệu đã tải xong
    }

    // Danh sách hiển thị (dựa trên kết quả tìm kiếm hoặc toàn bộ sản phẩm)
    val filteredProducts = if (searchQuery.value.isNotEmpty()) {
        searchResult.value ?: emptyList()
    } else {
        productList.value
    }

    // Hiển thị danh sách sản phẩm bằng LazyColumn
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            when {
                // Trạng thái đang tải
                isLoading.value -> {
                    Text("Đang tải danh sách sản phẩm...", Modifier.padding(16.dp))
                }
                // Khi danh sách rỗng
                filteredProducts.isEmpty() -> {
                    Text("Không có sản phẩm nào.", Modifier.padding(16.dp))
                }
            }
        }

        // Hiển thị các sản phẩm nếu danh sách không rỗng
        items(filteredProducts.size) { index ->
            FoodItemCard(navController, filteredProducts[index])
        }
    }
}


fun fetchProducts(productList: MutableState<List<Product>>) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(API_PRODUCT::class.java)
    service.getProduct().enqueue(object : Callback<List<Product>> {
        override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
            productList.value = response.body() ?: emptyList()
        }

        override fun onFailure(call: Call<List<Product>>, t: Throwable) {
            Log.d("API_ERROR", "Không thể lấy dữ liệu sản phẩm: ${t.message}")
        }
    })
}

@Composable
fun FoodItemCard(navController: NavHostController, product: Product) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("detail/${Uri.encode(product.productId)}/${Uri.encode(product.name)}/${Uri.encode(product.image_url)}/${Uri.encode(product.price)}/${Uri.encode(product.description)}/${Uri.encode(product.category)}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = product.image_url,
                    contentDescription = product.name,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = product.price,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(255, 87, 34, 255),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val route: String, val title: String, val icon: ImageVector)

@Composable
fun BottomNavScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHostScreen(navController = navController, paddingValues = paddingValues)
    }
}
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = Color(0xFFF5F5F5),
        contentColor = Color.Black,
        modifier = Modifier.padding(bottom = 40.dp) // Thay đổi giá trị 16.dp theo ý muốn
    ) {
        val items = listOf(
            BottomNavItem("home", "Trang Chủ", Icons.Filled.Home),
            BottomNavItem("cart", "Giỏ Hàng", Icons.Filled.ShoppingCart),
            BottomNavItem("profile", "Thông Tin", Icons.Filled.Person),
            BottomNavItem("order", "Đơn hàng", Icons.Filled.DateRange)
        )

        items.forEach { item ->
            BottomNavigationItem(
                selected = false,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    ASMTheme {
        val navController = rememberNavController()
//        NavHostScreen(navController)
    }
}
