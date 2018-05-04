package amanda.fiap.com.gamesapp

import amanda.fiap.com.gamesapp.api.UserApi
import amanda.fiap.com.gamesapp.client.RetrofitClient
import amanda.fiap.com.gamesapp.constants.Constants
import amanda.fiap.com.gamesapp.model.User
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    var s: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = applicationContext.getSharedPreferences("login", 0)
        val e = s?.edit()

        btn_login.setOnClickListener() {
            val api = RetrofitClient.getInstance(Constants.API_URL).create(UserApi::class.java)
            val login = User(null, username.editText?.text.toString(), password.editText?.text.toString())
            api.login(login).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                    if(response?.isSuccessful == true) {
                        if (response.body()?.username != null && !response.body()?.username.equals("")) {
                            try {
                                e?.putString("username", login.username)?.apply()
                                e?.putString("password", login.password)?.commit()
                                e?.putBoolean("validLogin", true)?.commit()

                                val userLogged = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(userLogged)
                                finish()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Usuário e/ou Senha Inválidos", Toast.LENGTH_LONG).show()
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

        btn_cadastrar.setOnClickListener(){
            try {
                val newUser = Intent(this@LoginActivity, UserActivity::class.java)
                startActivity(newUser)
                finish()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
