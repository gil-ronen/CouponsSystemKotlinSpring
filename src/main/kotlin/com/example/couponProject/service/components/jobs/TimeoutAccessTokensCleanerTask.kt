package com.example.couponProject.service.components.jobs

import com.example.couponProject.service.components.loginManager.TokensManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service



@Service
class TimeoutAccessTokensCleanerTask {
    @Autowired
    private lateinit var tokensManager: TokensManager
    private var jobEnabled = false
    /**
     * start the cleanTimeoutAccessTokensTask job
     */
    fun startJob() {
        jobEnabled = true
    }

    /**
     * stop the cleanTimeoutAccessTokensTask job
     */
    fun stopJob() {
        jobEnabled = false
    }

    /**
     * delete all the timeout access tokens from the TokensManager list every minute
     * job is invoked every minute
     */
    @Scheduled(fixedRate = 1000 * 60)
    fun cleanTimeoutAccessTokensTask() {
        if (jobEnabled) { // CLEAN TIME OUT ACCESS TOKENS:
            tokensManager.removeAllExpiredAccessTokens()
        }
    }
}