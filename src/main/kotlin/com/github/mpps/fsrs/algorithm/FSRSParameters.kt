package com.github.mpps.fsrs.algorithm

data class FSRSParameters(
    val requestRetention: Double = DEFAULT_REQUEST_RETENTION,
    val maximumInterval: Double = DEFAULT_MAXIMUM_INTERVAL,
    val w: List<Double> = DEFAULT_W,
    val enableFuzz: Boolean = DEFAULT_ENABLE_FUZZ
) {

    companion object {
        const val DEFAULT_REQUEST_RETENTION = 0.9
        const val DEFAULT_MAXIMUM_INTERVAL = 36500.0
        val DEFAULT_W = listOf(
            0.4, 0.6, 2.4, 5.8, 4.93, 0.94, 0.86, 0.01, 1.49, 0.14, 0.94, 2.18, 0.05,
            0.34, 1.26, 0.29, 2.61,
        )
        const val DEFAULT_ENABLE_FUZZ = false
    }
}