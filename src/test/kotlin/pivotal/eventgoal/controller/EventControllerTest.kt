package pivotal.eventgoal.controller

import io.restassured.RestAssured
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pivotal.eventgoal.EventGoalApplication
import pivotal.eventgoal.entity.Event
import pivotal.eventgoal.entity.io.UpdateEvent

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(EventGoalApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest {
    @LocalServerPort val serverPort: Int = 0

    @Before
    fun setup() {
        RestAssured.port = serverPort
    }

    @Test
    fun getAllEvents() {
        createNewEvent("event1", "event description")
        get("/events/")
                .then()
                .contentType(ContentType.JSON)
                .body("list.size()", equalTo(1))
                .body("[0].name", equalTo("event1"))
                .body("[0].description", equalTo("event description"))
    }

    @Test
    fun postEvent() {
        val event = Event("event name", "event description")

        given()
                .contentType("application/json")
                .body(event)
                .`when`()
                .post("/events/")
                .then()
                .statusCode(200)
                .body("name", equalTo("event name"))
                .body("description", equalTo("event description"))

    }

    @Test
    fun updateEvent_onlyUpdatesSubmittedFields() {
        val eventId = createNewEvent()
        // full update
        val fullUpdateEvent = UpdateEvent("first update", "first description")

        given()
                .contentType("application/json")
                .body(fullUpdateEvent)
                .`when`()
                .put("/events/$eventId")
                .then()
                .statusCode(200)
                .body("name", equalTo("first update"))
                .body("description", equalTo("first description"))

        // partial update
        val partialUpdateName = UpdateEvent(name = "second update")

        given()
                .contentType("application/json")
                .body(partialUpdateName)
                .`when`()
                .put("/events/$eventId")
                .then()
                .statusCode(200)
                .body("name", equalTo("second update"))
                .body("description", equalTo("first description"))

        val partialUpdateDescription = UpdateEvent(description = "second description")

        given()
                .contentType("application/json")
                .body(partialUpdateDescription)
                .`when`()
                .put("/events/$eventId")
                .then()
                .statusCode(200)
                .body("name", equalTo("second update"))
                .body("description", equalTo("second description"))
    }

    @Test
    fun deleteEvent() {
        val eventId = createNewEvent()

        delete("/events/$eventId")
                .then()
                .statusCode(200)
    }

    @Test
    fun deleteEvent_returnsOkayIfIdDoesNotExist() {
        delete("/events/123456432")
                .then()
                .statusCode(200)
    }

    private fun createNewEvent(name: String = "new event", description: String = "new event description"): Long {
        return given()
                .contentType("application/json")
                .body(Event(name, description))
                .`when`()
                .post("/events/")
                .then()
                .extract()
                .path<Long>("id")
    }
}
