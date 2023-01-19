package com.ismealdi.visit.helper

/**
 *  Wrapper class to contains result of external process
 *  Goal: Minimize Exception usage
 *  Rules:
 *      1. Left is Failure
 *      2. Right is Success
 *
 */
sealed class Either<out T>{
    data class Left<out T>(val error: ErrorResult) : Either<T>()
    data class Right<out T>(val data: T) : Either<T>()

    inline fun <V> fold(failure: (ErrorResult) -> V, success: (T) -> V): V = when (this) {
        is Left -> failure(error)
        is Right -> success(data)
    }
}

fun <T>right(value: T): Either<T> = Either.Right(value)
fun <T>left(error: ErrorResult): Either<T> = Either.Left(error)

fun <T>Either<T>.onSuccess(result: (T)-> Unit){
    if(this is Either.Right){
        result(this.data)
    }
}

fun <T>Either<T>.onFailure(result: (ErrorResult)-> Unit){
    if(this is Either.Left){
        result(this.error)
    }
}