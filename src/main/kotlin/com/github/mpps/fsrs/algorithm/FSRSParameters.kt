package com.github.mpps.fsrs.algorithm

data class FSRSParameters(
    val requestRetention: Double,
    val maximumInterval: Double,
    val w: List<Double>,
    val enableFuzz: Boolean
)