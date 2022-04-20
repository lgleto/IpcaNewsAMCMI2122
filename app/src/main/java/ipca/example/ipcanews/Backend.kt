package ipca.example.ipcanews

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.InputStream


object Backend {

    const val BASE_URI = "https://newsapi.org/v2/"
    const val PATH_HEADLINES = "top-headlines?country=pt&category="
    const val API_KEY = "&apiKey=1765f87e4ebc40229e80fd0f75b6416c"

    fun getTopHeadLinesArticles( category: String, callback : (( List<Article>)->Unit) ) {
        var  articles = arrayListOf<Article>()
        GlobalScope.launch (Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(
                    BASE_URI +
                            PATH_HEADLINES +
                            category +
                            API_KEY)
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
                           callback.invoke(articles)
                        }
                    }
                }
            }
        }
    }

    fun getImage(  urlImage: String,  callback : ((Bitmap)->Unit) ){
        GlobalScope.launch (Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urlImage)
                .build()
            client.newCall(request).execute().use { response ->
                response.body?.let { body->
                    val inputStream: InputStream? = body.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    GlobalScope.launch (Dispatchers.Main) {
                        callback.invoke(bitmap)
                    }
                }
            }
        }
    }
}