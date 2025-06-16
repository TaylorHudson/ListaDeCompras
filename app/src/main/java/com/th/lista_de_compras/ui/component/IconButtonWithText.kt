package com.th.lista_de_compras.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonWithText(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDeleteButton: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    TextButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            tint = if (isDeleteButton) Color.Red else LocalContentColor.current,
            contentDescription = null
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            style = textStyle
        )

        Spacer(Modifier.width(8.dp))
    }
}
