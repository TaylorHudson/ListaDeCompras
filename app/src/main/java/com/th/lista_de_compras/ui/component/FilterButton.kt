package com.th.lista_de_compras.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    selected: Boolean,
) {

    val backgroundColor = if (selected) Color.Black.copy(alpha = 0.6f) else Color.Black

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = Color.White),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 10.sp
            )
        )
    }
}