package amanda.fiap.com.gamesapp.api

import amanda.fiap.com.gamesapp.model.Game
import retrofit2.Call
import retrofit2.http.*

interface GameApi {

    @GET("/game")
    fun findAll() : Call<List<Game>>

    @GET("/game/{platform}")
    fun findByPlatform(@Path("platform") category: String): Call<List<Game>>

    @GET("/game/{category}")
    fun findByCategory(@Path("category") category: String): Call<List<Game>>

    @POST("/game")
    fun save(@Body game: Game): Call<Void>

    @PUT("/game")
    fun update(@Body game: Game): Call<Void>

    @DELETE("/game/{id}")
    fun delete(@Path("id") id: Long): Call<Void>

}