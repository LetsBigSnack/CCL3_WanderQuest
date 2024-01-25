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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import com.ccl3_id.wanderquest.MainActivity
import com.ccl3_id.wanderquest.viewModels.LevelUpViewModel
import kotlin.system.exitProcess
import androidx.compose.material3.Scaffold
import com.ccl3_id.wanderquest.ui.composables.ButtonSettings
import com.ccl3_id.wanderquest.ui.theme.RobotoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpView(levelUpViewModel: LevelUpViewModel, context: Context) {

    val state = levelUpViewModel.levelUpViewState.collectAsState()
    val player = state.value.selectedPlayer;
    val statPoints = player!!.playerAttributePoints;
    val stats = state.value.stats;
    val context= LocalContext.current;


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Level Up!",fontSize = ButtonSettings.BUTTON_FONT_SIZE_MASSIVE,
                        fontFamily = RobotoFontFamily, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(
                            context,
                            MainActivity::class.java
                        ); context.startActivity(intent);
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.secondary
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Stats", fontSize = 40.sp)
            Text(text = "Available Stat points: $statPoints", fontSize = 20.sp)

            stats.forEach { (statName,statValue) ->
                Stats(statName, statValue, levelUpViewModel);
            };

            Button(
                onClick = {
                    levelUpViewModel.saveStats();
                    val intent = Intent(
                        context,
                        MainActivity::class.java
                    ); context.startActivity(intent);
                },
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Save Stats", fontSize = 25.sp)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stats(statName : String, statValue: Int, levelUpViewModel: LevelUpViewModel){

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
        IconButton(onClick = { levelUpViewModel.subStat(statName) },
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
            Text(text = "$statValue", fontSize = 25.sp)
        }
        IconButton(onClick = {levelUpViewModel.addStat(statName) },
            modifier = Modifier
                .size(20.dp) // Set the size of the IconButton
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape) // Set a round background
        ) {
            Icon(Icons.Default.ArrowForward,"Add")
        }
    }

}