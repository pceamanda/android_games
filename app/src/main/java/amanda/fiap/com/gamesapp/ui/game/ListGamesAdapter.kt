package amanda.fiap.com.gamesapp.ui.game

import amanda.fiap.com.gamesapp.MainActivity
import amanda.fiap.com.gamesapp.R
import amanda.fiap.com.gamesapp.model.Game
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_game.view.*

class ListGamesAdapter(private val games: List<Game>, private val context: Context?) : RecyclerView.Adapter<ListGamesAdapter.GamesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        return GamesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        val game = games[position]
        holder.bindView(game, context)
    }

    class GamesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(game: Game, context: Context?) {

            val activity = context as MainActivity

            itemView.setOnClickListener{
                val edit = GameFragment()
                edit.game = game
                activity.changeFragment(edit)
            }

            itemView.tvTitle.text = game.title
            itemView.tvCategory.text = game.category
            if (game.imageUrl.isEmpty()) {
                itemView.ivImage.setBackgroundResource(R.drawable.no_image)
            } else {
                Picasso.get()
                        .load(game.imageUrl)
                        .placeholder(R.drawable.waiting)
                        .error(R.drawable.no_image)
                        .into(itemView.ivImage)
            }
        }
    }
}