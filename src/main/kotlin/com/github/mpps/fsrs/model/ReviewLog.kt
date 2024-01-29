package com.github.mpps.fsrs.model

import java.time.OffsetDateTime

data class ReviewLog(
    val rating: Rating,
    val state: State,
    val due: OffsetDateTime,
    val stability: Long,
    val difficulty: Long,
    val elapsedDays: Long,
    val lastElapsedDays: Long,
    val scheduledDays: Long,
    val review: OffsetDateTime
)