package com.maduoliveira.savegameapp.ui.screens.dashboard

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun diagonalSharpCornerShape(cutPx: Float): Shape =
    GenericShape { size: Size, layoutDirection: LayoutDirection ->
        val w = size.width
        val h = size.height
        val cut = cutPx

        val path = Path().apply {
            moveTo(cut, 0f)
            lineTo(w - cut, 0f)
            lineTo(w, cut)
            lineTo(w, h - cut)
            lineTo(w - cut, h)
            lineTo(cut, h)
            lineTo(0f, h - cut)
            lineTo(0f, cut)
            close()
        }
        addPath(path)
    }
//EXEMPLO DE USO
/*
val cutDp = 18.dp
val cutPx = with(LocalDensity.current) { cutDp.toPx() }
Card(
modifier = modifier,
shape = diagonalSharpCornerShape(cutPx),
colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface)
) {*/

val customCardShape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 20.dp,
    bottomEnd = 0.dp,
    bottomStart = 20.dp
)