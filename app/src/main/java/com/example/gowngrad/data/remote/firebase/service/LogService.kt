package com.example.gowngrad.data.remote.firebase.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}