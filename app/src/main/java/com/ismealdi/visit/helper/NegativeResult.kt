package com.ismealdi.visit.helper

/**
 *  This negative results used for uniforming error from data layer to be used in UI layer
 *  Every business rule has dedicated error type (see: [ExceptionError])
 *  Any suggestion is welcomed.
 *
 */
sealed class ErrorResult(open val message: String)

object TestError: ErrorResult("UnitTesting")
data class CommonError(override val message: String): ErrorResult(message)

sealed class ServiceError(override val message: String): ErrorResult(message)
object ServerError: ServiceError("")
object NotFoundError: ServiceError("")
object UnknownError: ServiceError("")

object InvalidToken: ErrorResult("")

sealed class ExceptionError(override val message: String): ErrorResult(message)
data class UnknownException(val throwable: Throwable): ExceptionError(throwable.localizedMessage.orEmpty())
object UnreachableException: ExceptionError("")
object TimeOutException: ExceptionError("")
data class  ProtoBuffException(val throwable: Throwable): ExceptionError(throwable.localizedMessage.orEmpty())
data class  RoomException(val throwable: Throwable): ExceptionError(throwable.localizedMessage.orEmpty())

/**
 *  Specific Errors
 */
data class ReferralCodeError(override val message: String): ErrorResult(message)