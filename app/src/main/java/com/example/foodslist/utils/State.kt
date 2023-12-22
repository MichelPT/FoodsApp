package com.example.foodslist.utils

sealed class State<out T: Any?> {
    object Loading : State<Nothing>()
    data class Success<out T: Any>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
}