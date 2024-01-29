package com.github.mpps.fsrs.algorithm

import com.github.mpps.fsrs.model.Rating
import com.github.mpps.fsrs.scheduler.SchedulingCard
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.*
import kotlin.random.Random

class FSRSAlgorithm(
    private val parameters: FSRSParameters
) {

    private val internalModifier: Double = (parameters.requestRetention.pow(1 / DECAY) - 1) / FACTOR


    fun initDifficultiesAndStabilities(s: SchedulingCard) {
        s.again.difficulty = this.initDifficulty(Rating.Again)
        s.again.stability = this.initStability(Rating.Again)
        s.hard.difficulty = this.initDifficulty(Rating.Hard)
        s.hard.stability = this.initStability(Rating.Hard)
        s.good.difficulty = this.initDifficulty(Rating.Good)
        s.good.stability = this.initStability(Rating.Good)
        s.easy.difficulty = this.initDifficulty(Rating.Easy)
        s.easy.stability = this.initStability(Rating.Easy)
    }

    fun nextDs(
        s: SchedulingCard,
        lastD: Double,
        lastS: Double,
        retrievability: Double,
    ) {
        s.again.difficulty = this.nextDifficulty(lastD, Rating.Again);
        s.again.stability = this.nextForgetStability(
            lastD,
            lastS,
            retrievability,
        );
        s.hard.difficulty = this.nextDifficulty(lastD, Rating.Hard);
        s.hard.stability = this.nextRecallStability(
            lastD,
            lastS,
            retrievability,
            Rating.Hard,
        );
        s.good.difficulty = this.nextDifficulty(lastD, Rating.Good);
        s.good.stability = this.nextRecallStability(
            lastD,
            lastS,
            retrievability,
            Rating.Good,
        );
        s.easy.difficulty = this.nextDifficulty(lastD, Rating.Easy);
        s.easy.stability = this.nextRecallStability(
            lastD,
            lastS,
            retrievability,
            Rating.Easy,
        );
    }

    private fun initDifficulty(rating: Rating): Double {
        return min(
            max(this.parameters.w[4] - (rating.ordinal - 3) * this.parameters.w[5], 1.0),
            10.0,
        );
    }

    private fun initStability(rating: Rating): Double {
        return max(this.parameters.w[rating.ordinal - 1], 0.1);
    }

    private fun nextInterval(s: Double): Double {
        val newInterval = this.applyFuzz(s * internalModifier);
        return min(
            max(Math.round(newInterval).toDouble(), 1.0),
            this.parameters.maximumInterval,
        )
    }

    private fun applyFuzz(ivl: Double): Double {
        if (!this.parameters.enableFuzz || ivl < 2.5) return ivl
        val fuzzFactor = Random.nextDouble()
        val ivl2 = Math.round(ivl)
        val minIvl = max(2, Math.round(ivl2 * 0.95 - 1))
        val maxIvl = Math.round(ivl2 * 1.05 + 1)
        return floor(fuzzFactor * (maxIvl - minIvl + 1) + minIvl)
    }

    private fun nextDifficulty(d: Double, g: Rating): Double {
        val nextD = d - this.parameters.w[6] * (g.ordinal - 3);
        return constrainDifficulty(
            meanReversion(this.parameters.w[4], nextD),
        );
    }

    private fun constrainDifficulty(difficulty: Double): Double {
        return min(
            max(difficulty.toBigDecimal(MathContext(2, RoundingMode.HALF_UP)).toDouble(), 1.0),
            10.0
        )
    }

    private fun meanReversion(init: Double, current: Double): Double {
        return this.parameters.w[7] * init + (1 - this.parameters.w[7]) * current;
    }

    private fun nextRecallStability(d: Double, s: Double, r: Double, g: Rating): Double {
        val hardPenalty = if (Rating.Hard == g) this.parameters.w[15] else 1.0
        val easyBound = if (Rating.Easy == g) this.parameters.w[16] else 1.0
        return (
                s *
                        (1 +
                                exp(this.parameters.w[8]) *
                                (11 - d) *
                                s.pow(-this.parameters.w[9]) *
                                (exp((1 - r) * this.parameters.w[10]) - 1) *
                                hardPenalty *
                                easyBound)
                )
    }

    private fun nextForgetStability(d: Double, s: Double, r: Double): Double =
        (this.parameters.w[11] *
                d.pow(-this.parameters.w[12]) *
                ((s + 1).pow(this.parameters.w[13]) - 1) *
                exp((1 - r) * this.parameters.w[14]))
            .toBigDecimal(MathContext(2, RoundingMode.HALF_UP))
            .toDouble()

    private fun forgettingCurve(elapsedDays: Double, stability: Double): Double {
        return (1 + FACTOR * elapsedDays / stability).pow(DECAY)
    }

    companion object {
        const val DECAY: Double = -0.5
        val FACTOR: Double = 0.9.pow(1 / DECAY) - 1
    }
}