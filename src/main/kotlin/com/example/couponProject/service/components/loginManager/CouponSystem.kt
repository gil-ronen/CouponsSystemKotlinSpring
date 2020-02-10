package com.example.couponProject.service.components.loginManager

import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.service.components.jobs.CouponCleanerDailyTask
import com.example.couponProject.service.components.jobs.TimeoutAccessTokensCleanerTask
import com.example.couponProject.service.facade.AdminFacade
import com.example.couponProject.service.facade.ClientFacade
import com.example.couponProject.service.facade.CompanyFacade
import com.example.couponProject.service.facade.CustomerFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@ComponentScan
@Service
class CouponSystem {
    @Autowired
    private lateinit var adminFacade: AdminFacade
    @Autowired
    private lateinit var companyFacade: CompanyFacade
    @Autowired
    private lateinit var customerFacade: CustomerFacade
    @Autowired
    private lateinit var couponCleanerDailyTask: CouponCleanerDailyTask
    @Autowired
    private lateinit var timeoutAccessTokensCleanerTask: TimeoutAccessTokensCleanerTask

    /**
     * the main login to the coupons system
     * @param email String
     * @param password String
     * @param clientType ClientType
     * @return ClientFacade
     */
    fun login(email: String, password: String, clientType: ClientType): ClientFacade? {
        return when (clientType) {
            ClientType.Administrator -> {
                try {
                    adminFacade.login(email, password)
                } catch (e: WrongPasswordException) {
                    return null
                }
                adminFacade
            }
            ClientType.Company -> {
                try {
                    companyFacade.login(email, password)
                } catch (e: EmailNotFoundException) {
                    return null
                } catch (e: WrongPasswordException) {
                    return null
                }
                companyFacade
            }
            ClientType.Customer -> {
                try {
                    customerFacade.login(email, password)
                } catch (e: EmailNotFoundException) {
                    return null
                } catch (e: WrongPasswordException) {
                    return null
                }
                customerFacade
            }
            else -> null
        }
    }

    /**
     * invoke the CouponCleanerDailyTask
     * invoke the TimeoutAccessTokensCleanerTask
     */
    @PostConstruct
    fun init() { //coupon cleaner daily task invoked
        couponCleanerDailyTask.startJob()
        //clean time out access tokens task invoked
        timeoutAccessTokensCleanerTask.startJob()
    }

    /**
     * stop the CouponCleanerDailyTask
     * stop the TimeoutAccessTokensCleanerTask
     */
    @PreDestroy
    fun destroy() { //coupon cleaner daily task is over
        couponCleanerDailyTask.stopJob()
        //clean time out access tokens task is over
        timeoutAccessTokensCleanerTask.stopJob()
    }
}