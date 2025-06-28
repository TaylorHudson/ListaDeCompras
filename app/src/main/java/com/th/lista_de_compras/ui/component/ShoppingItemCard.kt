package com.th.lista_de_compras.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.utils.formatAsCurrency

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit,
    onCardClick: () -> Unit,
) {
    val textDecoration = if (item.purchased) TextDecoration.LineThrough else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp),
            )
            .clickable(onClick = onCardClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.purchased,
                onCheckedChange =  onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    checkmarkColor = Color.White,
                    uncheckedColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${item.quantity}x ${item.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = textDecoration
                ),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = item.price.formatAsCurrency(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textDecoration = textDecoration
                ),
            )
        }

    }
}
