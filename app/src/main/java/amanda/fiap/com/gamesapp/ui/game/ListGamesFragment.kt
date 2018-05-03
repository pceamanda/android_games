package amanda.fiap.com.gamesapp.ui.game

import amanda.fiap.com.gamesapp.R
import amanda.fiap.com.gamesapp.api.GameApi
import amanda.fiap.com.gamesapp.client.RetrofitClient
import amanda.fiap.com.gamesapp.constants.Constants
import amanda.fiap.com.gamesapp.model.Game
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_list_game.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.error.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListGamesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSearch.setOnClickListener {
            val api = RetrofitClient
                    .getInstance(Constants.API_URL)
                    .create(GameApi::class.java)

            api.findByCategory(findByCategory.editText?.text.toString().toLowerCase()).enqueue(object : Callback<List<Game>> {
                override fun onResponse(call: Call<List<Game>>?, response: Response<List<Game>>?) {
                    if(response?.isSuccessful == true) {
                        if(findByCategory.editText?.text.toString().isEmpty()){
                            Toast.makeText(context,
                                    "Categoria é obrigatória",
                                    Toast.LENGTH_SHORT).show()
                        }
                        findByCategory.editText?.setText("")
                        setup(response.body())
                    }
                }

                override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                    Toast.makeText(context,
                            "Erro ao buscar jogos",
                            Toast.LENGTH_SHORT).show()
                }
            })
        }

        findAllGames()
    }

    fun findAllGames() {
        val api = RetrofitClient
                .getInstance(Constants.API_URL)
                .create(GameApi::class.java)

        loading.visibility = View.VISIBLE

        api.findAll().enqueue(object: Callback<List<Game>> {

            override fun onResponse(call: Call<List<Game>>?, response: Response<List<Game>>?) {

                error.visibility = View.GONE
                tvMsgErro.text = ""

                if(response?.isSuccessful == true) {
                    setup(response.body())
                } else {
                    error.visibility = View.VISIBLE
                    tvMsgErro.text = response?.errorBody()?.string()
                }

                loading.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                error.visibility = View.VISIBLE
                tvMsgErro.text = t?.message
                loading.visibility = View.GONE
            }
        })
    }

    fun setup(games: List<Game>?) {
        games.let {
            rvGames.adapter = ListGamesAdapter(games!!, context)
            val layoutManager = LinearLayoutManager(context)
            rvGames.layoutManager = layoutManager
        }
    }
}
