package com.maduoliveira.savegameapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.ui.theme.SaveGameAppTheme
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame
import com.maduoliveira.savegameapp.ui.theme.neonGreen
import com.maduoliveira.savegameapp.ui.theme.neonRed
import kotlinx.coroutines.delay
@Composable
fun SaveGameSplashScreen(onTargetReached: () -> Unit) {
    var startProgress by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (startProgress) 1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    var startFadeIn by remember { mutableStateOf(false) }
    val logoAlpha by animateFloatAsState(
        targetValue = if (startFadeIn) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true) {
        startFadeIn = true
        delay(300)
        startProgress = true
        delay(3200)
        onTargetReached()
    }

    val transitionShadowGradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color(0xFF0F1115).copy(alpha = 0.5f),
            Color(0xFF000000)
        ),
        startY = 0f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.img_01),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(transitionShadowGradient))
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.5f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.save_game_app),
                    color = Color.White,
                    style = TypographySaveGame.displayLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .alpha(logoAlpha)
                        .neonTextGlowEffect(neonGreen)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.slogan),
                    color = Color.White,
                    style = TypographySaveGame.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .alpha(logoAlpha)
                        .neonTextGlowEffect(neonGreen)
                )
            }

            Spacer(modifier = Modifier.weight(1.2f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.loading),
                    style = TypographySaveGame.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .width(240.dp)
                        .height(12.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            RoundedCornerShape(2.dp)
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(1.dp)
                            )
                            .neonGlowEffect(neonRed)
                    )
                }
            }
        }
    }
}
fun Modifier.neonGlowEffect(color: Color, blurRadius: Float = 30f): Modifier = this.then(
    Modifier.drawWithContent {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                this.color = android.graphics.Color.TRANSPARENT
                setShadowLayer(blurRadius, 0f, 0f, color.toArgb())
                style = android.graphics.Paint.Style.STROKE
                strokeWidth = 1f
            }

            canvas.nativeCanvas.drawRoundRect(
                0f, 0f, size.width, size.height, 0f, 0f, paint
            )
        }
        drawContent()
    }
)

fun Modifier.neonTextGlowEffect(color: Color, blurRadius: Float = 25f): Modifier = this.then(
    Modifier.drawWithContent {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                setShadowLayer(blurRadius, 0f, 0f, color.toArgb())
            }
            canvas.nativeCanvas.saveLayer(null, paint)
            drawContent()
            canvas.nativeCanvas.restore()
        }
        drawContent()
    }
)

@Preview
@Composable
fun SaveGameScreenSplashPreview (){
    SaveGameAppTheme() {
        SaveGameSplashScreen {  }
    }
}