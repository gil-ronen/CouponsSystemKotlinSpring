package com.example.couponProject.rest

import org.springframework.http.ResponseEntity


//@RestController
abstract class ClientController {
    /**
     * login to client user
     * @param email    String
     * @param password String
     * @return ResponseEntity<>
     */
    abstract fun login(email: String, password: String): ResponseEntity<*>
}
