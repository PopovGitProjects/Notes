package com.popov.dev.notes.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.popov.dev.notes.presentation.screens.creation.CreateNoteScreen
import com.popov.dev.notes.presentation.screens.editing.EditNoteScreen
import com.popov.dev.notes.presentation.screens.notes.NoteScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.rout
    ) {
        composable(Screen.Notes.rout) {
            NoteScreen(
                onNoteClick = {
                    navController.navigate(Screen.EditNote.createRote(it.id))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.CreateNote.rout)
                }
            )
        }
        composable(Screen.CreateNote.rout) {
            CreateNoteScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.EditNote.rout) {
            EditNoteScreen(
                notId = Screen.EditNote.getNoteId(it.arguments),
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val rout: String) {
    data object Notes : Screen("note")
    data object CreateNote : Screen("create_note")
    data object EditNote : Screen("edit_note/{note_id}") {
        fun createRote(noteId: Int): String {
            return "edit_note/$noteId"
        }
        fun getNoteId(arguments: Bundle?): Int{
            return arguments?.getString("note_id")?.toInt() ?: 0
        }
    }
}
