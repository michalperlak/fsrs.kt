package com.github.mpps.model

import java.time.OffsetDateTime

data class ReviewLog(
    val rating: Rating,
    val state: State,
    val due: OffsetDateTime,
    val stability: Int,
    val difficulty: Int,
    val elapsedDays: List<Int>,
    val scheduledDays: Int,
    val review: OffsetDateTime
)