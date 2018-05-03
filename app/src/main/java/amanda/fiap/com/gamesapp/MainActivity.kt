package amanda.fiap.com.gamesapp

import amanda.fiap.com.gamesapp.ui.AboutFragment
import amanda.fiap.com.gamesapp.ui.game.GameFragment
import amanda.fiap.com.gamesapp.ui.game.ListGamesFragment
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var s: SharedPreferences? = null
    var e = s?.edit()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(ListGamesFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_create -> {
                changeFragment(GameFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                changeFragment(AboutFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_logout -> {
                s = applicationContext.getSharedPreferences("login", 0)
                val e = s?.edit()

                e?.putBoolean("validLogin", false)?.commit()
                val logado = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(logado)
                finish()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerFragment, fragment)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        changeFragment(ListGamesFragment())
    }
}
