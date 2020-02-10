package com.example.couponProject.repo
import com.example.couponProject.beans.Category
import com.example.couponProject.beans.Coupon
import org.springframework.data.jpa.repository.JpaRepository



interface CouponRepository : JpaRepository<Coupon, Int> {
    /**
     * Looking for coupons by company id
     * @param companyID int
     * @return MutableList<Coupon>
    </Coupon> */
    fun findCouponsByCompanyID(companyID: Int): MutableList<Coupon>

    /**
     * Looking for coupons by company id and category
     * @param companyID int
     * @param category Category
     * @return MutableList<Coupon>
    </Coupon> */
    fun findByCompanyIDAndCategory(companyID: Int, category: Category): MutableList<Coupon>

    /**
     * Looking for coupons by company id and price less then equal to max price limit
     * @param companyID int
     * @param price double
     * @return MutableList<Coupon>
    </Coupon> */
    fun findCouponsByCompanyIDAndPriceLessThanEqual(companyID: Int, price: Double): MutableList<Coupon>
}