package com.emikhalets.voteapp.text_validator

import com.emikhalets.voteapp.utils.ResetPassToast
import com.emikhalets.voteapp.utils.validateResetPass
import org.junit.Assert.assertTrue
import org.junit.Test

class ResetPassInputValidatorTest {

    @Test
    fun validateResetPassValidate() {
        val email = "login@mail.ru"
        var success = false
        validateResetPass(email) { if (it == ResetPassToast.SUCCESS) success = true }
        assertTrue(success)
    }

    @Test
    fun validateResetPassEmptyEmail() {
        val email = ""
        var emptyFields = false
        validateResetPass(email) { if (it == ResetPassToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateResetPassInvalidLogin_1() {
        val email = "login"
        var invalidLogin = false
        validateResetPass(email) { if (it == ResetPassToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateResetPassInvalidLogin_2() {
        val email = "@domain.com"
        var invalidLogin = false
        validateResetPass(email) { if (it == ResetPassToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateResetPassInvalidLogin_3() {
        val email = "email@domain"
        var invalidLogin = false
        validateResetPass(email) { if (it == ResetPassToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateResetPassInvalidLogin_4() {
        val email = "email@111.222.333.44444"
        var invalidLogin = false
        validateResetPass(email) { if (it == ResetPassToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }

    @Test
    fun validateResetPassInvalidLogin_5() {
        val email = "email@domain..com"
        var invalidLogin = false
        validateResetPass(email) { if (it == ResetPassToast.INVALID_EMAIL) invalidLogin = true }
        assertTrue(invalidLogin)
    }
}