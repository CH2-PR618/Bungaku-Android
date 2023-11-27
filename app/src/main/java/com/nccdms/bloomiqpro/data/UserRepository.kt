package com.nccdms.bloomiqpro.data

import com.nccdms.bloomiqpro.data.local.pref.UserPreferences

class UserRepository private constructor(
    private val userPreferences: UserPreferences
){
}