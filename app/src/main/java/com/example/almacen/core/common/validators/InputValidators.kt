package com.example.almacen.core.common.validators

fun String.isNumeric(): Boolean{
    return this.all{it.isDigit()}
}

fun String.isDecimal(): Boolean{
    return this.matches(Regex("^\\d*\\.?\\d*\$"))
}