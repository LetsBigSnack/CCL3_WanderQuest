package com.ccl3_id.wanderquest.ui.composables

import android.content.Intent
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccl3_id.wanderquest.MainActivity


class ButtonSettings(){

    companion object{
        val BUTTON_FONT_SIZE_MEDIUM = 20.sp
        val BUTTON_FONT_SIZE_BIG = 28.sp
        val BUTTON_FONT_SIZE_MASSIVE = 32.sp

    }


}



@Composable
fun WanderButton(text : String, color: Color, onClickEvent : () -> Unit, fontSize : TextUnit, textColor : Color, enabled : Boolean = true, modifier: Modifier = Modifier
    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    .fillMaxWidth()){
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