package ipca.example.ipcanews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var  articles = arrayListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch (Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?country=pt&category=sports&apiKey=1765f87e4ebc40229e80fd0f75b6416c")
                .build()
            client.newCall(request).execute().use { response ->
                var result = response.body!!.string()
                var resultJson = JSONObject(result)
                if (resultJson.has("status")){
                    if (resultJson.getString("status") == "ok"){
                        var articlesJSArray = resultJson.getJSONArray("articles")
                        for (index in 0 until articlesJSArray.length()){
                            var articleJSON  = articlesJSArray[index] as JSONObject
                            var article = Article.fromJSON(articleJSON)
                            articles.add(article)
                        }

                        GlobalScope.launch (Dispatchers.Main){
                            var strArticles = ""
                            for (a in articles){
                                strArticles += a.title + "\n"

                            }
                            findViewById<TextView>(R.id.textViewHello).text = strArticles
                        }
                    }
                }
            }
        }
    }

}