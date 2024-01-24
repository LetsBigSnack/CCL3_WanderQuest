package com.ccl3_id.wanderquest.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccl3_id.wanderquest.ui.theme.RobotoFontFamily

@Composable
fun MultiStyleText(text1: String, color1: Color, text2: String, color2: Color, fontSize : TextUnit = 50.sp, modifier: Modifier = Modifier.padding(top = 10.dp, bottom = 10.dp) ) {
    Text( text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color1)) {
            append(text1)
        }
        withStyle(style = SpanStyle(color = color2)) {
            append(text2)
        }
    },
        fontSize = fontSize,
        fontFamily = RobotoFontFamily,
        modifier = modifier
    )
}