package com.example.couponProject.service.facade

import com.example.couponProject.exceptions.loginExceptions.EmailNotFoundException
import com.example.couponProject.exceptions.loginExceptions.WrongPasswordException


abstract class ClientFacade {
    /**
     * login to client user
     * @param email String
     * @param password String
     * @return boolean
     * @throws EmailNotFoundException if email not found
     * @throws WrongPasswordException if password is incorrect
     */
    @Throws(EmailNotFoundException::class, WrongPasswordException::class)
    abstract fun login(email: String, password: String): Boolean
}