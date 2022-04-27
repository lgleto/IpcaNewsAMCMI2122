package ipca.example.n4you.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ipca.example.ipcanews.Article
import ipca.example.ipcanews.Backend
import ipca.example.ipcanews.toStrAAMMDD
import ipca.example.n4you.R
import ipca.example.n4you.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    var  articles = arrayListOf<Article>()
    private var myVib: Vibrator? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articlesAdapter = ArticlesAdapter()
        binding.listViewArticles.adapter = articlesAdapter

        Backend.getTopHeadLinesArticles("general", "pt"){
            articles = it as ArrayList<Article>
            articlesAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                val bundle = Bundle()
                bundle.putString("article_json", articles[position].toJSON().toString())
                findNavController().navigate(
                    R.id.action_navigation_home_to_articleDetailFragment,
                    bundle)
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
                        AlertDialog.Builder(requireContext())
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