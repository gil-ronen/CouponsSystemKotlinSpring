package com.example.couponProject.service.facade

import com.example.couponProject.beans.Company
import com.example.couponProject.beans.Coupon
import com.example.couponProject.beans.Customer
import com.example.couponProject.exceptions.companyExceptions.CompanyDoesntExistException
import com.example.couponProject.exceptions.companyExceptions.CompanyEmailAlreadyExistException
import com.example.couponProject.exceptions.companyExceptions.CompanyNameAlreadyExistException
import com.example.couponProject.exceptions.companyExceptions.CompanyNameIsUnchangeableException
import com.example.couponProject.exceptions.customerExceptions.CustomerDoesntExistException
import com.example.couponProject.exceptions.customerExceptions.CustomerEmailAlreadyExistException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.repo.CompanyRepository
import com.example.couponProject.repo.CouponRepository
import com.example.couponProject.repo.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import javax.transaction.Transactional


@Service
@Transactional
class AdminFacade : ClientFacade() {
    @Autowired
    private lateinit var companyRepo: CompanyRepository
    @Autowired
    private lateinit var customerRepo: CustomerRepository
    @Autowired
    private lateinit var couponRepo: CouponRepository

    /**
     * login to admin user
     * @param email String
     * @param password String
     * @return boolean
     * @throws WrongPasswordException if email or password is incorrect
     */
    @Throws(WrongPasswordException::class)
    override fun login(email: String, password: String): Boolean {
        val adminEmail = "admin@admin.com"
        val adminPassword = "admin"
        return if (email == adminEmail && password == adminPassword) true else {
            throw WrongPasswordException("failed to login! email or password is incorrect!")
        }
    }


    /**
     * add a new company
     * @param company Company
     * @return Company
     * @throws CompanyNameAlreadyExistException You can't add a new company with the same name as an existing company
     * @throws CompanyEmailAlreadyExistException You can't add a new company with the same email as an existing company
     */
    @Throws(CompanyNameAlreadyExistException::class, CompanyEmailAlreadyExistException::class)
    fun addCompany(company: Company): Company {
        if (companyRepo.existsByName(company.name)) { //throw Exception
            throw CompanyNameAlreadyExistException("company name already exist!")
        }
        if (companyRepo.existsByEmail(company.email)) { //throw Exception
            throw CompanyEmailAlreadyExistException("company email already exist!")
        }
        return companyRepo.save(company)
    }

    /**
     * update existing company
     * @param company Company
     * @return Company
     * @throws CompanyDoesntExistException You can't update a company that doesn't exist
     * @throws CompanyNameIsUnchangeableException You can't rename an existing company
     */
    @Throws(CompanyDoesntExistException::class, CompanyEmailAlreadyExistException::class, CompanyNameIsUnchangeableException::class)
    fun updateCompany(company: Company): Company {
        return if (!companyRepo.existsById(company.id)) { //throw Exception
            throw CompanyDoesntExistException("company doesn't exist!")
        } else {
            var comp: Company = companyRepo.getOne(company.id)
            if (comp.name != company.name) { //throw Exception
                throw CompanyNameIsUnchangeableException("can't change company name!")
            } else {
                companyRepo.save(company)
            }
        }
    }

    /**
     * delete existing company
     * @param companyID int
     * @return boolean
     * @throws CompanyDoesntExistException You can't delete a company that doesn't exist
     */
    @Throws(CompanyDoesntExistException::class)
    fun deleteCompany(companyID: Int): Boolean {
        if (!companyRepo.existsById(companyID)) { //throw Exception
            throw CompanyDoesntExistException("company doesn't exist!")
        }
        val allCompanyCoupons: MutableList<Coupon> = companyRepo.getOne(companyID).coupons
        for (coupon in allCompanyCoupons) {
            val customers: MutableList<Customer> = customerRepo.findAll() as MutableList<Customer>
            for (cust in customers) {
                if (cust.coupons.contains(coupon)) {
                    cust.coupons.remove(coupon)
                }
            }
        }
        companyRepo.deleteById(companyID)
        return true
    }

    /**
     * get all companies
     * @return MutableList<Company>
     */
    fun getAllCompanies(): MutableList<Company>
    {
        return companyRepo.findAll()
    }


    /**
     * get one company by company id
     * @param companyID int
     * @return Company
     * @throws CompanyDoesntExistException You can't get a company that doesn't exist
     */
    @Throws(CompanyDoesntExistException::class)
    fun getOneCompany(companyID: Int): Company {
        if (!companyRepo.existsById(companyID)) { //throw Exception
            throw CompanyDoesntExistException("company doesn't exist!")
        }
        return companyRepo.getOne(companyID) //findById
    }

    /**
     * add new customer
     * @param customer Customer
     * @return Customer
     * @throws CustomerEmailAlreadyExistException You can't add a new customer with the same email as an existing customer
     */
    @Throws(CustomerEmailAlreadyExistException::class)
    fun addCustomer(customer: Customer): Customer {
        if (customerRepo.existsByEmail(customer.email)) { //throw Exception
            throw CustomerEmailAlreadyExistException("customer email already exist!")
        }
        return customerRepo.save(customer)
    }

    /**
     * update existing customer
     * @param customer Customer
     * @return Customer
     * @throws CustomerDoesntExistException You can't update a customer that doesn't exist
     */
    @Throws(CustomerEmailAlreadyExistException::class, CustomerDoesntExistException::class)
    fun updateCustomer(customer: Customer): Customer {
        return if (!customerRepo.existsById(customer.id)) { //throw Exception
            throw CustomerDoesntExistException("customer doesn't exist!")
        } else {
            customerRepo.save(customer)
        }
    }


    /**
     * delete existing customer
     * @param customerID int
     * @return boolean
     * @throws CustomerDoesntExistException You can't delete a customer that doesn't exist
     */
    @Throws(CustomerDoesntExistException::class)
    fun deleteCustomer(customerID: Int): Boolean {
        if (!customerRepo.existsById(customerID)) { //throw Exception
            throw CustomerDoesntExistException("customer doesn't exist!")
        }
        val customer: Customer = customerRepo.findById(customerID).get()
        customer.coupons.clear()
        customerRepo.deleteById(customerID)
        return true
    }

    /**
     * get all customers
     * @return MutableList<Customer>
     */
    fun getAllCustomers(): MutableList<Customer>
    {
        return customerRepo.findAll()
    }

    /**
     * get one customer by customer id
     * @param customerID int
     * @return Customer
     * @throws CustomerDoesntExistException You can't get a customer that doesn't exist
     */
    @Throws(CustomerDoesntExistException::class)
    fun getOneCustomer(customerID: Int): Customer {
        if (!customerRepo.existsById(customerID)) { //throw Exception
            throw CustomerDoesntExistException("customer doesn't exist!")
        }
        return customerRepo.getOne(customerID)
    }

}
