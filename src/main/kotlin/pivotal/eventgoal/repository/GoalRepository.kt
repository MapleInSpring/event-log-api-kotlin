package pivotal.eventgoal.repository

import org.springframework.data.repository.CrudRepository
import pivotal.eventgoal.entity.Goal

interface GoalRepository: CrudRepository<Goal, Long>