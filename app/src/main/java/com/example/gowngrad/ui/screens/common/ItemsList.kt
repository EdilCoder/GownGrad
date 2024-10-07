package com.example.gowngrad.ui.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.ui.screens.settings.DropdownContextMenu

@Composable
fun ItemsList(
    item: Item,
    options: List<String>,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    onActionClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .padding(8.dp, 0.dp, 16.dp, 16.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { checked ->
                    onCheckChange(checked)
                },
                modifier = Modifier.padding(8.dp, 0.dp)
            )
            Text(text = item.fullName,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.1.sp
                ),
                modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = item.phoneNumber,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.1.sp
                ),
                modifier = Modifier.weight(1f))

            DropdownContextMenu(options, Modifier.wrapContentWidth(), onActionClick)
        }
    }
}
