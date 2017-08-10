package pivotal.eventgoal.controller

import org.springframework.web.bind.annotation.*
import pivotal.eventgoal.entity.Event
import pivotal.eventgoal.entity.io.UpdateEvent
import pivotal.eventgoal.repository.EventRepository

@RestController
@RequestMapping("/events")
class EventController(val eventRepo: EventRepository) {
    @GetMapping("/")
    fun findAll(): Iterable<Event> = eventRepo.findAll()

    @PostMapping("/")
    fun create(@RequestBody event: Event): Event = eventRepo.save(event)

    @PutMapping("/{id}")
    fun update(@RequestBody updateEvent: UpdateEvent, @PathVariable id: Long): Event {
        return eventRepo.findOne(id).let {
            eventRepo.save(
                    it.copy(name = updateEvent.name ?: it.name,
                            description = updateEvent.description ?: it.description))
        }

    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        eventRepo.findOne(id)?.let {
            eventRepo.delete(it)
        }
    }
}