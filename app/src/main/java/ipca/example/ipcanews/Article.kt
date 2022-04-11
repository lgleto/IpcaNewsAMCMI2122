package ipca.example.ipcanews

import org.json.JSONObject
import java.util.*

class Article  (
    var title       : String?,
    var description : String?,
    var url         : String,
    var urlToImage  : String?,
    var publishedAt : Date?)
{

    companion object{
        fun fromJSON(jsonObject: JSONObject) : Article{
            return Article(
                jsonObject["title"] as? String?,
                jsonObject["description"] as? String?,
                jsonObject["url"] as String,
                jsonObject["urlToImage"] as? String?,
                null
            )
        }
    }

}