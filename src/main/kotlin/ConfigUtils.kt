import java.util.*

object ConfigUtils {

    val PROPERTIES = loadProperties()

    private fun loadProperties(): Map<String, String> {
        val configMap = HashMap<String, String>()

        val resource = Thread.currentThread().contextClassLoader.getResourceAsStream("application.properties")
        val properties = Properties()
        properties.load(resource)

        properties.map { configMap.put(it.key.toString(), it.value.toString()) }
        return configMap
    }
}

object ConfigKeys {
    const val API_HOST = "url"
}