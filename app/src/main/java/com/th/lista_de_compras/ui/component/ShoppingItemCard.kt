package com.th.lista_de_compras.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.utils.formatAsCurrency

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
            .clickable(onClick = onCardClick),
        elevation = CardDefaults.cardElevation(2.dp)
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
