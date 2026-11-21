package com.livk.provider.api.domain

import java.io.Serializable

/**
 * @author livk
 */
data class Users(var id: Long, var username: String, var password: String) : Serializable {

}
