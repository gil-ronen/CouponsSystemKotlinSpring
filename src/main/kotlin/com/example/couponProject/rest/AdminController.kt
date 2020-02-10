package com.example.couponProject.rest


import com.example.couponProject.beans.Company
import com.example.couponProject.beans.Customer
import com.example.couponProject.exceptions.companyExceptions.CompanyDoesntExistException
import com.example.couponProject.exceptions.companyExceptions.CompanyEmailAlreadyExistException
import com.example.couponProject.exceptions.companyExceptions.CompanyNameAlreadyExistException
import com.example.couponProject.exceptions.companyExceptions.CompanyNameIsUnchangeableException
import com.example.couponProject.exceptions.customerExceptions.CustomerDoesntExistException
import com.example.couponProject.exceptions.customerExceptions.CustomerEmailAlreadyExistException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.service.components.loginManager.ClientType
import com.example.couponProject.service.components.loginManager.TokensManager
import com.example.couponProject.service.facade.AdminFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("Admin")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class AdminController : ClientController() {

    @Autowired
    private lateinit var adminService: AdminFacade
    @Autowired
    private lateinit var tokensManager: TokensManager

    /**
     * login to admin user - NOT IN USE
     * @param email String
     * @param password String
     * @return ResponseEntity<>
     */
    override fun login(email: String, password: String): ResponseEntity<*> {
        return try {
            ResponseEntity<Boolean>(adminService.login(email, password), HttpStatus.OK)
        } catch (e: WrongPasswordException) {
            ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * add a new company
     * @param accessToken String
     * @param company Company
     * @return ResponseEntity<>
     */
    @PostMapping("/addCompany/{accessToken}")
    fun addCompany(@RequestBody company: Company, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Company>( adminService.addCompany(company), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CompanyNameAlreadyExistException) {
            ResponseEntity<String>(e.message , HttpStatus.BAD_REQUEST)
        } catch (e: CompanyEmailAlreadyExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * update existing company
     * @param accessToken String
     * @param company Company
     * @return ResponseEntity<>
     */
    @PutMapping("/updateCompany/{accessToken}")
    fun updateCompany(@RequestBody company: Company, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Company> (adminService.updateCompany(company) , HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CompanyDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CompanyEmailAlreadyExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CompanyNameIsUnchangeableException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * delete existing company
     * @param accessToken String
     * @param companyID int
     * @return ResponseEntity<>
     */
    @DeleteMapping("/deleteCompany/{accessToken}")
    fun deleteCompany(@RequestParam companyID: Int, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Boolean>(adminService.deleteCompany(companyID), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CompanyDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * get all companies
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getAllCompanies/{accessToken}")
    fun getAllCompanies(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
            ResponseEntity<Collection<Company>>(adminService.getAllCompanies(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get one company by company id
     * @param accessToken String
     * @param companyID int
     * @return ResponseEntity<>
     */
    @GetMapping("/getOneCompany/{accessToken}")
    fun getOneCompany(@RequestParam companyID: Int, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Company>(adminService.getOneCompany(companyID), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CompanyDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * add new customer
     * @param accessToken String
     * @param customer Customer
     * @return ResponseEntity<>
     */
    @PostMapping("/addCustomer/{accessToken}")
    fun addCustomer(@RequestBody customer: Customer, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Customer>(adminService.addCustomer(customer) , HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CustomerEmailAlreadyExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * update existing customer
     * @param accessToken String
     * @param customer Customer
     * @return ResponseEntity<>
     */
    @PutMapping("/updateCustomer/{accessToken}")
    fun updateCustomer(@RequestBody customer: Customer, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Customer>(adminService.updateCustomer(customer), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CustomerDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CustomerEmailAlreadyExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * delete existing customer
     * @param accessToken String
     * @param customerID int
     * @return ResponseEntity<>
     */
    @DeleteMapping("/deleteCustomer/{accessToken}")
    fun deleteCustomer(@RequestParam customerID: Int, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Boolean>(adminService.deleteCustomer(customerID), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CustomerDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * get all customers
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getAllCustomers/{accessToken}")
    fun getAllCustomers(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
            ResponseEntity<Collection<Customer>>(adminService.getAllCustomers(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get one customer by customer id
     * @param accessToken String
     * @param customerID int
     * @return ResponseEntity<>
     */
    @GetMapping("/getOneCustomer/{accessToken}")
    fun getOneCustomer(@RequestParam customerID: Int, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, 0, ClientType.Administrator))
                ResponseEntity<Customer>(adminService.getOneCustomer(customerID), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CustomerDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }


}