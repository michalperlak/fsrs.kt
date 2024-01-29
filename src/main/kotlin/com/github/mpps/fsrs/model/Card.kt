package com.github.mpps.fsrs.model

import java.time.OffsetDateTime

data class Card(
    val due: OffsetDateTime,
    val stability: Int,
    val difficulty: Int,
    val elapsedDays: Int,
    val scheduledDays: Int,
    val reps: Int,
    val lapses: Int,
    val state: State,
    val lastReview: OffsetDateTime?
)