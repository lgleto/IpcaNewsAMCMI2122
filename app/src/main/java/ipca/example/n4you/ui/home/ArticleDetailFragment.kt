package ipca.example.n4you.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ipca.example.ipcanews.Article
import ipca.example.n4you.MainActivity
import ipca.example.n4you.R
import ipca.example.n4you.databinding.FragmentArticleDetailBinding
import org.json.JSONObject

class ArticleDetailFragment : Fragment()  {

    private var _binding : FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var article : Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            val articleStr = args.getString("article_json")
            article = Article.fromJSON(JSONObject(articleStr))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = article.title
        binding.webView.loadUrl(article.url)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_article, menu)
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
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}