package com.example.couponProject.rest

import com.example.couponProject.beans.Category
import com.example.couponProject.beans.Coupon
import com.example.couponProject.beans.Customer
import com.example.couponProject.exceptions.couponExceptions.CouponHasExpiredException
import com.example.couponProject.exceptions.couponExceptions.CouponOutOfStockException
import com.example.couponProject.exceptions.couponExceptions.CustomerPurchasedThisCouponYetException
import com.example.couponProject.exceptions.customerExceptions.CustomerDoesntExistException
import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.service.components.loginManager.ClientType
import com.example.couponProject.service.components.loginManager.TokensManager
import com.example.couponProject.service.facade.CustomerFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("Customer")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class CustomerController : ClientController() {
    @Autowired
    private lateinit var customerService: CustomerFacade
    @Autowired
    private lateinit var tokensManager: TokensManager

    /**
     * login to customer user - NOT IN USE
     * @param email String
     * @param password String
     * @return ResponseEntity<>
     */
    override fun login(email: String, password: String): ResponseEntity<*> {
        return try {
            ResponseEntity<Boolean>(customerService.login(email, password), HttpStatus.OK)
        } catch (e: EmailNotFoundException) {
            ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        } catch (e: WrongPasswordException) {
            ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * purchase new coupon
     * @param accessToken String
     * @param coupon Coupon
     * @return ResponseEntity<>
     */
    @PostMapping("/purchaseCoupon/{accessToken}")
    fun purchaseCoupon(@RequestBody coupon: Coupon, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, customerService.getCustomerDetails().id, ClientType.Customer))
                ResponseEntity<Coupon>(customerService.purchaseCoupon(coupon) , HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CustomerDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CouponOutOfStockException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CouponHasExpiredException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CustomerPurchasedThisCouponYetException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * get all coupons of this customer
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getCustomerCoupons/{accessToken}")
    fun getCustomerCoupons(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, customerService.getCustomerDetails().id, ClientType.Customer))
            ResponseEntity<MutableList<Coupon>>(customerService.getCustomerCoupons(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get all coupons of this customer by category
     * @param accessToken String
     * @param category Category
     * @return ResponseEntity<>
     */
    @GetMapping("/getCustomerCouponsByCategory/{accessToken}")
    fun getCustomerCoupons(@RequestParam category: Category, @PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, customerService.getCustomerDetails().id, ClientType.Customer))
            ResponseEntity<MutableList<Coupon>>(customerService.getCustomerCoupons(category) , HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get all coupons of this customer by less then equal to max price limit
     * @param accessToken String
     * @param maxPrice double
     * @return ResponseEntity<>
     */
    @GetMapping("/getCustomerCouponsByMaxPrice/{accessToken}")
    fun getCustomerCoupons(@RequestParam maxPrice: Double, @PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, customerService.getCustomerDetails().id, ClientType.Customer))
            ResponseEntity<MutableList<Coupon>>(customerService.getCustomerCoupons(maxPrice), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get customer details
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getCustomerDetails/{accessToken}")
    fun getCustomerDetails(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, customerService.getCustomerDetails().id, ClientType.Customer))
            ResponseEntity<Customer>(customerService.getCustomerDetails(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get all coupons that exist in the system
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getAllCouponsInTheSystem/{accessToken}")
    fun getAllCouponsInTheSystem(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, customerService.getCustomerDetails().id, ClientType.Customer))
            ResponseEntity<MutableList<Coupon>>(customerService.getAllCouponsInTheSystem(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }
}