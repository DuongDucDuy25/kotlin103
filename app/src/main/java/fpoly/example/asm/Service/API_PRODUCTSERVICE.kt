package fpoly.example.asm.Service

import fpoly.example.asm.Interface.API_PRODUCT
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API_PRODUCTSERVICE {
    private const val BASE_URL = "http://10.0.2.2:3000" // Đảm bảo địa chỉ này trỏ đúng tới server

    val apiProductSevice: API_PRODUCT = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API_PRODUCT::class.java)

}