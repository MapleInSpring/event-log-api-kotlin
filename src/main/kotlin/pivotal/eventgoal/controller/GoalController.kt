package pivotal.eventgoal.controller

import org.springframework.web.bind.annotation.*
import pivotal.eventgoal.entity.Goal
import pivotal.eventgoal.entity.io.UpdateGoal
import pivotal.eventgoal.repository.GoalRepository

@RestController
@RequestMapping("/goals")
class GoalController(val goalRepo: GoalRepository) {
    @GetMapping("/")
    fun findAll(): Iterable<Goal> = goalRepo.findAll()

    @PostMapping("/")
    fun create(@RequestBody goal: Goal): Goal = goalRepo.save(goal)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updateGoal: UpdateGoal): Goal {
        return goalRepo.findOne(id).let {
            goalRepo.save(it.copy(
                    name = updateGoal.name ?: it.name,
                    term = updateGoal.term ?: it.term
            ))
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        goalRepo.findOne(id)?.let {
            goalRepo.delete(it)
        }
    }
}