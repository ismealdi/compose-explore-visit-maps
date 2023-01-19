package com.ismealdi.visit.modules

import org.koin.core.module.Module

interface BaseModule {

    var loadNeeded: Boolean
    fun module(): Module

}