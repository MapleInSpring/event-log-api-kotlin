package pivotal.eventgoal.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pivotal.eventgoal.EventGoalApplication
import pivotal.eventgoal.entity.Goal
import pivotal.eventgoal.entity.GoalTerm
import pivotal.eventgoal.entity.GoalTerm.LONG_TERM
import pivotal.eventgoal.entity.GoalTerm.SHORT_TERM
import pivotal.eventgoal.entity.io.UpdateGoal

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(EventGoalApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoalControllerTest {
    @LocalServerPort val serverPort: Int = 0

    @Before
    fun setup() {
        RestAssured.port = serverPort
    }

    @Test
    fun getAllGoals() {
        createNewGoal("goal1", LONG_TERM)
        RestAssured.get("/goals/")
                .then()
                .contentType(ContentType.JSON)
                .body("list.size()", Matchers.equalTo(1))
                .body("[0].name", Matchers.equalTo("goal1"))
                .body("[0].term", Matchers.equalTo(LONG_TERM.name))
    }

    @Test
    fun postGoal() {
        val event = Goal("goal name", LONG_TERM)

        RestAssured.given()
                .contentType("application/json")
                .body(event)
                .`when`()
                .post("/goals/")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("goal name"))
                .body("term", Matchers.equalTo(LONG_TERM.name))

    }

    @Test
    fun updateGoal_onlyUpdatesSubmittedFields() {
        val goalId = createNewGoal()
        // full update
        val fullUpdateGoal = UpdateGoal("first update", LONG_TERM)

        RestAssured.given()
                .contentType("application/json")
                .body(fullUpdateGoal)
                .`when`()
                .put("/goals/$goalId")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("first update"))
                .body("term", Matchers.equalTo(LONG_TERM.name))

        // partial update
        val partialUpdateName = UpdateGoal(name = "second update")

        RestAssured.given()
                .contentType("application/json")
                .body(partialUpdateName)
                .`when`()
                .put("/goals/$goalId")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("second update"))
                .body("term", Matchers.equalTo(LONG_TERM.name))

        val partialUpdateDescription = UpdateGoal(term = SHORT_TERM)

        RestAssured.given()
                .contentType("application/json")
                .body(partialUpdateDescription)
                .`when`()
                .put("/goals/$goalId")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("second update"))
                .body("term", Matchers.equalTo(SHORT_TERM.name))
    }

    @Test
    fun deleteGoal() {
        val goalId = createNewGoal()

        RestAssured.delete("/goals/$goalId")
                .then()
                .statusCode(200)
    }

    @Test
    fun deleteGoal_returnsOkayIfIdDoesNotExist() {
        RestAssured.delete("/goals/123456432")
                .then()
                .statusCode(200)
    }

    private fun createNewGoal(name: String = "new event", term: GoalTerm = SHORT_TERM): Long {
        return RestAssured.given()
                .contentType("application/json")
                .body(Goal(name, term))
                .`when`()
                .post("/goals/")
                .then()
                .extract()
                .path<Long>("id")
    }
}