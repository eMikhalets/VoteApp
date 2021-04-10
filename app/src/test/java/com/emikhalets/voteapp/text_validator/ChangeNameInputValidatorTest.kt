package com.emikhalets.voteapp.text_validator

import com.emikhalets.voteapp.utils.ChangeNameToast
import com.emikhalets.voteapp.utils.validateChangeName
import org.junit.Assert.assertTrue
import org.junit.Test

class ChangeNameInputValidatorTest {

    @Test
    fun validateChangeNameValidate() {
        val name = "asdcvbjirh"
        var success = false
        validateChangeName(name) { if (it == ChangeNameToast.SUCCESS) success = true }
        assertTrue(success)
    }

    @Test
    fun validateChangeNameEmptyName() {
        val name = ""
        var emptyFields = false
        validateChangeName(name) { if (it == ChangeNameToast.EMPTY_FIELDS) emptyFields = true }
        assertTrue(emptyFields)
    }
}