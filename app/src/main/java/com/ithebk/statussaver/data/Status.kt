package com.ithebk.statussaver.data

enum class STATUS_TYPE {
    IMAGE,
    VIDEO
}

data class Status(val path: String, val type: STATUS_TYPE) {

}