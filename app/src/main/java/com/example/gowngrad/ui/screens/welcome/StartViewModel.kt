package com.example.gowngrad.ui.screens.welcome

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

    fun onStartButtonClicked(){
        Firebase.analytics.logEvent("start_button_clicked", null)
    }
}