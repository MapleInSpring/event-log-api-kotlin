package pivotal.eventgoal.entity

import pivotal.eventgoal.entity.GoalTerm.SHORT_TERM
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Goal(
        var name: String = "",
        val term: GoalTerm = SHORT_TERM,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0
)

enum class GoalTerm{
    SHORT_TERM,
    LONG_TERM
}