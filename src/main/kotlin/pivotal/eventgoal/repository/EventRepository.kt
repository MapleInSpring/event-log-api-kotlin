package pivotal.eventgoal.repository

import org.springframework.data.repository.CrudRepository
import pivotal.eventgoal.entity.Event

interface EventRepository: CrudRepository<Event, Long>