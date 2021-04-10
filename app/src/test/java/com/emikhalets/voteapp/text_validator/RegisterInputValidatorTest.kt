package com.emikhalets.voteapp.text_validator

import com.emikhalets.voteapp.utils.RegisterToast
import com.emikhalets.voteapp.utils.validateRegister
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterInputValidatorTest {

    @Test
    fun validateRegisterValidate() {
        val login = "login@mail.ru"
        val pass = "123456"
        val passC = "123456"
        var success = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.SUCCESS) success = true }
        assertTrue(success)
    }

    @Test
    fun validateRegisterInvalidPass() {
        val login = "login@mail.ru"
        val pass = "12345"
        val passC = "12345"
        var invalidPass = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.INVALID_PASS) invalidPass = true }
        assertTrue(invalidPass)
    }

    @Test
    fun validateRegisterInvalidPassMismatch() {
        val login = "login@mail.ru"
        val pass = "123456"
        val passC = "123457"
        var passMismatch = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.PASS_MISMATCH) passMismatch = true }
        assertTrue(passMismatch)
    }

    @Test
    fun validateRegisterEmptyLogin() {
        val login = ""
        val pass = "123456"
        val passC = "123456"
        var emptyFields = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateRegisterEmptyPass() {
        val login = "login@mail.com"
        val pass = ""
        val passC = "123456"
        var emptyFields = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateRegisterEmptyPassConf() {
        val login = "login@mail.com"
        val pass = "123456"
        val passC = ""
        var emptyFields = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateRegisterEmptyPassAndLoginAndPassConf() {
        val login = ""
        val pass = ""
        val passC = ""
        var emptyFields = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateRegisterInvalidLogin_1() {
        val login = "login"
        val pass = "123456"
        val passC = "123456"
        var invalidLogin = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateRegisterInvalidLogin_2() {
        val login = "@domain.com"
        val pass = "123456"
        val passC = "123456"
        var invalidLogin = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateRegisterInvalidLogin_3() {
        val login = "email@domain"
        val pass = "123456"
        val passC = "123456"
        var invalidLogin = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateRegisterInvalidLogin_4() {
        val login = "email@111.222.333.44444"
        val pass = "123456"
        val passC = "123456"
        var invalidLogin = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateRegisterInvalidLogin_5() {
        val login = "email@domain..com"
        val pass = "123456"
        val passC = "123456"
        var invalidLogin = false
        validateRegister(login, pass, passC) { if (it == RegisterToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }
}