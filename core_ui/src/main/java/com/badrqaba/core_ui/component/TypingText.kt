package com.badrqaba.core_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import kotlinx.coroutines.delay

@Composable
fun TypingText(
    fullText: String,
    modifier: Modifier = Modifier,
    typingSpeed: Long = 50L,
    textStyle: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = Color.Unspecified
) {
    var displayedText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(fullText) {
        displayedText = ""
        for (i in fullText.indices) {
            displayedText = fullText.substring(0, i + 1)
            delay(typingSpeed)
        }
    }

    Text(
        text = displayedText,
        style = textStyle,
        modifier = modifier,
        textAlign = textAlign,
        color = color,
        lineHeight = 40.sp
    )
}
@Preview
@Composable
private fun TypingTextPreview() {
    PocketGoodsTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TypingText(
                fullText = "Pocket goods android application, all right reserved",
                typingSpeed = 60L,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}