package com.emikhalets.voteapp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class TakeImageContract(private val activity: MainActivity) : ActivityResultContract<Int, Uri>() {

    override fun createIntent(context: Context, input: Int): Intent {
        return CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setAspectRatio(1, 1)
                .setRequestedSize(input, input)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .getIntent(activity)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) {
            CropImage.getActivityResult(intent).uri
        } else {
            null
        }
    }
}