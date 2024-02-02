package com.ccl3_id.wanderquest.ui.views

import android.app.Activity
import android.content.Intent
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
import com.ccl3_id.wanderquest.ui.composables.BigText
import com.ccl3_id.wanderquest.ui.composables.MediumText
import com.ccl3_id.wanderquest.ui.composables.MultiColorText
import com.ccl3_id.wanderquest.ui.composables.WanderMenuButton
import com.ccl3_id.wanderquest.ui.composables.WanderMenuButtonCancel
import com.ccl3_id.wanderquest.ui.composables.WanderMenuButtonSecondary
import com.ccl3_id.wanderquest.viewModels.LoginViewModel


/**
 * Composable function that represents the login view screen.
 *
 * @param loginViewModel ViewModel associated with the login screen to manage its state.
 * @param hasPermissions Mutable state representing whether the necessary permissions are granted.
 */
@Composable
fun LoginView(loginViewModel: LoginViewModel, hasPermissions: MutableState<Boolean>) {

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

            DisplayWanderQuestLogo()

            if (!hasPermissions.value) {

                ErrorMessage();

            } else {

                if (players.isNotEmpty()) {

                    val lastPlayed = players.find { player -> player.lastPlayed };

                    if (lastPlayed != null) {

                        var playerName = if (lastPlayed.playerName.length > 10) {
                            lastPlayed.playerName.substring(0, 10) + "..."
                        } else {
                            lastPlayed.playerName
                        }

                        WanderMenuButton(
                            text = "Continue: $playerName",
                            onClickEvent = {
                                val intent = Intent(context, MainActivity::class.java);
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent);
                            }
                        )

                    }

                    WanderMenuButtonSecondary(
                        text = "Characters",
                        onClickEvent = {
                            val intent = Intent(context, CharactersActivity::class.java);
                            context.startActivity(intent);
                        })

                    WanderMenuButtonSecondary(
                        text = "Create Character",
                        onClickEvent = {
                            val intent = Intent(context, CreationActivity::class.java);
                            context.startActivity(intent);
                        })


                } else {

                    WanderMenuButton(
                        text = "Create Character",
                        onClickEvent = {
                            val intent = Intent(context, CreationActivity::class.java);
                            context.startActivity(intent);
                        })

                }

                WanderMenuButtonCancel(
                    text = "Exit",
                    onClickEvent = {
                        if (context is Activity) {
                            context.finishAffinity()
                        };
                    })
            }
        }
}

/**
 * Composable function that displays the WanderQuest logo.
 */
@Composable
fun DisplayWanderQuestLogo() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0f, 0f, 0f, 0.25f)),
        horizontalArrangement = Arrangement.Center
    ) {
        MultiColorText("Wander", MaterialTheme.colorScheme.primary, "Quest", Color.White)
    }
    Image(
        painter = painterResource(id = R.drawable.wanderquestlogo),
        contentDescription = "Wander Quest Logo",
        modifier = Modifier.size(300.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

/**
 * Composable function that displays an error message indicating the need for permissions.
 */
@Composable
fun ErrorMessage(){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp) ) {

        BigText(text = "Please enable all Permission!", color = MaterialTheme.colorScheme.primary)
        MediumText(text = "- Location");
        MediumText(text = "- Activity");
        MediumText(text = "- Notification ( >= Android 13)");
        MediumText(text = "Restart App after enabling Permissions");

    }
}
