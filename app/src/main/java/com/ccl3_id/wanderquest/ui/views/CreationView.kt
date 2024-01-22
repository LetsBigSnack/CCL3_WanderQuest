package com.ccl3_id.wanderquest.ui.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.ccl3_id.wanderquest.LoginActivity
import com.ccl3_id.wanderquest.R
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.viewModels.CreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationView(creationViewModel: CreationViewModel, context: Context) {

    val state = creationViewModel.creationViewState.collectAsState()
    val step = state.value.stepNumber;
    var characterName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf("") }

    when(step) {
        1 ->  StepOneName(
            creationViewModel,
            context
        )
        2 -> StepTwoClass(
            expanded = expanded,
            onExpandedChange = { newExpanded -> expanded = newExpanded },
            creationViewModel = creationViewModel
        )
        3 -> StepThreeStats(creationViewModel = creationViewModel)
        4 -> StepFourReview(creationViewModel = creationViewModel, context)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOneName(creationViewModel: CreationViewModel, context: Context)
{
    val state = creationViewModel.creationViewState.collectAsState()
    val characterName = state.value.characterName;

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        // Background image
        Image(
            painter = painterResource(id = R.drawable.wander_bg_m),
            contentDescription = null,  // decorative image
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // or choose another scaling option
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Name", fontSize = 40.sp)
            TextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                value = characterName,
                onValueChange = { newChar -> creationViewModel.changeCharacterName(newChar)  },
                label = {
                    Text(
                        text = "Character Name",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
            )
            Button(
                onClick = { creationViewModel.nextStep(); },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                enabled = !characterName.isNullOrEmpty()
            ) {
                Text(text = "Next Step", fontSize = 25.sp)
            }
            Button(
                onClick = { val intent = Intent(context, LoginActivity::class.java);
                    context.startActivity(intent);  },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Cancel", fontSize = 25.sp)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoClass(expanded : Boolean, onExpandedChange: (Boolean) -> Unit,
                 creationViewModel : CreationViewModel
){

    val state = creationViewModel.creationViewState.collectAsState()
    val characterClass = state.value.characterClass;

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        // Background image
        Image(
            painter = painterResource(id = R.drawable.wander_bg_f),
            contentDescription = null,  // decorative image
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // or choose another scaling option
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Class", fontSize = 40.sp)
            Box (
            ) {
                Button(onClick = { onExpandedChange(true) }) {

                    if (characterClass != "") {
                        Text(text = characterClass)
                    } else {
                        Text(text = "Select Class")
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    properties = PopupProperties(focusable = true)
                ) {

                    Player.CLASS_LIST.forEach { obj ->
                        DropdownMenuItem(
                            onClick = {
                                creationViewModel.selectClass(obj)
                                onExpandedChange(false)
                            }
                        ) {
                            Text(
                                text = obj,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            if(characterClass != "") {
                Player.CLASS_ATTRIBUTES[characterClass]?.let { Text(text = "Preferred Stats: $it", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                Player.CLASS_DESCRIPTION[characterClass]?.let { Text(text = it, fontSize = 20.sp) }
            }

            Button(
                onClick = { creationViewModel.nextStep(); },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                enabled = !characterClass.isNullOrEmpty()
            ) {
                Text(text = "Next Step", fontSize = 25.sp)
            }
            Button(
                onClick = {  creationViewModel.previousStep() },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Previous Step", fontSize = 25.sp)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepThreeStats(creationViewModel : CreationViewModel){
    val state = creationViewModel.creationViewState.collectAsState()
    val statPoints = state.value.statPoints;
    val stats = state.value.stats;

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.wander_bg_m),
            contentDescription = null,  // decorative image
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // or choose another scaling option
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Stats", fontSize = 40.sp)
            Text(text = "Available Stat points: $statPoints", fontSize = 20.sp)

            stats.forEach { (statName, statValue) ->
                StatAllocation(statName, statValue, creationViewModel);
            };

            Button(
                onClick = { creationViewModel.nextStep(); },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Next Step", fontSize = 25.sp)
            }
            Button(
                onClick = {  creationViewModel.previousStep() },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Previous Step", fontSize = 25.sp)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFourReview(creationViewModel: CreationViewModel, context: Context){

    val state = creationViewModel.creationViewState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.wander_bg_f),
            contentDescription = null,  // decorative image
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // or choose another scaling option
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Review", fontSize = 40.sp)
            Text(text = "Name: ${state.value.characterName}", fontSize = 20.sp)
            Text(text = "Class: ${state.value.characterClass}", fontSize = 20.sp)
            Text(text = "Stats: ", fontSize = 20.sp)
            state.value.stats.forEach { (statName, statValue) ->
                Text(text = "${statName} : ${statValue} ", fontSize = 15.sp)
            };
            Button(
                onClick = { creationViewModel.createCharacter()
                    val intent = Intent(context, LoginActivity::class.java);
                    context.startActivity(intent);

                },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Create Character", fontSize = 25.sp)
            }
            Button(
                onClick = {  creationViewModel.previousStep() },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Previous Step", fontSize = 25.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatAllocation(statName : String, statPoint : Int, creationViewModel : CreationViewModel){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Column (modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$statName", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = { creationViewModel.subStat(statName) },
            modifier = Modifier
                .size(20.dp) // Set the size of the IconButton
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)) {
            Icon(Icons.Default.ArrowBack,"Subtract")
        }
        Column (modifier = Modifier
            .weight(1f)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$statPoint", fontSize = 25.sp)
        }
        IconButton(onClick = { creationViewModel.addStat(statName) },
            modifier = Modifier
                .size(20.dp) // Set the size of the IconButton
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape) // Set a round background
        ) {
            Icon(Icons.Default.ArrowForward,"Add")
        }
    }

}
