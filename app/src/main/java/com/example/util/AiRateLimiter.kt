package com.example.util

import java.util.concurrent.ConcurrentLinkedQueue

sealed class RateLimitResult {
    object Allowed : RateLimitResult()
    data class ExceededPerMinute(val waitSeconds: Long) : RateLimitResult()
    data class ExceededDailyFreeLimit(val dailyMax: Int) : RateLimitResult()
}

object AiRateLimiter {
    private const val FREE_DAILY_LIMIT = 15
    private const val FREE_PER_MINUTE_LIMIT = 5
    private const val PREMIUM_PER_MINUTE_LIMIT = 30

    private val requestTimestamps = ConcurrentLinkedQueue<Long>()
    private var dailyQueryCount = 0
    private var lastRecordedDay = getCurrentDayOfYear()

    @Synchronized
    fun canMakeAiRequest(isPremium: Boolean): RateLimitResult {
        val currentDay = getCurrentDayOfYear()
        if (currentDay != lastRecordedDay) {
            dailyQueryCount = 0
            lastRecordedDay = currentDay
        }

        val now = System.currentTimeMillis()
        val oneMinuteAgo = now - 60_000

        // Clean up timestamps older than 60 seconds
        while (requestTimestamps.isNotEmpty() && requestTimestamps.peek() != null && requestTimestamps.peek()!! < oneMinuteAgo) {
            requestTimestamps.poll()
        }

        val minuteLimit = if (isPremium) PREMIUM_PER_MINUTE_LIMIT else FREE_PER_MINUTE_LIMIT
        if (requestTimestamps.size >= minuteLimit) {
            val oldest = requestTimestamps.peek() ?: now
            val waitSecs = ((oldest + 60_000 - now) / 1000).coerceAtLeast(1)
            return RateLimitResult.ExceededPerMinute(waitSecs)
        }

        if (!isPremium && dailyQueryCount >= FREE_DAILY_LIMIT) {
            return RateLimitResult.ExceededDailyFreeLimit(FREE_DAILY_LIMIT)
        }

        return RateLimitResult.Allowed
    }

    @Synchronized
    fun recordAiRequest() {
        val now = System.currentTimeMillis()
        requestTimestamps.add(now)
        dailyQueryCount++
    }

    fun getDailyRemaining(isPremium: Boolean): Int {
        if (isPremium) return 9999
        return (FREE_DAILY_LIMIT - dailyQueryCount).coerceAtLeast(0)
    }

    private fun getCurrentDayOfYear(): Int {
        val cal = java.util.Calendar.getInstance()
        return cal.get(java.util.Calendar.DAY_OF_YEAR)
    }
}
