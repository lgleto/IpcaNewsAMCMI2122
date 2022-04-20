package ipca.example.ipcanews

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Article  (
    var title       : String?,
    var description : String?,
    var url         : String,
    var urlToImage  : String?,
    var publishedAt : Date?)
{

    fun toJSON() : JSONObject{
        val jsonObject = JSONObject()
        jsonObject.put("title"      ,title      )
        jsonObject.put("description",description)
        jsonObject.put("url"        ,url        )
        jsonObject.put("urlToImage" ,urlToImage )
        jsonObject.put("publishedAt",publishedAt?.toStrServer())
        return jsonObject
    }


    companion object{
        fun fromJSON(jsonObject: JSONObject) : Article{
            return Article(
                jsonObject["title"      ] as? String?,
                jsonObject["description"] as? String?,
                jsonObject["url"        ] as String,
                jsonObject["urlToImage" ] as? String?,
                (jsonObject["publishedAt" ] as? String?)?.toDate()
            )
        }
    }

}