@file:OptIn(ExperimentalMaterial3Api::class)

package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.pwr.app20_20_20.BottomNavigationBar
import com.pwr.app20_20_20.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HowToUseScreen (navController: NavController){
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Black
    )
    {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            ExpandableCard(title = stringResource(R.string.what_is_20_20_20_rule), description = stringResource(R.string.what_is_20_20_20_rule_description))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing)))
            ExpandableCard(title = stringResource(R.string.how_to_use_to_do_list), description = stringResource(R.string.how_to_use_to_do_list_description))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing)))
            ExpandableCard(title = stringResource(R.string.benefits_of_eye_exercises), description = stringResource(R.string.benefits_of_eye_exercises_description))
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ExpandableCard(
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.headlineSmall.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    description: String,
    descriptionFontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    descriptionFontWeight: FontWeight = FontWeight.Normal,
    shape: androidx.compose.ui.graphics.Shape = CardDefaults.elevatedShape,
    padding: Dp = dimensionResource(id = R.dimen.padding)
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        },
        shape = shape,

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = description,
                    fontSize = descriptionFontSize,
                    fontWeight = descriptionFontWeight,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}