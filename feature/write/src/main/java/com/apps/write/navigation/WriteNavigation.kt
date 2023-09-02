package com.apps.write.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.apps.util.Constants
import com.apps.util.Screen
import com.apps.util.model.Mood
import com.apps.write.WriteScreen
import com.apps.write.WriteViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit){
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(
            name = Constants.WRITE_SCREEN_ARGUMENT_KEY
        ){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ){
        val viewModel: WriteViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState()
        val galleryState = viewModel.galleryState
        val context = LocalContext.current
        val pageNumber by remember {
            derivedStateOf {
                pagerState.currentPage
            }
        }

        LaunchedEffect(key1 = uiState){
            Log.d("SelectedDiary", "${uiState.selectedDiaryId}")
        }

        WriteScreen(
            galleryState = galleryState,
            uiState = uiState,
            pagerState = pagerState,
            moodName = {
                Mood.values()[pageNumber].name
            },
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onBackPressed = onBackPressed,
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply {
                        mood = Mood.values()[pageNumber].name
                    },
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = {message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onDateTimeUpdated = {
                viewModel.updateDateTime(zonedDateTime = it)
            },
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                Log.d("WriteViewModel", "URI : $it")
                viewModel.addImage(
                    image = it,
                    imageType = type
                )
            },
            onImageDeleteClicked = {galleryState.removeImage(it) }
        )
    }
}