package com.th.lista_de_compras.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

    val formattedText = remember(valueInCents) {
        val clean = valueInCents.filter { it.isDigit() }
        val parsed = if (clean.isBlank()) BigDecimal.ZERO else BigDecimal(clean).divide(BigDecimal(100))
        currencyFormatter.format(parsed)
    }

    OutlinedTextField(
        value = formattedText,
        onValueChange = { newText ->
            val digits = valueInCents.filter { it.isDigit() }
            val appended = digits + newText.takeLast(1).filter { it.isDigit() }
            onValueChange(appended)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        singleLine = true,
        readOnly = true
    )
}
