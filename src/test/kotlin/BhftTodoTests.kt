import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


class BhftTodoTests : TestBase() {

    @AfterEach
    fun deleteTodo() {
        todoSteps.getTodos().forEach {
            todoSteps.deleteTodo(it.id)
                .then()
                .statusCode(204)
        }
    }

    @Test
    fun `verify create TODO and GET returns newly created TODO`() {
        val todoToCreate = Todo()
        todoSteps.createTodo(todoToCreate)
            .then()
            .statusCode(201)

        val todos = todoSteps.getTodos()
        assertTrue(todos.contains(todoToCreate))
    }

    @Test
    fun `verify create TODO with empty body returns 400`() {
        todoSteps.createTodo(null)
            .then()
            .statusCode(400)
    }

    @Test
    fun `verify create TODO with existing id returns 400`() {
        val todo = Todo()

        todoSteps.createTodo(todo)
            .then()
            .statusCode(201)

        todoSteps.createTodo(todo)
            .then()
            .statusCode(400)
    }

    @Test
    fun `verify GET TODOs returns empty when there are no TODOs`() {
        val todos = todoSteps.getTodos()

        assertTrue(todos.isEmpty())
    }

    @ParameterizedTest(name = "case {index}: limit = {0}")
    @ValueSource(ints = [0, 1, 2])
    fun `verify GET TODO with limits`(limit: Int) {
        //создаем 2 туду
        val todos = listOf(Todo(), Todo())
        todos.forEach {
            todoSteps.createTodo(it)
                .then().statusCode(201)
        }

        val getTodos = todoSteps.getTodos(limit = limit)
        assertEquals(limit, getTodos.size)
    }

    @ParameterizedTest(name = "case {index}: offset = {0}")
    @ValueSource(ints = [0, 1, 2])
    fun `verify GET TODO with offset`(offset: Int) {
        //создаем 2 туду
        val todos = listOf(Todo(), Todo())
        todos.forEach {
            todoSteps.createTodo(it)
                .then().statusCode(201)
        }

        val getTodos = todoSteps.getTodos(offset = offset)
        assertEquals(todos.size - offset, getTodos.size)
    }

    @Test
    fun `verify GET TODO with limit and offset`() {
        //создаем 2 туду
        val todos = listOf(Todo(), Todo())
        todos.forEach {
            todoSteps.createTodo(it)
                .then().statusCode(201)
        }

        val getTodosOffset0 = todoSteps.getTodos(limit = 1, offset = 0)
        val getTodosOffset1 = todoSteps.getTodos(limit = 1, offset = 1)
        assertNotEquals(getTodosOffset0, getTodosOffset1)
    }

    @Test
    fun `verify updating nonexistent TODO returns 404`() {
        todoSteps.updateTodo(kotlin.random.Random.nextLong(0, 99999), Todo())
            .then()
            .statusCode(404)
    }

    @Test
    fun `verify successful update of TODO`() {
        val initialTodo = Todo()
        todoSteps.createTodo(initialTodo)
            .then()
            .statusCode(201)

        val updatedTodo = Todo()
        todoSteps.updateTodo(initialTodo.id, updatedTodo)
            .then()
            .statusCode(200)

        val todos = todoSteps.getTodos()
        assertTrue(todos.contains(updatedTodo))
        assertFalse(todos.contains(initialTodo))
    }

    @Test
    fun `verify delete nonexistent TODO returns 404`() {
        todoSteps.deleteTodo(kotlin.random.Random.nextLong(0, 99999))
            .then()
            .statusCode(404)
    }
}