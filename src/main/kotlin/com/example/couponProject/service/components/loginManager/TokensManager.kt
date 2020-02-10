package com.example.couponProject.service.components.loginManager


import com.example.couponProject.beans.AccessToken
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*



@Service
class TokensManager {

    private val accessTokens: MutableList<AccessToken> = ArrayList<AccessToken>()

    /**
     * check if the access token is exist in the list, if so, the token validity should be extended by 30 minutes
     * @param accessToken String
     * @param userId int
     * @param clientType ClientType
     * @return boolean
     */
    fun isAccessTokenExist(accessToken: String, userId: Int, clientType: ClientType): Boolean {
        if (accessTokens != null) {
            for (xst in accessTokens) {
                if (xst != null) {
                    if (xst.uuid.toString() == accessToken && xst.userId == userId && xst.clientType == clientType) { //Each time we come here, the Token validity should be extended by 30 minutes, therefor we call to addNewAccessToken
                        val nowPlus30Minutes = LocalDateTime.now().plusMinutes(30)
                        xst.expiredTime= Date.from(nowPlus30Minutes.atZone(ZoneId.systemDefault()).toInstant())
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * check if the access token that got from the client side, is exist in the list for authentication
     * @param accessToken String
     * @return boolean
     */
    fun isClientRegistered(accessToken: String?): Boolean {
        if (accessTokens != null) {
            for (xst in accessTokens) {
                if (xst != null) {
                    if (xst.uuid.toString() == accessToken) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * add new access token to the authorized users list
     * @param accessToken AccessToken
     */
    fun addNewAccessToken(accessToken: AccessToken) {
        if (!isAccessTokenExist(accessToken.uuid.toString(), accessToken.userId, accessToken.clientType)) {
            val nowPlus30Minutes = LocalDateTime.now().plusMinutes(30)
            accessToken.expiredTime = Date.from(nowPlus30Minutes.atZone(ZoneId.systemDefault()).toInstant())
            accessTokens.add(accessToken)
        }
    }

    /**
     * remove access token from the list. we came here if the user logged out or if the access token time expired.
     * @param accessToken AccessToken
     */
    fun removeAccessToken(accessToken: AccessToken?) {
        accessTokens.remove(accessToken)
    }

    /**
     * When the user logged out from the system, then remove his access token
     * @param accessToken String
     * @return boolean
     */
    fun logoutAccessToken(accessToken: String?): Boolean {
        if (accessTokens != null) {
            for (xst in accessTokens) {
                if (xst != null) {
                    if (xst.uuid.toString() == accessToken) {
                        removeAccessToken(xst)
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * When there was no server-side activity in the last 30 minutes, then remove all the time out access tokens
     */
    fun removeAllExpiredAccessTokens() {
        if (accessTokens != null) {
            val date = Date()
            for (accessToken in accessTokens) {
                if (accessToken != null) {
                    if (date.after(accessToken.expiredTime))
                        removeAccessToken(accessToken)
                }
            }
        }
    }

}