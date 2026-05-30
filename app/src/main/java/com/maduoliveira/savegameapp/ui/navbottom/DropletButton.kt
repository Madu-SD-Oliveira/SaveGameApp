package com.maduoliveira.savegameapp.ui.navbottom

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DropletButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    dropletColor: Color = Color.Magenta,
    animationSpec: AnimationSpec<Float> = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
) {
    val fraction by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = animationSpec,
        label = "dropletFraction"
    )

    Box(
        modifier = modifier
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val painter = painterResource(id = icon)
        Box(
            modifier = Modifier
                .size(26.dp)
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawCircle(
                        color = dropletColor,
                        radius = size.width * fraction,
                        center = center,
                        blendMode = BlendMode.SrcIn
                    )
                }
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = Color.Gray
            )
        }
    }
}
