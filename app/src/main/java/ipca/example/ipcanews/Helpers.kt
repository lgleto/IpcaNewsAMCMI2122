package ipca.example.ipcanews

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate() : Date? {
    this?.let {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date =  formatter.parse(this)
        return date
    }
    return null
}

fun Date.toStrAAMMDD() : String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(this)
}

fun Date.toStrServer() : String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
    return formatter.format(this)
}