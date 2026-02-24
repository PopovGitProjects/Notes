package com.popov.dev.notes.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.popov.dev.notes.presentation.screens.creation.CreateNoteScreen
import com.popov.dev.notes.presentation.screens.notes.NoteScreen
import com.popov.dev.notes.presentation.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
//                NoteScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "onNoteClickCallbackMessage: $it")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity", "onAddNoteCallbackMessage")
//                    }
//                )
                CreateNoteScreen(onFinished = {
                    Log.d("MainActivity", "Create Note")
                })
            }
        }
    }
}