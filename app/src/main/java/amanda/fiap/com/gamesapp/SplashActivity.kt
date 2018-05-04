package amanda.fiap.com.gamesapp

import amanda.fiap.com.gamesapp.api.UserApi
import amanda.fiap.com.gamesapp.client.RetrofitClient
import amanda.fiap.com.gamesapp.constants.Constants
import amanda.fiap.com.gamesapp.model.User
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    var s: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        s = applicationContext.getSharedPreferences("login", 0)

        if(s!!.getBoolean("validLogin", false)){
            val usuario = s!!.getString("username", null)
            val senha = s!!.getString("password", null)

            val api = RetrofitClient.getInstance(Constants.API_URL).create(UserApi::class.java)
            val login = User(null, usuario, senha)
            api.login(login).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                    if(response?.isSuccessful == true) {
                        if (response.body()?.username != null && !response.body()?.username.equals("")) {
                            try {
                                val logado = Intent(this@SplashActivity, MainActivity::class.java)
                                startActivity(logado)
                                finish()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Usuario e/ou Senha Inválidos", Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(applicationContext, response?.body().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Log.e("LOGIN", t?.message)
                    Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                }
            })
        }

        carregarSplash()
    }

    fun carregarSplash(){
        val animacao = AnimationUtils.loadAnimation(this, R.anim.animation_splash)
        ivImageLogo.startAnimation(animacao)

        Handler().postDelayed({
            if(!s!!.getBoolean("validLogin", false)) {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }
        }, 6000)
    }
}
