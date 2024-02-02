package com.ccl3_id.wanderquest.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccl3_id.wanderquest.ui.theme.RobotoFontFamily


class ButtonSettings(){
    companion object{
        val BUTTON_FONT_SIZE_MEDIUM = 20.sp
        val BUTTON_FONT_SIZE_BIG = 28.sp
        val BUTTON_FONT_SIZE_MASSIVE = 32.sp

    }
}

class TextSettings(){
    companion object{
        val TEXT_FONT_SIZE_MEDIUM = 20.sp
        val TEXT_FONT_SIZE_BIG = 28.sp
    }

}

@Composable
fun MultiColorText(text1: String, color1: Color, text2: String, color2: Color, fontSize : TextUnit = 50.sp, modifier: Modifier = Modifier.padding(top = 10.dp, bottom = 10.dp) ) {
    androidx.compose.material.Text(
        text = buildAnnotatedString {
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


@Composable
fun WanderButton(text : String,
                 color: Color,
                 onClickEvent : () -> Unit,
                 fontSize : TextUnit,
                 textColor : Color,
                 enabled : Boolean = true,
                 modifier: Modifier = Modifier
                     .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                     .fillMaxWidth()
    ){
    Button(
        onClick = onClickEvent,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled
    ) {
        Text(text = text, fontSize = fontSize, color = textColor)
    }
}

@Composable
fun WanderMenuButton(text : String,
                     color: Color = MaterialTheme.colorScheme.primary,
                     onClickEvent : () -> Unit,
                     modifier: Modifier = Modifier
                         .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                         .fillMaxWidth()
){
    Button(
        onClick = onClickEvent,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
    ) {
        BigText(text = text);
    }
}

@Composable
fun WanderMenuButtonSecondary(
    text : String,
    onClickEvent : () -> Unit,
    modifier: Modifier = Modifier
        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        .fillMaxWidth()
){
    WanderMenuButton(text = text, color = MaterialTheme.colorScheme.onSecondaryContainer, onClickEvent = onClickEvent, modifier = modifier)
}

@Composable
fun WanderMenuButtonCancel(
    text : String,
    onClickEvent : () -> Unit,
    modifier: Modifier = Modifier
        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        .fillMaxWidth()
){
    WanderMenuButton(text = text, color = MaterialTheme.colorScheme.tertiary, onClickEvent = onClickEvent, modifier = modifier)
}


@Composable
fun MediumText(text: String,
               color: Color = Color.White,
               modifier: Modifier = Modifier){

    Text(
        text = text,
        color = color,
        fontSize = TextSettings.TEXT_FONT_SIZE_MEDIUM,
        modifier = modifier
    )

}

@Composable
fun BigText(text: String,
               color: Color = Color.White,
               modifier: Modifier = Modifier){

    Text(
        text = text,
        color = color,
        fontSize = TextSettings.TEXT_FONT_SIZE_BIG,
        modifier = modifier
    )

}