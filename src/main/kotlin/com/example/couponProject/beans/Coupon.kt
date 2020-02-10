package com.example.couponProject.beans

import javax.persistence.*
import java.util.Date


@Entity
@Table(name = "COUPONS")
data class Coupon(@Id @GeneratedValue var id: Int, var companyID: Int, @Enumerated(EnumType.STRING) var category: Category, var title: String,
                  var description: String, var startDate: Date, var endDate: Date ,var amount: Int, var price: Double, var image: String)

{
    constructor(companyID: Int, category: Category,  title: String, description: String,  startDate: Date,  endDate: Date , amount: Int,  price: Double,  image: String):
            this(0, companyID, category, title,  description,  startDate, endDate, amount,  price,  image)


    /**
     * comparison of two coupons
     * @param other Any?
     * @return Boolean
     */
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as Coupon

        if (id != other.id) return false

        return true
    }

    /**
     * make hash from this coupon
     * @return Int
     */
    override fun hashCode(): Int {
        return id
    }
}