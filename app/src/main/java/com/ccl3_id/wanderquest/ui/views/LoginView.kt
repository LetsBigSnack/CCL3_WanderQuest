package com.ccl3_id.wanderquest.ui.views

import android.app.Activity
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ccl3_id.wanderquest.CharactersActivity
import com.ccl3_id.wanderquest.CreationActivity
import com.ccl3_id.wanderquest.MainActivity
import com.ccl3_id.wanderquest.R
import com.ccl3_id.wanderquest.ui.composables.ButtonSettings
import com.ccl3_id.wanderquest.ui.composables.MultiStyleText
import com.ccl3_id.wanderquest.ui.composables.WanderButton
import com.ccl3_id.wanderquest.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(loginViewModel: LoginViewModel, hasPermissions: MutableState<Boolean>) {
    //TODO remove Context variable
    val context= LocalContext.current;
    val state = loginViewModel.loginViewState.collectAsState()

    val players = state.value.players;



        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0f, 0f, 0f, 0.25f)),
                horizontalArrangement = Arrangement.Center
            ) {
                MultiStyleText("Wander", MaterialTheme.colorScheme.primary, "Quest", Color.White)
            }
            Image(
                painter = painterResource(id = R.drawable.wanderquestlogo),
                contentDescription = "Box",
                modifier = Modifier.size(300.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            if (!hasPermissions.value) {

                Column(modifier = Modifier.fillMaxSize().padding(10.dp) ) {
                    Text(
                        text = "Please enable all Permission!",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG
                    )
                    Text(
                        text = "- Location",
                        color = Color.White,
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                    Text(
                        text = "- Activity",
                        color = Color.White,
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                    Text(
                        text = "- Notification ( >= Android 13)",
                        color = Color.White,
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                    Text(
                        text = "Restart App after enabling Permissions",
                        color = Color.White,
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                }

            } else {


                var createButtonColor = MaterialTheme.colorScheme.onSecondaryContainer
                if (players.isNotEmpty()) {

                    val lastPlayed = players.find { player -> player.lastPlayed };

                    if (lastPlayed != null) {

                        var playerName = if (lastPlayed.playerName.length > 10) {
                            lastPlayed.playerName.substring(0, 10) + "..."
                        } else {
                            lastPlayed.playerName
                        }

                        WanderButton(
                            text = "Continue: $playerName",
                            color = MaterialTheme.colorScheme.primary,
                            onClickEvent = {
                                val intent = Intent(context, MainActivity::class.java);
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent);
                            },
                            fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                            textColor = Color.White
                        )

                    }

                    WanderButton(
                        text = "Characters",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        onClickEvent = {
                            val intent = Intent(context, CharactersActivity::class.java);
                            context.startActivity(intent);
                        },
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                        textColor = Color.White
                    )

                } else {
                    createButtonColor = MaterialTheme.colorScheme.primary
                }



                WanderButton(
                    text = "Create Character",
                    color = createButtonColor,
                    onClickEvent = {
                        val intent = Intent(context, CreationActivity::class.java);
                        context.startActivity(intent);
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )

                WanderButton(
                    text = "Exit",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        if (context is Activity) {
                            context.finishAffinity()
                        };
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )

            }
        }

}
