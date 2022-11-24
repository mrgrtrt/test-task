import ConfigKeys.API_HOST
import ConfigUtils.PROPERTIES
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestBase {

    companion object {

        @Container
        private val app = GenericContainer(DockerImageName.parse("todo-app"))
            .withExposedPorts(4242)

        lateinit var todoSteps: TodoSteps

        @JvmStatic
        @BeforeAll
        fun initialize() {
            app.waitingFor(Wait.forHttp("/"))

            val reqSpec = RequestSpecBuilder()
                .setBaseUri(PROPERTIES[API_HOST])
                .setPort(app.getMappedPort(4242))
                .setContentType(ContentType.JSON)
                .build()

            todoSteps = TodoSteps(reqSpec)
        }
    }
}