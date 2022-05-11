package ipca.example.n4you

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch (Dispatchers.IO){
            delay(2000)
            lifecycleScope.launch (Dispatchers.Main) {
                startActivity(
                    Intent(this@SplashActivity,
                        MainActivity::class.java))
            }
        }
    }
}