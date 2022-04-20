package ipca.example.ipcanews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
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

        val listViewArticles = findViewById<ListView>(R.id.listViewArticles)
        val articlesAdapter = ArticlesAdapter()
        listViewArticles.adapter = articlesAdapter

        Backend.getTopHeadLinesArticles("sports"){
            articles = it as ArrayList<Article>
            articlesAdapter.notifyDataSetChanged()
        }
    }

    inner class ArticlesAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return articles.size
        }

        override fun getItem(position: Int): Any {
            return articles[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_article,viewGroup,false)

            val textViewTitle = rootView.findViewById<TextView>(R.id.textViewTitle)
            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewDescription)
            val imageViewPhoto = rootView.findViewById<ImageView>(R.id.imageViewPhoto)
            textViewTitle.text = articles[position].title
            textViewDescription.text = articles[position].description

            articles[position].urlToImage?.let { url ->
                Backend.getImage(url){ bitmap ->
                    imageViewPhoto.setImageBitmap(bitmap)
                }
            }

            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)
                intent.putExtra(ArticleDetailActivity.ARTICLE_JSON, articles[position].toJSON().toString())
                startActivity(intent)
            }

            return rootView
        }
    }

}