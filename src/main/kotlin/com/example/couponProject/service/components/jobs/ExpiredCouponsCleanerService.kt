package com.example.couponProject.service.components.jobs


import com.example.couponProject.beans.Company
import com.example.couponProject.beans.Coupon
import com.example.couponProject.exceptions.couponExceptions.CouponDoesntExistException
import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.service.facade.AdminFacade
import com.example.couponProject.service.facade.CompanyFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId



@Service
class ExpiredCouponsCleanerService {
    @Autowired
    private lateinit var adminFacade: AdminFacade
    @Autowired
    private lateinit var companyFacade: CompanyFacade

    /**
     * delete all the expired coupons from the repository
     */
    fun clean() {
        val companies: MutableList<Company> = adminFacade.getAllCompanies() as MutableList<Company>
        for (comp in companies) {
            try {
                if (companyFacade.login(comp.email, comp.password)) {
                    val coupons: MutableList<Coupon> = companyFacade.getCompanyCoupons()
                    for (coup in coupons) {
                        val todayDate = LocalDate.now()
                        val expiredDate: LocalDate = coup.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        if (todayDate.isAfter(expiredDate)) {
                            try {
                                companyFacade.deleteCoupon(coup.id)
                            } catch (e: CouponDoesntExistException) {
                                e.printStackTrace()
                                continue
                            }
                        }
                    }
                }
            } catch (e: EmailNotFoundException) {
                e.printStackTrace()
                continue
            } catch (e: WrongPasswordException) {
                e.printStackTrace()
                continue
            }
        }
    }
}