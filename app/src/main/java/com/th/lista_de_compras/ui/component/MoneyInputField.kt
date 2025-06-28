import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MoneyInputField(
    amount: String,
    onAmountChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    }

    var textFieldValue by remember(amount) {
        val clean = amount.filter { it.isDigit() }
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
        onValueChange = { newValue ->
            val digits = newValue.text.filter { it.isDigit() }
            // Atualiza valor externo em centavos
            onAmountChange(digits)
            // Formata com moeda
            val parsed = digits.toBigDecimalOrNull()?.divide(BigDecimal(100)) ?: BigDecimal.ZERO
            val formatted = currencyFormatter.format(parsed)
            // Atualiza valor interno com cursor no final
            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = modifier,
        singleLine = true,
        readOnly = false,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color.Black
        )
    )
}
