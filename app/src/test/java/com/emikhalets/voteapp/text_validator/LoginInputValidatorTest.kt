package com.emikhalets.voteapp.text_validator

import com.emikhalets.voteapp.utils.LoginToast
import com.emikhalets.voteapp.utils.validateLogIn
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginInputValidatorTest {

    @Test
    fun validateLogInValidate() {
        val login = "login@mail.ru"
        val pass = "123456"
        var success = false
        validateLogIn(login, pass) { if (it == LoginToast.SUCCESS) success = true }
        assertTrue(success)
    }

    @Test
    fun validateLogInInvalidPass() {
        val login = "login@mail.ru"
        val pass = "12345"
        var invalidPass = false
        validateLogIn(login, pass) { if (it == LoginToast.INVALID_PASS) invalidPass = true }
        assertTrue(invalidPass)
    }

    @Test
    fun validateLogInEmptyLogin() {
        val login = ""
        val pass = "123456"
        var emptyFields = false
        validateLogIn(login, pass) { if (it == LoginToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateLogInEmptyPass() {
        val login = "login@mail.com"
        val pass = ""
        var emptyFields = false
        validateLogIn(login, pass) { if (it == LoginToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateLogInEmptyPassAndLogin() {
        val login = ""
        val pass = ""
        var emptyFields = false
        validateLogIn(login, pass) { if (it == LoginToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateLogInInvalidLogin_1() {
        val login = "login"
        val pass = "123456"
        var invalidLogin = false
        validateLogIn(login, pass) { if (it == LoginToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateLogInInvalidLogin_2() {
        val login = "@domain.com"
        val pass = "123456"
        var invalidLogin = false
        validateLogIn(login, pass) { if (it == LoginToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateLogInInvalidLogin_3() {
        val login = "email@domain"
        val pass = "123456"
        var invalidLogin = false
        validateLogIn(login, pass) { if (it == LoginToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateLogInInvalidLogin_4() {
        val login = "email@111.222.333.44444"
        val pass = "123456"
        var invalidLogin = false
        validateLogIn(login, pass) { if (it == LoginToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateLogInInvalidLogin_5() {
        val login = "email@domain..com"
        val pass = "123456"
        var invalidLogin = false
        validateLogIn(login, pass) { if (it == LoginToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }
}