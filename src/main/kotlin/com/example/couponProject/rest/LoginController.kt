package com.example.couponProject.rest


import com.example.couponProject.beans.AccessToken
import com.example.couponProject.service.components.loginManager.ClientType
import com.example.couponProject.service.components.loginManager.CouponSystem
import com.example.couponProject.service.components.loginManager.TokensManager
import com.example.couponProject.service.facade.ClientFacade
import com.example.couponProject.service.facade.CompanyFacade
import com.example.couponProject.service.facade.CustomerFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController



@RestController
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class LoginController {
    @Autowired
    private lateinit var couponSystem: CouponSystem
    @Autowired
    private lateinit var tokensManager: TokensManager

    /**
     * The gateway to the coupons system from the client side. login to the user account.
     * @param email String
     * @param password String
     * @param type ClientType
     * @return ResponseEntity<>
     */
    @GetMapping("/Login")
    fun login(@RequestParam email: String, @RequestParam password: String, @RequestParam type: ClientType): ResponseEntity<*> {
        return try {
            val clientFacade: ClientFacade? = couponSystem.login(email, password, type)
            val accessToken: AccessToken
            if (clientFacade != null)
                when (type) {
                ClientType.Administrator -> {
                    //CREATES ACCESS TOKEN AND ADD IT TO THE LIST
                    accessToken = AccessToken(0, ClientType.Administrator)
                    tokensManager.addNewAccessToken(accessToken)
                    ResponseEntity(accessToken.toJason(), HttpStatus.OK)
                }
                ClientType.Company -> {
                    val companyFacade: CompanyFacade = clientFacade as CompanyFacade
                    //CREATES ACCESS TOKEN AND ADD IT TO THE LIST
                    accessToken = AccessToken(companyFacade.getCompanyDetails().id, ClientType.Company)
                    tokensManager.addNewAccessToken(accessToken)
                    ResponseEntity(accessToken.toJason(), HttpStatus.OK)
                }
                ClientType.Customer -> {
                    val customerFacade: CustomerFacade = clientFacade as CustomerFacade
                    //CREATES ACCESS TOKEN AND ADD IT TO THE LIST
                    accessToken = AccessToken(customerFacade.getCustomerDetails().id, ClientType.Customer)
                    tokensManager.addNewAccessToken(accessToken)
                    ResponseEntity(accessToken.toJason(), HttpStatus.OK)
                }
                else -> ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
            } else
                ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            ResponseEntity("Incorrect email or password!", HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * check if the token is still valid
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/Auth")
    fun isAccessTokenValid(@RequestParam accessToken: String?): ResponseEntity<*> {
        return if (tokensManager.isClientRegistered(accessToken))
            ResponseEntity("{\"authorized\":true}", HttpStatus.OK)
        else
            ResponseEntity("{\"authorized\":false}", HttpStatus.OK)
    }

    /**
     * the user logged out from his account
     * @param accessToken String
     * @return ResponseEntity<>
     */
    @GetMapping("/Logout")
    fun logout(@RequestParam accessToken: String?): ResponseEntity<*> {
        return if (tokensManager.logoutAccessToken(accessToken))
            ResponseEntity("{\"logout\":true}", HttpStatus.OK)
        else
            ResponseEntity("{\"logout\":false}", HttpStatus.OK)
    }
}