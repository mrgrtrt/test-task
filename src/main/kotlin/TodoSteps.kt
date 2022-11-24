import ConfigKeys.API_HOST
import ConfigUtils.PROPERTIES
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.response.Response

class TodoSteps {

    private var reqSpec = RequestSpecBuilder()
        .setBaseUri(PROPERTIES[API_HOST])
        .setContentType(ContentType.JSON)
        .build()
        .log().all()

    fun deleteTodo(todoId: Long): Response {
        return given(reqSpec).auth().preemptive().basic("admin", "admin")
            .basePath("$todoId")
            .delete()
    }

    fun createTodo(todo: Todo?): Response {
        if (todo == null) {
            return given(reqSpec).post()
        }
        return given(reqSpec).body(todo).post()
    }

    fun getTodos(limit: Int? = null, offset: Int? = null): List<Todo> {
        var localReqSpec = RequestSpecBuilder().addRequestSpecification(reqSpec).build()
        if (limit != null) {
            localReqSpec = localReqSpec.queryParam("limit", limit)
        }
        if (offset != null) {
            localReqSpec = localReqSpec.queryParam("offset", offset)
        }
        return given(localReqSpec)
            .get().then()
            .extract().jsonPath().getList("", Todo::class.java)
    }

    fun updateTodo(todoId: Long, updatedTodo: Todo): Response {
        return given(reqSpec)
            .basePath("$todoId")
            .body(updatedTodo)
            .put()
    }

}