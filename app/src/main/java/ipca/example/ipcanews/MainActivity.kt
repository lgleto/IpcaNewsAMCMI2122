package ipca.example.ipcanews

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var  articles = arrayListOf<Article>()
    private var myVib: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myVib = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        val listViewArticles = findViewById<ListView>(R.id.listViewArticles)
        val articlesAdapter = ArticlesAdapter()
        listViewArticles.adapter = articlesAdapter

        Backend.getTopHeadLinesArticles("sports", getString(R.string.country)){
            articles = it as ArrayList<Article>
            articlesAdapter.notifyDataSetChanged()
        }
    }

    inner class ArticlesAdapter : BaseAdapter() , View.OnLongClickListener{
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
            val textViewDate = rootView.findViewById<TextView>(R.id.textViewDate)

            val imageViewPhoto = rootView.findViewById<ImageView>(R.id.imageViewPhoto)
            textViewTitle.text = articles[position].title
            textViewDescription.text = articles[position].description
            textViewDate.text = articles[position].publishedAt?.toStrAAMMDD()

            articles[position].urlToImage?.let { url ->
                Backend.getImage(url){ bitmap ->
                    imageViewPhoto.setImageBitmap(bitmap)
                }
            }

            rootView.tag = position

            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)
                intent.putExtra(ArticleDetailActivity.ARTICLE_JSON, articles[position].toJSON().toString())
                startActivity(intent)
            }
            rootView.setOnLongClickListener (this)

            return rootView
        }

        override fun onLongClick(view: View?): Boolean {
            val position = view?.tag as? Int
            position?.let{


                val article = articles[position]

                article.urlToImage?.let { url ->
                    Backend.getImage(url){ bitmap ->

                        val d: Drawable = BitmapDrawable(resources, bitmap)
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle(article.title)
                            .setMessage(article.description) // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(getString(R.string.dismiss),
                                DialogInterface.OnClickListener { dialog, which ->
                                    // Continue with delete operation
                                })
                            .setIcon(d)
                            .show()
                    }
                }







                //Toast.makeText(this@MainActivity,articles[position].title, Toast.LENGTH_LONG ).show()
                myVib?.vibrate(50)
                return true
            }
            return false
        }
    }

}