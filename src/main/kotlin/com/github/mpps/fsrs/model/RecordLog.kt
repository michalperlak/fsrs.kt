package com.github.mpps.fsrs.model

class RecordLog(
    private val items: Map<Rating, RecordLogItem>
) {
    operator fun get(rating: Rating) = items[rating]
}