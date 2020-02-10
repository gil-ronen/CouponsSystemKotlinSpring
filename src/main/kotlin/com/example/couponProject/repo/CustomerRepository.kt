package com.example.couponProject.repo

import com.example.couponProject.beans.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface CustomerRepository : JpaRepository<Customer, Int> {
    /**
     * Checks whether a customer exists in the repository by email
     * @param email String
     * @return Boolean
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Looking for a customer in the repository by email
     * @param email String
     * @return Customer
     */
    fun findByEmail(email: String): Customer

    /**
     * Looking for all customers id's in the repository that purchased a specific coupon by coupon id
     * @param couponId int
     * @return Collection<Integer>
    </Integer> */
    @Query(value = "SELECT customer_id FROM coupons_system.customers_coupons WHERE coupons_id=?1", nativeQuery = true)
    fun getAllCustomersIdByCouponId(couponId: Int): MutableList<Int>?
}