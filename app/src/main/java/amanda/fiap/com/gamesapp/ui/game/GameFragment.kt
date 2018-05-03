package amanda.fiap.com.gamesapp.ui.game

import amanda.fiap.com.gamesapp.MainActivity
import amanda.fiap.com.gamesapp.R
import amanda.fiap.com.gamesapp.api.GameApi
import amanda.fiap.com.gamesapp.client.RetrofitClient
import amanda.fiap.com.gamesapp.constants.Constants
import amanda.fiap.com.gamesapp.model.Game
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_game.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameFragment : Fragment() {

    var game: Game = Game(null, "", "", "", "")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(game.id != null) {
            inputCategory.editText?.setText(game.category)
            inputPlatform.editText?.setText(game.platform)
            inputTitle.editText?.setText(game.title)
            inputUrlImage.editText?.setText(game.imageUrl)
            btnDelete.visibility = View.VISIBLE
        } else {
            btnDelete.visibility = View.GONE
        }

        btnSave.setOnClickListener {
            val api = RetrofitClient
                    .getInstance(Constants.API_URL)
                    .create(GameApi::class.java)

            if (!validate()) {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show()
            } else {
                val game = Game(game.id,
                        inputTitle.editText?.text.toString(),
                        inputPlatform.editText?.text.toString(),
                        inputCategory.editText?.text.toString(),
                        inputUrlImage.editText?.text.toString())

                if (game.id != null) {
                    api.update(game).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if (response?.isSuccessful == true) {
                                Toast.makeText(context, "Jogo alterado com sucesso", Toast.LENGTH_SHORT).show()
                                clear()
                                val activity = context as MainActivity
                                activity.changeFragment(ListGamesFragment())
                            } else {
                                Toast.makeText(context, "Jogo não pode ser alterado", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Log.e("game", t?.message)
                        }
                    })
                } else {
                    api.save(game).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if (response?.isSuccessful == true) {
                                Toast.makeText(context, "Jogo inserido com sucesso", Toast.LENGTH_SHORT).show()
                                clear()
                                val activity = context as MainActivity
                                activity.changeFragment(ListGamesFragment())
                            } else {
                                Toast.makeText(context, "Jogo não pode ser inserido", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Log.e("game", t?.message)
                        }
                    })
                }
            }
        }


        btnDelete.setOnClickListener {
            val api = RetrofitClient.getInstance(Constants.API_URL).create(GameApi::class.java)

            if(game.id != null) {
                api.delete(game.id!!).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.isSuccessful == true) {
                            Toast.makeText(context, "Jogo deletado com sucesso", Toast.LENGTH_SHORT).show()
                            clear()
                            val activity = context as MainActivity
                            activity.changeFragment(ListGamesFragment())
                        } else {
                            Toast.makeText(context, "Jogo não pode ser deletado", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e("game", t?.message)
                    }
                })
            }
        }
    }

    fun validate(): Boolean {

        if("" == inputCategory.editText?.text.toString()) {
            return false
        }

        if("" == inputPlatform.editText?.text.toString()) {
            return false
        }

        if("" == inputTitle.editText?.text.toString()) {
            return false
        }

        if("" == inputUrlImage.editText?.text.toString()) {
            return false
        }

        return true
    }

    private fun clear() {
        game.id = null
        inputUrlImage.editText?.setText("")
        inputTitle.editText?.setText("")
        inputPlatform.editText?.setText("")
        inputCategory.editText?.setText("")
    }

}
