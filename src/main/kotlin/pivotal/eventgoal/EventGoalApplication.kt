package pivotal.eventgoal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EventGoalApplication

fun main(args: Array<String>) {
    SpringApplication.run(EventGoalApplication::class.java, *args)
}
