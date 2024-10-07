package com.example.gowngrad.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownContextMenu(
    options: List<String>,
    modifier: Modifier,
    onActionClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { isExpanded = true }) {
            Icon(
                modifier = Modifier.padding(8.dp, 0.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(180.dp)
                .wrapContentHeight()
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Log.d("DropdownMenuItem", "Rendering option: $selectionOption")
                        Text(text = selectionOption) },
                    onClick = {
                        isExpanded = false
                        onActionClick(selectionOption)
                    }
                )
            }
        }
    }
}



