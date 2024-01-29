package com.github.mpps.fsrs.model

import java.time.OffsetDateTime

data class ReviewLog(
    val rating: Rating,
    val state: State,
    val due: OffsetDateTime,
    val stability: Double,
    val difficulty: Double,
    val elapsedDays: Long,
    val lastElapsedDays: Long,
    val scheduledDays: Long,
    val review: OffsetDateTime
)