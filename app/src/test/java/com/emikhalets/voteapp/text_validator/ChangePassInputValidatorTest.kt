package com.emikhalets.voteapp.text_validator

import com.emikhalets.voteapp.utils.ChangePassToast
import com.emikhalets.voteapp.utils.validateChangePass
import org.junit.Assert.assertTrue
import org.junit.Test

class ChangePassInputValidatorTest {

    @Test
    fun validateChangePassValidate() {
        val pass = "1234567"
        val cPass = "1234567"
        var success = false
        validateChangePass(pass, cPass) { if (it == ChangePassToast.SUCCESS) success = true }
        assertTrue(success)
    }

    @Test
    fun validateChangePassEmptyPass1() {
        val pass = ""
        val cPass = ""
        var emptyFields = false
        validateChangePass(pass, cPass) { if (it == ChangePassToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateChangePassEmptyPass2() {
        val pass = ""
        val cPass = "1234567"
        var emptyFields = false
        validateChangePass(pass, cPass) { if (it == ChangePassToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateChangePassEmptyPass3() {
        val pass = "1234567"
        val cPass = ""
        var emptyFields = false
        validateChangePass(pass, cPass) { if (it == ChangePassToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }

    @Test
    fun validateChangePassInvalidPass() {
        val pass = "12345"
        val cPass = "12345"
        var invalid = false
        validateChangePass(pass, cPass) { if (it == ChangePassToast.INVALID_PASS) invalid = true }
        assertTrue(invalid)
    }

    @Test
    fun validateChangePassMismatch() {
        val pass = "1234567"
        val cPass = "1234568"
        var mismatch = false
        validateChangePass(pass, cPass) { if (it == ChangePassToast.PASS_MISMATCH) mismatch = true }
        assertTrue(mismatch)
    }
}