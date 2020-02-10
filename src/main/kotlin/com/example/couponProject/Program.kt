package com.example.couponProject

import com.example.couponProject.beans.Category
import com.example.couponProject.beans.Company
import com.example.couponProject.beans.Coupon
import com.example.couponProject.beans.Customer
import com.example.couponProject.service.components.loginManager.ClientType
import com.example.couponProject.service.components.loginManager.CouponSystem
import com.example.couponProject.service.facade.AdminFacade
import com.example.couponProject.service.facade.CompanyFacade
import com.example.couponProject.service.facade.CustomerFacade
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
class Program

fun main(args: Array<String>) {

    val ctx = runApplication<Program>(*args)

    try {
        println("GO!")
        val couponSystem: CouponSystem = ctx.getBean(CouponSystem::class.java)
        // invoke the daily job
        couponSystem.init()
        //======================================================================================================================
        // Creating an AdminFacade object and calling all business logic functions
        val adminFacade = couponSystem.login("admin@admin.com", "admin", ClientType.Administrator) as AdminFacade

        println("\n____________________COMPANIES____________________\n")
        val company1 = Company("Coca Cola", "c@c.com", "1234", arrayListOf())
        val company2 = Company("Tnuva", "t@t.com", "1234", arrayListOf())
        val company3 = Company("Rami Levi", "r@r.com", "4444", arrayListOf())
        val company4 = Company("ShuferSal", "s@s.com", "1234", arrayListOf())
        adminFacade.addCompany(company1)
        adminFacade.addCompany(company2)
        adminFacade.addCompany(company3)
        adminFacade.addCompany(company4)
        val updatedCompany = adminFacade.getOneCompany(company3.id)
        updatedCompany.email = "e@e.com"
        updatedCompany.password = "1234"
        adminFacade.updateCompany(updatedCompany)
        val deletedCompany = adminFacade.getOneCompany(company4.id)
        adminFacade.deleteCompany(deletedCompany.id)
        val companies: Collection<Company> = adminFacade.getAllCompanies()
        for (c in companies)
            println(c.toString())

        println("\n____________________CUSTOMERS____________________\n")
        val customer1 = Customer("Yossi", "Benayun", "e1@e.com", "1234", arrayListOf())
        val customer2 = Customer("Leo", "Messi", "e123@e.com", "1234", arrayListOf())
        val customer3 = Customer("Binyamin", "Netanyahu", "e3@e.com", "1234", arrayListOf())
        adminFacade.addCustomer(customer1)
        adminFacade.addCustomer(customer2)
        adminFacade.addCustomer(customer3)
        val updatedCustomer = adminFacade.getOneCustomer(customer2.id)
        updatedCustomer.firstName = "Gil"
        updatedCustomer.lastName = "Ronen"
        updatedCustomer.email = "e2@e.com"
        adminFacade.updateCustomer(updatedCustomer)
        val deletedCustomer = adminFacade.getOneCustomer(customer3.id)
        adminFacade.deleteCustomer(deletedCustomer.id)
        val customers: Collection<Customer> = adminFacade.getAllCustomers()
        for (c in customers)
            println(c.toString())

        //======================================================================================================================
        // Creating CompanyFacade objects and calling all business logic functions
        val dateStart = LocalDate.now()
        val dateEnd = dateStart.plusDays(7)
        val date0 = Date.from(dateStart.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        val date1 = Date.from(dateEnd.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        //=====COMPANY 1=====//
        var companyFacade = couponSystem.login("c@c.com", "1234", ClientType.Company) as CompanyFacade
        val coupon1 = Coupon(companyFacade.getCompanyDetails().id, Category.Restaurant, "Hamburger meal", "includes hamburger, drink and potatoes fries", date0, date1, 20, 2.4, "https://picsum.photos/200/300")
        val coupon2 = Coupon(companyFacade.getCompanyDetails().id, Category.Electricity, "Samsung TV 56\"", "Smart TV Full HD", date0, date1, 760, 4.3, "https://picsum.photos/200/300")
        companyFacade.addCoupon(coupon1)
        companyFacade.addCoupon(coupon2)

        println("\n========================================\n")
        println("\n___________Company 1 details:___________\n")
        println(companyFacade.getCompanyDetails())
        println("\n___________Company 1 coupons:___________\n")
        val company1coupons: Collection<Coupon> = companyFacade.getCompanyCoupons()
        for (c in company1coupons) println(c.toString())
        println("\n___Company 1 coupons by Electricity Category:___\n")
        for (c in companyFacade.getCompanyCoupons(Category.Electricity)) println(c.toString())
        println("\n______Company 1 coupons by max price 4.0:______\n")
        for (c in companyFacade.getCompanyCoupons(4.0)) println(c.toString())

        //=====COMPANY 2=====//
        companyFacade = couponSystem.login("t@t.com", "1234", ClientType.Company) as CompanyFacade
        val coupon3 = Coupon(companyFacade.getCompanyDetails().id, Category.Restaurant, "Pizza", "One larg size pizza with 2 topi's ", date0, date1, 60, 7.8, "https://picsum.photos/200/300")
        val coupon4 = Coupon(companyFacade.getCompanyDetails().id, Category.Vacation, "Thailand trip", "Book now your next vacation to Thailand!", date0, date1, 70, 9.8, "https://picsum.photos/200/300")
        val coupon5 = Coupon(companyFacade.getCompanyDetails().id, Category.Food, "50% off Italian food", "Buy Italian Food for 50% discount!", date0, date1, 30, 4.1, "https://picsum.photos/200/300")
        companyFacade.addCoupon(coupon3)
        companyFacade.addCoupon(coupon4)
        companyFacade.addCoupon(coupon5)

        println("\n========================================\n")
        println("\n___________Company 2 details:___________\n")
        println(companyFacade.getCompanyDetails())
        println("\n___________Company 2 coupons:___________\n")
        val company2coupons: Collection<Coupon> = companyFacade.getCompanyCoupons()
        for (c in company2coupons) println(c.toString())
        println("\n___Company 2 coupons by Restaurant Category:___\n")
        for (c in companyFacade.getCompanyCoupons(Category.Restaurant)) println(c.toString())
        println("\n______Company 2 coupons by max price 9.0:______\n")
        for (c in companyFacade.getCompanyCoupons(9.0)) println(c.toString())

        //=====COMPANY 3=====//
        companyFacade = couponSystem.login("e@e.com", "1234", ClientType.Company) as CompanyFacade
        val tempCoupon = Coupon(companyFacade.getCompanyDetails().id, Category.Food, "70% off Meat food", "Buy Beef and Chicken Meat for 70% discount!", date0, date1, 30, 4.1, "https://picsum.photos/200/300")
        val coupon6 = Coupon(companyFacade.getCompanyDetails().id, Category.Vacation, "Greece trip", "Book now your next vacation to Greece!", date0, date1, 20, 4.3, "https://picsum.photos/200/300")
        val coupon7 = Coupon(companyFacade.getCompanyDetails().id, Category.Restaurant, "McDonald's Meal", "Hamburger meal", date0, date1, 60, 7.8, "https://picsum.photos/200/300")
        val coupon8 = Coupon(companyFacade.getCompanyDetails().id, Category.Vacation, "Cyprus trip", "Book now your next vacation to Cyprus!", date0, date1, 70, 9.8, "https://picsum.photos/200/300")
        companyFacade.addCoupon(tempCoupon)
        companyFacade.addCoupon(coupon6)
        companyFacade.addCoupon(coupon7)
        companyFacade.addCoupon(coupon8)
        var coupons: Collection<Coupon> = companyFacade.getCompanyCoupons()
        val couponToDelete = coupons.iterator().next()
        companyFacade.deleteCoupon(couponToDelete.id)
        coupons = companyFacade.getCompanyCoupons()
        val couponToUpdate = coupons.iterator().next()
        couponToUpdate.title = "London trip"
        couponToUpdate.description = "Book now your next vacation to London!"
        couponToUpdate.image = "https://i.picsum.photos/id/1027/200/300.jpg"
        companyFacade.updateCoupon(couponToUpdate)

        println("\n========================================\n")
        println("\n___________Company 3 details:___________\n")
        println(companyFacade.getCompanyDetails())
        println("\n___________Company 3 coupons:___________\n")
        val company3coupons: Collection<Coupon> = companyFacade.getCompanyCoupons()
        for (c in company3coupons) println(c.toString())
        println("\n___Company 3 coupons by Vacation Category:___\n")
        for (c in companyFacade.getCompanyCoupons(Category.Vacation)) println(c.toString())
        println("\n______Company 3 coupons by max price 8.0:______\n")
        for (c in companyFacade.getCompanyCoupons(8.0)) println(c.toString())

        //======================================================================================================================
        // Creating CustomerFacade objects and calling all business logic functions

        //=====CUSTOMER 1=====//
        var customerFacade = couponSystem.login("e1@e.com", "1234", ClientType.Customer) as CustomerFacade
        for (c in company1coupons) customerFacade.purchaseCoupon(c)
        var i = 1
        for (c in company2coupons) {
            if (i % 2 == 0) customerFacade.purchaseCoupon(c)
            i++
        }
        i = 0
        for (c in company3coupons) {
            if (i % 2 == 0) customerFacade.purchaseCoupon(c)
            i++
        }

        println("\n========================================\n")
        println("\n___________Customer 1 details:___________\n")
        println(customerFacade.getCustomerDetails())
        println("\n___________Customer 1 coupons:___________\n")
        for (c in customerFacade.getCustomerCoupons()) println(c.toString())
        println("\n___Customer 1 coupons by Vacation Restaurant:___\n")
        for (c in customerFacade.getCustomerCoupons(Category.Restaurant)) println(c.toString())
        println("\n______Customer 1 coupons by max price 4.5:______\n")
        for (c in customerFacade.getCustomerCoupons(4.5)) println(c.toString())

        //=====CUSTOMER 2=====//
        customerFacade = couponSystem.login("e2@e.com", "1234", ClientType.Customer) as CustomerFacade
        for (c in company1coupons) customerFacade.purchaseCoupon(c)
        i = 0
        for (c in company2coupons) {
            if (i % 2 == 0) customerFacade.purchaseCoupon(c)
            i++
        }
        i = 1
        for (c in company3coupons) {
            if (i % 2 == 0) customerFacade.purchaseCoupon(c)
            i++
        }
        println("\n========================================\n")
        println("\n___________Customer 2 details:___________\n")
        println(customerFacade.getCustomerDetails())
        println("\n___________Customer 2 coupons:___________\n")
        for (c in customerFacade.getCustomerCoupons()) println(c.toString())
        println("\n___Customer 2 coupons by Vacation Restaurant:___\n")
        for (c in customerFacade.getCustomerCoupons(Category.Restaurant)) println(c.toString())
        println("\n______Customer 2 coupons by max price 4.5:______\n")
        for (c in customerFacade.getCustomerCoupons(4.5)) println(c.toString())

        //======================================================================================================================
        // stop the daily job
        couponSystem.destroy()
    } catch (e: Exception) {
        println(e.message)
    }
}





