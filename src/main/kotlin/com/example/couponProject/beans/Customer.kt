package com.example.couponProject.beans

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import javax.persistence.*

@Configuration
@ComponentScan
@Entity
@Table(name = "CUSTOMERS")
data class Customer(@Id @GeneratedValue val id: Int, var firstName: String, var lastName: String, var email: String, var password: String,
                    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL]) @JoinTable(name = "customers_coupons")val coupons: MutableList<Coupon>)
{
    constructor(firstName: String, lastName: String, email: String, password: String, coupons: MutableList<Coupon>):
            this(0, firstName, lastName, email, password, arrayListOf())


    /**
     * comparison of two customers
     * @param other Any?
     * @return Boolean
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer

        if (id != other.id) return false

        return true
    }

    /**
     * make hash from this customer
     * @return Int
     */
    override fun hashCode(): Int {
        return id
    }


}