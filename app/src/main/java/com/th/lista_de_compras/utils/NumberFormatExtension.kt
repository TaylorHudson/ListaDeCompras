package com.th.lista_de_compras.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.formatAsCurrency(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR")) as DecimalFormat
    return numberFormat.format(this)
}

fun BigDecimal.toCentsString(): String {
    return this
        .multiply(BigDecimal(100))
        .toBigInteger()
        .toString()
}

fun String.toBigDecimalFromCents(): BigDecimal {
    return this.toBigDecimalOrNull()?.divide(BigDecimal(100)) ?: BigDecimal.ZERO
}
