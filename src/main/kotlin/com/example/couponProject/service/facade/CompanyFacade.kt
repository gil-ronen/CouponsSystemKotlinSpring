package com.example.couponProject.service.facade

import com.example.couponProject.beans.Category
import com.example.couponProject.beans.Company
import com.example.couponProject.beans.Coupon
import com.example.couponProject.beans.Customer
import com.example.couponProject.exceptions.companyExceptions.CompanyDoesntExistException
import com.example.couponProject.exceptions.couponExceptions.CompanyIdInCouponIsUnchangeableException
import com.example.couponProject.exceptions.couponExceptions.CouponDoesntExistException
import com.example.couponProject.exceptions.couponExceptions.CouponTitleAlreadyExistInThisCompanyException
import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.repo.CompanyRepository
import com.example.couponProject.repo.CouponRepository
import com.example.couponProject.repo.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
@Transactional
class CompanyFacade: ClientFacade() {

    private var companyID: Int = 0
    @Autowired
    private lateinit var companyRepo: CompanyRepository
    @Autowired
    private lateinit var couponRepo: CouponRepository
    @Autowired
    private lateinit var customerRepo: CustomerRepository


    /**
     * login to company user
     * @param email String
     * @param password String
     * @return boolean
     * @throws EmailNotFoundException if company email not found
     * @throws WrongPasswordException if company password is incorrect
     */
    @Throws(EmailNotFoundException::class, WrongPasswordException::class)
    override fun login(email: String, password: String): Boolean {
        val comp: Company = companyRepo.findByEmail(email)
        return if (comp == null) { //throw Exception
            throw EmailNotFoundException("failed to login! company email not found!")
        } else {
            if (comp.password != password) { //throw Exception
                throw WrongPasswordException("failed to login! password is incorrect!")
            }
            companyID = comp.id
            true
        }
    }

    /**
     * add a new coupon
     * @param coupon Coupon
     * @return Coupon
     * @throws CompanyDoesntExistException If you try to add a coupon to a company that doesn't exist
     * @throws CouponTitleAlreadyExistInThisCompanyException A new coupon with the same title can't be added to an existing coupon from the same company
     */
    @Throws(CompanyDoesntExistException::class, CouponTitleAlreadyExistInThisCompanyException::class)
    fun addCoupon(coupon: Coupon): Coupon {
        val comp: Company = companyRepo.getOne(companyID)
        return if (comp == null) { //throw Exception

            throw CompanyDoesntExistException("company doesn't exist!")
        } else {
            coupon.companyID = companyID //if this company tries to add coupon to another company
            for (coup in comp.coupons) {
                if (coup.title == coupon.title) { //throw Exception
                    throw CouponTitleAlreadyExistInThisCompanyException("coupon title is already exist!")
                }
            }
            comp.coupons.add(coupon)
            companyRepo.save(comp)
            coupon
        }
    }

    /**
     * update existing coupon
     * @param coupon Coupon
     * @return Coupon
     * @throws CouponDoesntExistException You can't update a coupon that doesn't exist
     * @throws CompanyIdInCouponIsUnchangeableException You can't update a company id in coupon
     * @throws CouponTitleAlreadyExistInThisCompanyException A coupon with the same title can't be updated to an existing coupon title from the same company
     */
    @Throws(CouponDoesntExistException::class, CompanyIdInCouponIsUnchangeableException::class, CouponTitleAlreadyExistInThisCompanyException::class)
    fun updateCoupon(coupon: Coupon): Coupon? {
        return if (!couponRepo.existsById(coupon.id)) { //throw Exception
            throw CouponDoesntExistException("coupon doesn't exist!")
        } else {
            if (coupon.companyID != companyID) { //throw Exception
                throw CompanyIdInCouponIsUnchangeableException("can't change company ID in exist coupon!")
            } else {
                val coupons: MutableList<Coupon> = getCompanyCoupons()
                //checks if coupon title is already exist
                for (coup in coupons) {
                    if (coup != coupon && coup.title == coupon.title) { //throw Exception
                        throw CouponTitleAlreadyExistInThisCompanyException("coupon title is already exist!")
                    }
                }
                for (coup in coupons) {
                    if (coup.id == coupon.id) {
                        return couponRepo.save(coupon)
                    }
                }
                null
            }
        }
    }

    /**
     * delete existing coupon
     * @param couponID Int
     * @return boolean
     * @throws CouponDoesntExistException You can't delete a coupon that doesn't exist
     */
    @Throws(CouponDoesntExistException::class)
    fun deleteCoupon(couponID: Int): Boolean {
        if (!couponRepo.existsById(couponID)) { //throw Exception
            throw CouponDoesntExistException("coupon doesn't exist!")
        }
        val coupon: Coupon = couponRepo.getOne(couponID)
        //Step 1: delete coupon from all customers that purchased it
        val customersId: MutableList<Int> = customerRepo.getAllCustomersIdByCouponId(couponID) as MutableList<Int>
        for (id in customersId) {
            val customer: Customer = customerRepo.findById(id).get()
            customer.coupons.remove(coupon)
        }
        //Step 2: delete coupon from the company that created it
        val company: Company = companyRepo.findById(coupon.companyID).get() as Company
        company.coupons.remove(coupon)
        //Step 3: delete coupon from the coupons repository
        couponRepo.deleteById(couponID)
        return true
    }

    /**
     * get all company coupons
     * @return MutableList<Coupon>
    */
    fun getCompanyCoupons(): MutableList<Coupon>{
        return couponRepo.findCouponsByCompanyID(companyID) as MutableList<Coupon>
    }


    /**
     * get all company coupons by category
     * @param category Category
     * @return MutableList<Coupon>
     */
    fun getCompanyCoupons(category: Category): MutableList<Coupon> {
        return couponRepo.findByCompanyIDAndCategory(companyID, category) as MutableList<Coupon>
    }

    /**
     * get all company coupons by less then equal to max price limit
     * @param maxPrice double
     * @return MutableList<Coupon>
    */
    fun getCompanyCoupons(maxPrice: Double): MutableList<Coupon> {
        return couponRepo.findCouponsByCompanyIDAndPriceLessThanEqual(companyID, maxPrice) as MutableList<Coupon>
    }

    /**
     * get company details
     * @return Company
     */
    fun getCompanyDetails(): Company
    {
        return companyRepo.findById(companyID).get()
    }

}