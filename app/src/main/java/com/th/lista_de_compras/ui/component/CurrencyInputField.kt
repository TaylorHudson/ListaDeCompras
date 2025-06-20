package com.th.lista_de_compras.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@Composable
fun CurrencyInputField(
    valueInCents: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    }

    var textFieldValue by remember(valueInCents) {
        val clean = valueInCents.filter { it.isDigit() }
        val parsed = if (clean.isBlank()) BigDecimal.ZERO else BigDecimal(clean).divide(BigDecimal(100))
        val formatted = currencyFormatter.format(parsed)
        mutableStateOf(
            TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        )
    }

    TextField(
        value = textFieldValue,
        onValueChange = { it ->
            val lastDigit = it.text.lastOrNull()?.takeIf { it.isDigit() }?.toString()
            val newDigits = valueInCents.filter { it.isDigit() } + (lastDigit ?: "")

            onValueChange(newDigits)

            val parsed = newDigits.toBigDecimalOrNull()?.divide(BigDecimal(100)) ?: BigDecimal.ZERO
            val formatted = currencyFormatter.format(parsed)

            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        singleLine = true,
    )
}
