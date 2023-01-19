package com.ismealdi.visit.base

import com.ismealdi.visit.api.network.Code
import com.ismealdi.visit.api.network.Network
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseCommand : KoinComponent {

    protected val network by inject<Network>()
    protected val code by inject<Code>()

}