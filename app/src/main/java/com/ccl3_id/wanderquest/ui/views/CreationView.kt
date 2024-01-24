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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.ccl3_id.wanderquest.LoginActivity
import com.ccl3_id.wanderquest.R
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.ui.composables.ButtonSettings
import com.ccl3_id.wanderquest.ui.composables.WanderButton
import com.ccl3_id.wanderquest.viewModels.CreationViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationView(creationViewModel: CreationViewModel) {

    val context= LocalContext.current;
    val state = creationViewModel.creationViewState.collectAsState()
    val step = state.value.stepNumber;
    var expanded by remember { mutableStateOf(false) }

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
            painter = painterResource(id = R.drawable.wander_man),
            colorFilter = ColorFilter.tint(Color(0f, 0f, 0f, 0.15f)),
            contentDescription = "Background Image for Character Creation",
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-175).dp, y = (150).dp),
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "Name", fontSize = 40.sp, color =  MaterialTheme.colorScheme.primary)
            TextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                value = characterName,
                onValueChange = { newChar -> creationViewModel.changeCharacterName(newChar)  },
                label = {
                    Text(
                        text = "Character Name",
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

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.padding(bottom = 28.dp)) {

                WanderButton(
                    text = "Next Step",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = { creationViewModel.nextStep(); },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White,
                    enabled = !characterName.isNullOrEmpty()
                )

                WanderButton(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        val intent = Intent(context, LoginActivity::class.java);
                        context.startActivity(intent);
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )
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
            painter = painterResource(id = R.drawable.wander_woman),
            colorFilter = ColorFilter.tint(Color(0f, 0f, 0f, 0.15f)),
            contentDescription = "Background Image for Character Creation",
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 100.dp, y = (150).dp),
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "Class", fontSize = 40.sp, color =  MaterialTheme.colorScheme.primary)

            var buttonText = ""

            if (characterClass != "") {
               buttonText = characterClass
            } else {
                buttonText = "Select Class"
            }

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {

                WanderButton(
                    text = buttonText,
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        onExpandedChange(true)
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )

                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondaryContainer),
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
                                color = Color.White,
                                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                            )
                        }
                    }
                }
            }

            if(characterClass != "") {
                //TODO rework this View
                Player.CLASS_ATTRIBUTES[characterClass]?.let { Text(text = "Preferred Stats: $it", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                Player.CLASS_DESCRIPTION[characterClass]?.let { Text(text = it, fontSize = 20.sp) }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.padding(bottom = 28.dp)) {

                WanderButton(
                    text = "Next Step",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = { creationViewModel.nextStep(); },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White,
                    enabled =  !characterClass.isNullOrEmpty()
                )

                WanderButton(
                    text = "Previous Step",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        creationViewModel.previousStep();
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )
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
            painter = painterResource(id = R.drawable.wander_man),
            colorFilter = ColorFilter.tint(Color(0f, 0f, 0f, 0.15f)),
            contentDescription = "Background Image for Character Creation",
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-175).dp, y = (150).dp),
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "Stats", fontSize = 40.sp, color =  MaterialTheme.colorScheme.primary)
            Text(text = "Available Stat points: $statPoints", fontSize = 24.sp, color =  Color.White)

            Spacer(modifier = Modifier.height(48.dp))

            stats.forEach { (statName, statValue) ->
                StatAllocation(statName, statValue, creationViewModel);
            };


            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.padding(bottom = 28.dp)) {

                WanderButton(
                    text = "Next Step",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = { creationViewModel.nextStep(); },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White,
                    enabled =  statPoints == 0
                )

                WanderButton(
                    text = "Previous Step",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        creationViewModel.previousStep();
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )
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
            painter = painterResource(id = R.drawable.wander_woman),
            colorFilter = ColorFilter.tint(Color(0f, 0f, 0f, 0.15f)),
            contentDescription = "Background Image for Character Creation",
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 100.dp, y = (150).dp),
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            //TODO make more fancy
            Text(text = "Review", fontSize = 40.sp, color =  MaterialTheme.colorScheme.primary)

            Column(modifier = Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.Start) {
                Text(text = "Name: ${state.value.characterName}", fontSize = 24.sp, color = Color.White)
                Text(text = "Class: ${state.value.characterClass}", fontSize =  24.sp, color = Color.White)
                Text(text = "Stats: ", fontSize =  24.sp, color = Color.White)
                state.value.stats.forEach { (statName, statValue) ->
                    Text(text = "${statName} : ${statValue} ", fontSize = 20.sp, color = Color.White)
                };

            }

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.padding(bottom = 28.dp)) {

                WanderButton(
                    text = "Create Character",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        creationViewModel.createCharacter()
                        val intent = Intent(context, LoginActivity::class.java);
                        context.startActivity(intent);
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )

                WanderButton(
                    text = "Previous Step",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClickEvent = {
                        creationViewModel.previousStep();
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                    textColor = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatAllocation(statName : String, statPoint : Int, creationViewModel : CreationViewModel){

    //TODO horzontal Arrangement is a bit fucked
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Column (modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$statName", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Row(modifier = Modifier
            .weight(0.8f)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,)
        {
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
                Text(text = "$statPoint", fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG, color = Color.White)
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

}
