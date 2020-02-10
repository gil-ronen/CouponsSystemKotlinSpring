package com.example.couponProject.service.facade

import com.example.couponProject.beans.Category
import com.example.couponProject.beans.Coupon
import com.example.couponProject.beans.Customer
import com.example.couponProject.exceptions.couponExceptions.CouponHasExpiredException
import com.example.couponProject.exceptions.couponExceptions.CouponOutOfStockException
import com.example.couponProject.exceptions.couponExceptions.CustomerPurchasedThisCouponYetException
import com.example.couponProject.exceptions.customerExceptions.CustomerDoesntExistException
import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.repo.CouponRepository
import com.example.couponProject.repo.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.transaction.Transactional



@Service
@Transactional
class CustomerFacade : ClientFacade() {

    private var customerID: Int = 0
    @Autowired
    private lateinit var customerRepo: CustomerRepository
    @Autowired
    private lateinit var couponRepo: CouponRepository

    /**
     * login to customer user
     * @param email String
     * @param password String
     * @return boolean
     * @throws EmailNotFoundException if customer email not found
     * @throws WrongPasswordException if customer password is incorrect
     */
    @Throws(EmailNotFoundException::class, WrongPasswordException::class)
    override fun login(email: String, password: String): Boolean {
        val cust: Customer? = customerRepo.findByEmail(email)
        return if (cust == null) { //throw Exception
            throw EmailNotFoundException("failed to login! customer email not found!")
        } else {
            if (cust.password != password) { //throw Exception
                throw WrongPasswordException("failed to login! password is incorrect!")
            }
            customerID = cust.id
            true
        }
    }

    /**
     * purchase new coupon
     * @param coupon Coupon
     * @return Coupon
     * @throws CustomerDoesntExistException  if customer doesn't exist
     * @throws CouponOutOfStockException if this coupon is out of stock
     * @throws CouponHasExpiredException if this coupon has been expired
     * @throws CustomerPurchasedThisCouponYetException if customer purchased this coupon yet
     */
    @Throws(CustomerDoesntExistException::class, CouponOutOfStockException::class, CouponHasExpiredException::class, CustomerPurchasedThisCouponYetException::class)
    fun purchaseCoupon(coupon: Coupon): Coupon {
        val cust: Customer = customerRepo.findById(customerID).get()
        return if (cust == null) { //throw Exception
            throw CustomerDoesntExistException("customer doesn't exist!")
        } else {
            if (coupon.amount <= 0) { //throw Exception
                throw CouponOutOfStockException("this coupon is out of stock")
            }
            val todayDate = LocalDate.now()
            val expiredDate: LocalDate = coupon.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            if (todayDate.isAfter(expiredDate)) { //throw Exception
                throw CouponHasExpiredException("this coupon has expired")
            }
            val coupons: MutableList<Coupon> = cust.coupons
            if (coupons.contains(coupon)) { //throw Exception
                throw CustomerPurchasedThisCouponYetException("customer purchased this coupon yet!")
            }
            coupon.amount--
            coupons.add(coupon)
            customerRepo.saveAndFlush(cust)
            coupon
        }
    }

    /**
     * get all coupons of this customer
     * @return MutableList<Coupon>
    */
    fun getCustomerCoupons(): MutableList<Coupon> {
        return customerRepo.findById(customerID).get().coupons
    }

    /**
     * get all coupons of this customer by category
     * @param category Category
     * @return MutableList<Coupon>
     */
    fun getCustomerCoupons(category: Category): MutableList<Coupon> {
        val coupons: MutableList<Coupon> = getCustomerCoupons()
        val filteredCategoryCoupons: MutableList<Coupon> = ArrayList<Coupon>()
        for (c in coupons) {
            if (c.category == category) {
                filteredCategoryCoupons.add(c)
            }
        }
        return filteredCategoryCoupons
    }

    /**
     * get all coupons of this customer by less then equal to max price limit
     * @param maxPrice double
     * @return MutableList<Coupon>
    */
    fun getCustomerCoupons(maxPrice: Double): MutableList<Coupon> {
        val coupons: MutableList<Coupon> = getCustomerCoupons()
        val filteredCategoryCoupons: MutableList<Coupon> = ArrayList<Coupon>()
        for (c in coupons) {
            if (c.price <= maxPrice) {
                filteredCategoryCoupons.add(c)
            }
        }
        return filteredCategoryCoupons
    }

    /**
     * get customer details
     * @return Customer
     */
    fun getCustomerDetails(): Customer
    {
        return customerRepo.findById(customerID).get()
    }


    /**
     * get all coupons that exist in the system
     * @return MutableList<Coupon>
    */
    fun getAllCouponsInTheSystem(): MutableList<Coupon>
    {
        return couponRepo.findAll()
    }
}