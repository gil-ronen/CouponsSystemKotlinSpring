package com.example.couponProject.beans

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import javax.persistence.*

@Configuration
@ComponentScan
@Entity
@Table(name = "COMPANIES")
data class Company(@Id @GeneratedValue val id: Int, var name: String, var email: String, var password: String,
                   @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL]) val coupons: MutableList<Coupon>)
{
    constructor(name: String, email: String, password: String, coupons: MutableList<Coupon>) :
            this(0, name, email, password, arrayListOf())


    /**
     * comparison of two companies
     * @param other Any?
     * @return Boolean
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Company

        if (id != other.id) return false

        return true
    }

    /**
     * make hash from this company
     * @return Int
     */
    override fun hashCode(): Int {
        return id
    }


}