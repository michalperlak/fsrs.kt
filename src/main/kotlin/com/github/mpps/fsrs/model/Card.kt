package com.github.mpps.fsrs.model

import java.time.OffsetDateTime

data class Card(
    var due: OffsetDateTime,
    var stability: Double,
    var difficulty: Double,
    var elapsedDays: Long,
    var scheduledDays: Long,
    var reps: Long,
    var lapses: Long,
    var state: State,
    var lastReview: OffsetDateTime?
) {

    companion object {
        fun createEmpty(now: OffsetDateTime): Card =
            Card(
                due = now,
                stability = 0.0,
                difficulty = 0.0,
                elapsedDays = 0,
                scheduledDays = 0,
                reps = 0,
                lapses = 0,
                state = State.New,
                lastReview = null,
            )
    }
}