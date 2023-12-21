package com.atwa.androidgpstask.feature.tracking.data

import com.atwa.androidgpstask.core.network.ResponseValidator

data class LocationUpdateResponse(
    val status: Int? = null,
    val msgId: Int? = null,
    val msg: String? = null
) : ResponseValidator {
    override fun isValid(): Boolean = msg == "Success"

}