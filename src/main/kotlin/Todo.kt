import kotlin.random.Random

data class Todo(
    val id: Long = Random.nextLong(0, 99999),
    val text: String = "Todo${Random.nextInt(1, 100)}",
    val completed: Boolean = true
)