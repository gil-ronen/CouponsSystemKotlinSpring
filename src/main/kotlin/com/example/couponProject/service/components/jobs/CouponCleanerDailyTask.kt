package com.example.couponProject.service.components.jobs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class CouponCleanerDailyTask {
    @Autowired
    private lateinit var cleanerService: ExpiredCouponsCleanerService
    private var jobEnabled = false
    /**
     * start the cleanExpiredCouponsDailyTask job
     */
    fun startJob() {
        jobEnabled = true
    }

    /**
     * stop the cleanExpiredCouponsDailyTask job
     */
    fun stopJob() {
        jobEnabled = false
    }

    /**
     * daily task that delete all the expired coupons from the repository
     * job is invoked every day at 24:00:01 (cron pattern: second, minute, hour, day, month, weekday)
     */
    @Scheduled(cron = "1 0 0 * * *")
    fun cleanExpiredCouponsDailyTask() {
        if (jobEnabled) { // CLEAN EXPIRED COUPONS:
            cleanerService.clean()
        }
    }
}