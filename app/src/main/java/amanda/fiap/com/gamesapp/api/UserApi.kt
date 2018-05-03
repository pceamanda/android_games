package amanda.fiap.com.gamesapp.api

import amanda.fiap.com.gamesapp.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/user/login")
    fun login(@Body user: User): Call<User>

    @POST("/user")
    fun save(@Body user: User): Call<Void>

}