package com.example.couponProject.beans

import com.example.couponProject.service.components.loginManager.ClientType
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.*

@Component
@Scope("prototype")
class AccessToken(var userId: Int, var clientType: ClientType) {

    var expiredTime: Date = Date()
    var uuid: UUID = UUID.randomUUID()

    init {
        this.userId = userId
        this.clientType = clientType
    }

    /**
     * convert access token (uuid) to json for the client side
     * @return String
     */
    fun toJason(): String {
        return "{\"AccessToken\": \"$uuid\"}"
    }
}