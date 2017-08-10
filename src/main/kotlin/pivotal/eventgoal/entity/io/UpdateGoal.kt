package pivotal.eventgoal.entity.io

import pivotal.eventgoal.entity.GoalTerm

data class UpdateGoal(
        val name: String? = null,
        val term: GoalTerm? = null
)