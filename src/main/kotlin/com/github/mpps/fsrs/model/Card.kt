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
)