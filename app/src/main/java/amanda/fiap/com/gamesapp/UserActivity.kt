package amanda.fiap.com.gamesapp

import amanda.fiap.com.gamesapp.api.UserApi
import amanda.fiap.com.gamesapp.client.RetrofitClient
import amanda.fiap.com.gamesapp.constants.Constants
import amanda.fiap.com.gamesapp.model.User
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        btnCreate.setOnClickListener {
            val api = RetrofitClient.getInstance(Constants.API_URL).create(UserApi::class.java)

            if(!validarCadastro()) {
                Toast.makeText(applicationContext, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show()
            } else if(newPassword?.editText?.text.toString() == confirmPassword?.editText?.text.toString()) {

                val newUser = User(null, newUser?.editText?.text.toString(), newPassword?.editText?.text.toString())

                api.save(newUser).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.isSuccessful == true) {
                            Toast.makeText(applicationContext, "Usuario cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                            try {
                                val cadastro = Intent(this@UserActivity, LoginActivity::class.java)
                                startActivity(cadastro)
                                finish()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Erro, não foi possivel salvar :(", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e("CADASTRO", t?.message)
                        Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                    }
                })

            } else {
                Toast.makeText(applicationContext, "Senhas são conferem", Toast.LENGTH_LONG).show()
            }
        }

        btnBack.setOnClickListener {
            val back = Intent(this@UserActivity, LoginActivity::class.java)
            startActivity(back)
            finish()
        }
    }

    fun validarCadastro(): Boolean {

        if("" == newUser?.editText?.text.toString()) {
            return false
        }

        if("" == newPassword?.editText?.text.toString()){
            return false
        }

        if("" == confirmPassword?.editText?.text.toString()){
            return false
        }

        return true
    }
}
