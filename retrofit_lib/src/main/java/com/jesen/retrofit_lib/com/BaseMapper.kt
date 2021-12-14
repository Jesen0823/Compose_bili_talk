package com.jesen.retrofit_lib.com

abstract class BaseMapper<T : Any, V : Any> {

    /** Return true if this can convert [data]. */
    fun handles(data: T): Boolean = true

    /** Convert [data] into [V]. */
    abstract fun map(data: T): V
}