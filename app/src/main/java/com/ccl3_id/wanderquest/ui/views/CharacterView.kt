package com.ccl3_id.wanderquest.ui.views


import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
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

@Composable
fun CharacterView(characterViewModel: CharacterViewModel, context: Context) {

    val state = characterViewModel.characterViewState.collectAsState()
    //TODO switch everything to LocalContext
    val context= LocalContext.current;

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters",fontSize = 30.sp,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background)},
                navigationIcon = {
                    IconButton(onClick = { val intent = Intent(context, LoginActivity::class.java); context.startActivity(intent);  }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
        ) {

            items(state.value.characters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            characterViewModel.selectCharacterToPlay(it);
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {

                        if(it.lastPlayed){
                            Text(
                                text = "${it.playerName}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
                            )
                        }else{
                            Text(
                                text = "${it.playerName}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(text = "Class: ${it.playerClass}", fontSize = 20.sp)
                        Text(text = "Level: ${it.playerLevel}", fontSize = 20.sp)
                    }
                    IconButton(modifier = Modifier
                        .size(40.dp) // Set the size of the IconButton
                        , onClick = {
                            characterViewModel.selectEditCharacter(it);
                        }) {
                        Icon(Icons.Default.Settings, "Update")
                    }
                    IconButton(modifier = Modifier
                        .size(40.dp) // Set the size of the IconButton
                        , onClick = {
                            characterViewModel.selectDeleteCharacter(it);
                        }) {
                        Icon(Icons.Default.Delete, "Delete")
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
            onDismissRequest = {
                characterViewModel.dismissDialog()
            },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(text = "Are you sure you want to delete this character?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        characterViewModel.deleteCharacter();
                        characterViewModel.dismissDialog();
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        characterViewModel.dismissDialog()
                    }
                ) {
                    Text(text = "No")
                }
            }

        )




    }
}


@Composable
fun selectCharacterModal(characterViewModel: CharacterViewModel) {

    val state = characterViewModel.characterViewState.collectAsState()

    if(state.value.openPlayerSelectDialog && state.value.selectedPlayer != null){
        AlertDialog(
            onDismissRequest = {
                characterViewModel.dismissDialog()
            },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(text = "Do you want to select this Character to play")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        characterViewModel.selectCharacter();
                        characterViewModel.dismissDialog();
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        characterViewModel.dismissDialog()
                    }
                ) {
                    Text(text = "No")
                }
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
            onDismissRequest = {
                characterViewModel.dismissDialog()
            },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(text = "Update Character" )
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = name,
                        onValueChange = { newText -> name = newText },
                        label = { Text(text = "Character Name:" ) }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {

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
                    }
                ) {
                    Text(text = "Update")
                }
            }

        )
    }
}