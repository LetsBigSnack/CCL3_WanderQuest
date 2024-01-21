package com.ccl3_id.wanderquest.ui.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import com.ccl3_id.wanderquest.CharactersActivity
import com.ccl3_id.wanderquest.CreationActivity
import com.ccl3_id.wanderquest.MainActivity
import com.ccl3_id.wanderquest.R
import com.ccl3_id.wanderquest.viewModels.LoginViewModel
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(loginViewModel: LoginViewModel, context: Context) {

    val state = loginViewModel.loginViewState.collectAsState()

    val players = state.value.players;

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Walkthrough Hero", fontSize = 40.sp)
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Box"
        )

        if(players.isNotEmpty()) {

            val lastPlayed = players.find { player -> player.lastPlayed };

            if (lastPlayed != null) {
                Button(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java);
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent);
                    },
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp).fillMaxWidth()
                ) {
                    Text(text = "Continue: "+lastPlayed.playerName, fontSize = 25.sp)
                }
            }

            Button(
                onClick = {
                    val intent = Intent(context, CharactersActivity::class.java);
                    context.startActivity(intent); },
                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp).fillMaxWidth()
            ) {
                Text(text = "Select Character", fontSize = 25.sp)
            }
        }
        Button(
            onClick = {
                val intent = Intent(context, CreationActivity::class.java);
                context.startActivity(intent);
            },
            modifier = Modifier.padding(top = 20.dp,start = 20.dp, end = 20.dp).fillMaxWidth()
        ) {
            Text(text = "Create Character", fontSize = 25.sp)
        }

        Button(
            onClick = {
                if (context is Activity) {
                    context.finishAffinity()
                }
            },
            modifier = Modifier.padding(top = 20.dp,start = 20.dp, end = 20.dp).fillMaxWidth()
        ) {
            Text(text = "Exit", fontSize = 25.sp)
        }
    }
}
