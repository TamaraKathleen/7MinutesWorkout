package eu.android.a7minutesworkout

import android.app.Application
import eu.android.a7minutesworkout.database.HistoryDatabase

class WorkOutApp: Application() {
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}