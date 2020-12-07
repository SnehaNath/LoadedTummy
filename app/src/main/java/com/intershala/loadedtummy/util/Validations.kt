package com.intershala.loadedtummy.util

class Validations {

    fun validateMobile(mobile: String): Boolean {
        return mobile.length == 10
    }
    fun validatePasswordLength(pass: String): Boolean {
        return pass.length > 4
    }

}