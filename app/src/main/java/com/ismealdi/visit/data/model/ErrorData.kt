package com.ismealdi.visit.data.model

import com.ismealdi.visit.extension.emptyString

data class ErrorData(
    var state: Boolean = false,
    var message: String = emptyString()
)