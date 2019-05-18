package com.example.sonyadmin.gameList

import kotlinx.coroutines.CoroutineScope

interface ScopeProvider {
    fun provideScope() : CoroutineScope
}