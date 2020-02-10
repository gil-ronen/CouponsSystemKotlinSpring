package com.example.couponProject.rest


import com.example.couponProject.beans.Category
import com.example.couponProject.beans.Company
import com.example.couponProject.beans.Coupon
import com.example.couponProject.exceptions.companyExceptions.CompanyDoesntExistException
import com.example.couponProject.exceptions.couponExceptions.CompanyIdInCouponIsUnchangeableException
import com.example.couponProject.exceptions.couponExceptions.CouponDoesntExistException
import com.example.couponProject.exceptions.couponExceptions.CouponTitleAlreadyExistInThisCompanyException
import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException
import com.example.couponProject.service.components.loginManager.ClientType
import com.example.couponProject.service.components.loginManager.TokensManager
import com.example.couponProject.service.facade.CompanyFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("Company")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class CompanyController : ClientController() {

    @Autowired
    private lateinit var companyService: CompanyFacade
    @Autowired
    private lateinit var tokensManager: TokensManager

    /**
     * login to company user - NOT IN USE
     * @param email String
     * @param password String
     * @return ResponseEntity<>
     */
    override fun login(email: String, password: String): ResponseEntity<*> {
        return try {
            ResponseEntity<Boolean>(companyService.login(email, password), HttpStatus.OK)
        } catch (e: EmailNotFoundException) {
            ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        } catch (e: WrongPasswordException) {
            ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * add a new coupon
     * @param coupon Coupon
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @PostMapping("/addCoupon/{accessToken}")
    fun addCoupon(@RequestBody coupon: Coupon, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
                ResponseEntity<Coupon>(companyService.addCoupon(coupon) , HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CompanyDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CouponTitleAlreadyExistInThisCompanyException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * update existing coupon
     * @param coupon Coupon
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @PutMapping("/updateCoupon/{accessToken}")
    fun updateCoupon(@RequestBody coupon: Coupon, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
                ResponseEntity<Coupon>(companyService.updateCoupon(coupon) , HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CouponDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CompanyIdInCouponIsUnchangeableException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: CouponTitleAlreadyExistInThisCompanyException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * delete existing coupon
     * @param couponID int
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @DeleteMapping("/deleteCoupon/{accessToken}")
    fun deleteCoupon(@RequestParam couponID: Int, @PathVariable accessToken: String): ResponseEntity<*> {
        return try {
            if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
                ResponseEntity<Boolean>(companyService.deleteCoupon(couponID), HttpStatus.OK)
            else
                ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
        } catch (e: CouponDoesntExistException) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * get all company coupons
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getCompanyCoupons/{accessToken}")
    fun getCompanyCoupons(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
            ResponseEntity<MutableList<Coupon>>(companyService.getCompanyCoupons(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get all company coupons by category
     * @param accessToken String
     * @param category Category
     * @return ResponseEntity<>
     */
    @GetMapping("/getCompanyCouponsByCategory/{accessToken}")
    fun getCompanyCoupons(@RequestParam category: Category, @PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
            ResponseEntity<MutableList<Coupon>>(companyService.getCompanyCoupons(category), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get all company coupons by less then equal to max price limit
     * @param accessToken String
     * @param maxPrice double
     * @return ResponseEntity<>
     */
    @GetMapping("/getCompanyCouponsByMaxPrice/{accessToken}")
    fun getCompanyCoupons(@RequestParam maxPrice: Double, @PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
            ResponseEntity<MutableList<Coupon>>(companyService.getCompanyCoupons(maxPrice), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }

    /**
     * get company details
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/getCompanyDetails/{accessToken}")
    fun getCompanyDetails(@PathVariable accessToken: String): ResponseEntity<*> {
        return if (tokensManager.isAccessTokenExist(accessToken, companyService.getCompanyDetails().id, ClientType.Company))
            ResponseEntity<Company>(companyService.getCompanyDetails(), HttpStatus.OK)
        else
            ResponseEntity("need to login!", HttpStatus.BAD_REQUEST)
    }
}