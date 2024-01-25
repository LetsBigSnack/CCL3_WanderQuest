package com.ccl3_id.wanderquest.ui.views


import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ccl3_id.wanderquest.LoginActivity
import com.ccl3_id.wanderquest.viewModels.CharacterViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.clip
import com.ccl3_id.wanderquest.ui.composables.ButtonSettings
import com.ccl3_id.wanderquest.ui.theme.RobotoFontFamily
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.ccl3_id.wanderquest.ui.composables.WanderButton

@Composable
fun CharacterView(characterViewModel: CharacterViewModel, context: Context) {

    val state = characterViewModel.characterViewState.collectAsState()
    //TODO switch everything to LocalContext
    val context= LocalContext.current;

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters",fontSize = ButtonSettings.BUTTON_FONT_SIZE_MASSIVE,
                    fontFamily = RobotoFontFamily, color = MaterialTheme.colorScheme.primary)},
                navigationIcon = {
                    IconButton(onClick = { val intent = Intent(context, LoginActivity::class.java); context.startActivity(intent);  }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.secondary
            )
        }
    ) { contentPadding ->

        // https://developer.android.com/jetpack/compose/lists
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(top = 10.dp)
        ) {

            items(state.value.characters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            characterViewModel.selectCharacterToPlay(it);
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)) {

                        var playerName = if (it.playerName.length > 10) {
                            it.playerName.substring(0, 10) + "..."
                        } else {
                            it.playerName
                        }

                        if(it.lastPlayed){
                            Text(
                                text = playerName,
                                fontSize = 28.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }else{
                            Text(
                                text = playerName,
                                fontSize = 28.sp,
                                color = Color.White
                            )
                        }

                        Text(text = "Class: ${it.playerClass}", fontSize = 20.sp, color = Color.White)
                        Text(text = "Level: ${it.playerLevel}", fontSize = 20.sp, color = Color.White)
                    }
                    val imageResource = painterResource(id = com.ccl3_id.wanderquest.R.drawable.pencil)
                    IconButton(
                        onClick = {
                            characterViewModel.selectEditCharacter(it);
                        }) {
                        Icon(imageResource, "Update",  tint = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(32.dp))
                    }
                    IconButton(
                        onClick = {
                            characterViewModel.selectDeleteCharacter(it);
                        }) {
                        Icon(Icons.Default.Delete, "Delete", tint =  MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
        Column {
            editCharacterModal(characterViewModel)
        }
        Column {
            deleteCharacterModal(characterViewModel)
        }
        Column {
            selectCharacterModal(characterViewModel)
        }
    }
}

@Composable
fun deleteCharacterModal(characterViewModel: CharacterViewModel) {

    val state = characterViewModel.characterViewState.collectAsState()

    if(state.value.openPlayerDeleteDialog && state.value.deletePlayer != null){
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = {
                characterViewModel.dismissDialog()
            },
            title = {
                Text(text = "Delete Character", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)

            },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(text = "Are you sure you want to delete this character?", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                }
            },
            confirmButton = {

                WanderButton(
                    text = "Yes",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        characterViewModel.deleteCharacter();
                        characterViewModel.dismissDialog();
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier
                )
            },
            dismissButton = {

                WanderButton(
                    text = "No",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        characterViewModel.dismissDialog()
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier
                )
            }

        )
    }
}


@Composable
fun selectCharacterModal(characterViewModel: CharacterViewModel) {

    val state = characterViewModel.characterViewState.collectAsState()
    val context = LocalContext.current

    if(state.value.openPlayerSelectDialog && state.value.selectedPlayer != null){
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = {
                characterViewModel.dismissDialog()
            },
            title = {
                Text(text = "Select Character", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)

            },
            text = {
                Column {
                    Text(text = "Do you want to select this Character to play", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                }
            },
            confirmButton = {
                WanderButton(
                    text = "Yes",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        val text = "Character selected"
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context , text, duration)

                        characterViewModel.selectCharacter();
                        characterViewModel.dismissDialog();
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier
                )
            },
            dismissButton = {
                WanderButton(
                    text = "No",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        characterViewModel.dismissDialog()
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier
                )
            }
        )
    }
}

@Composable
fun editCharacterModal(characterViewModel: CharacterViewModel) {

    val state = characterViewModel.characterViewState.collectAsState()
    val context= LocalContext.current;

    if(state.value.openPlayerEditDialog && state.value.editPlayer != null){

        var name by rememberSaveable { mutableStateOf(state.value.editPlayer!!.playerName) }
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = {
                characterViewModel.dismissDialog()
            },
            title = {
                Text(text = "Update Character", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
            },
            text = {
                Column {
                    TextField(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        value = name,
                        onValueChange = { newText -> name = newText },
                        label = {
                            Text(
                                text = "Character Name:",
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        },
                        //TODO cursor color
                        textStyle = TextStyle(color = Color.White, fontSize = 24.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            },
            confirmButton = {

                WanderButton(
                    text = "Update",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        if(name.isNullOrEmpty()){
                            val text = "Please fill out all options"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context , text, duration) // in Activity
                            toast.show()
                        }else{

                            var tempPlayer = state.value.editPlayer;
                            if (tempPlayer != null) {
                                tempPlayer.playerName = name;
                                characterViewModel.selectEditCharacter(tempPlayer);
                                characterViewModel.updateCharacter();
                                characterViewModel.dismissDialog();
                            };
                        }
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier
                )

            }

        )
    }
}