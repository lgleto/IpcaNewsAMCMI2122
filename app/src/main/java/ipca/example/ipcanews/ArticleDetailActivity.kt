package ipca.example.ipcanews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import org.json.JSONObject

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val articleStr = intent.getStringExtra(ARTICLE_JSON)
        val article = Article.fromJSON(JSONObject(articleStr))

        title = article.title

        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl(article.url)

    }

    companion object{
        const val ARTICLE_JSON = "article_json"
    }
}