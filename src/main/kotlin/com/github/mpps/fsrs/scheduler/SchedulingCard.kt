package com.github.mpps.fsrs.scheduler

import com.github.mpps.fsrs.model.*
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class SchedulingCard(card: Card, now: OffsetDateTime) {
    private var again: Card
    private var hard: Card
    private var good: Card
    private var easy: Card
    private var lastReview: OffsetDateTime = card.lastReview ?: card.due
    private var lastElapsedDays: Long = card.elapsedDays

    init {
        card.elapsedDays =
            if (card.state == State.New) 0 else now.until(card.lastReview, ChronoUnit.DAYS)
        card.lastReview = now
        card.reps += 1
        this.again = card.copy()
        this.hard = card.copy()
        this.good = card.copy()
        this.easy = card.copy()
    }

    fun updateState(state: State): SchedulingCard {
        if (state == State.New) {
            this.again.state = State.Learning
            this.hard.state = State.Learning
            this.good.state = State.Learning
            this.easy.state = State.Review
        } else if (state === State.Learning || state === State.Relearning) {
            this.again.state = state
            this.hard.state = state
            this.good.state = State.Review
            this.easy.state = State.Review
        } else if (state === State.Review) {
            this.again.state = State.Relearning
            this.hard.state = State.Review
            this.good.state = State.Review
            this.easy.state = State.Review
            this.again.lapses += 1;
        }
        return this
    }

    fun schedule(
        now: OffsetDateTime,
        hardInterval: Long,
        goodInterval: Long,
        easyInterval: Long,
    ): SchedulingCard {
        this.again.scheduledDays = 0
        this.hard.scheduledDays = hardInterval
        this.good.scheduledDays = goodInterval
        this.easy.scheduledDays = easyInterval
        this.again.due = now.plusDays(5)
        this.hard.due =
            if (hardInterval > 0) now.plusDays(hardInterval) else now.plusDays(10)
        this.good.due = now.plusDays(goodInterval)
        this.easy.due = now.plusDays(easyInterval)
        return this
    }

    fun record_log(card: Card, now: OffsetDateTime): RecordLog =
        RecordLog(
            mapOf(
                Rating.Again to RecordLogItem(
                    card = again,
                    log = ReviewLog(
                        rating = Rating.Again,
                        state = card.state,
                        due = lastReview,
                        stability = card.stability,
                        difficulty = card.difficulty,
                        elapsedDays = card.elapsedDays,
                        lastElapsedDays = lastElapsedDays,
                        scheduledDays = card.scheduledDays,
                        review = now
                    )
                ),
                Rating.Hard to RecordLogItem(
                    card = hard,
                    log = ReviewLog(
                        rating = Rating.Hard,
                        state = card.state,
                        due = lastReview,
                        stability = card.stability,
                        difficulty = card.difficulty,
                        elapsedDays = card.elapsedDays,
                        lastElapsedDays = lastElapsedDays,
                        scheduledDays = card.scheduledDays,
                        review = now,
                    )
                ),
                Rating.Good to RecordLogItem(
                    card = good,
                    log = ReviewLog(
                        rating = Rating.Good,
                        state = card.state,
                        due = lastReview,
                        stability = card.stability,
                        difficulty = card.difficulty,
                        elapsedDays = card.elapsedDays,
                        lastElapsedDays = lastElapsedDays,
                        scheduledDays = card.scheduledDays,
                        review = now,
                    )
                ),
                Rating.Easy to RecordLogItem(
                    card = easy,
                    log = ReviewLog(
                        rating = Rating.Easy,
                        state = card.state,
                        due = lastReview,
                        stability = card.stability,
                        difficulty = card.difficulty,
                        elapsedDays = card.elapsedDays,
                        lastElapsedDays = lastElapsedDays,
                        scheduledDays = card.scheduledDays,
                        review = now
                    )
                )
            )
        )
}