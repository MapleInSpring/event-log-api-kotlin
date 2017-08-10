package pivotal.eventgoal.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class EventController {
    @GetMapping("/")
    fun findAll() = arrayOf(1,2,3)
}