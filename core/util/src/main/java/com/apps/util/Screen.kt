package com.apps.util

import com.apps.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String){
    object Authentication: Screen(route = "auth_screen")
    object Home: Screen(route = "home_screen")
    object Write: Screen(route = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=" +
            "{$WRITE_SCREEN_ARGUMENT_KEY}"){
        fun passDiaryId(diaryId: String) =
            "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=$diaryId"
    }
}
