package fpoly.example.asm.Interface

import AddtoCartRequest
import ApiResponse
import CartItem
import Order
import fpoly.example.asm.Model.*
import retrofit2.Call
import retrofit2.http.*

interface API_PRODUCT {

    // Get the list of products


    // Add a new product
    @POST("/products/add")
    fun addProduct(@Body product: Product): Call<Product>

//     Add a product to the cart (requires authentication)
    @POST("/products/cart")
    fun addToCart(
    @Body request: AddtoCartRequest,
    @Header("Authorization") token: String
   ): Call<ApiResponse>


    @GET("products/get-cart")
    fun getCart(@Header("Authorization") token: String): Call<List<CartItem>>

    //
    // Checkout the cart (requires authentication)

        @POST("products/checkout")
        fun createOrder(
            @Body order: Order
        ): Call<Order>

//
     // Lấy sản phẩm theo ID để chỉnh sửa
    @GET("products/{productId}")
    fun getProductID(@Path("productId") productId: String): Call<Product>

    // Search products by query
    @GET("/products/search")
    fun searchProducts(@Query("q") query: String): Call<List<Product>>

    // Get product by ID
    @GET("/products/json")
    fun getProduct():Call<List<Product>>

    // Update product by ID
    @PUT("/products/edit/{id}")
    fun updateProduct(@Path("id") id: String, @Body product: Product): Call<Product>

    // Delete product by ID
    @DELETE("/products/delete/{id}")
    fun deleteProduct(@Path("id") id: String): Call<Void>
}
