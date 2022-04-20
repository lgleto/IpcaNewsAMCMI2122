package ipca.example.ipcanews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toast
import org.json.JSONObject

class ArticleDetailActivity : AppCompatActivity() {
    lateinit var article : Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val articleStr = intent.getStringExtra(ARTICLE_JSON)
        article = Article.fromJSON(JSONObject(articleStr))

        title = article.title

        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl(article.url)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_article, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, article.url)
                    putExtra(Intent.EXTRA_TITLE, article.title)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Share to.."))

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object{
        const val ARTICLE_JSON = "article_json"
    }
}