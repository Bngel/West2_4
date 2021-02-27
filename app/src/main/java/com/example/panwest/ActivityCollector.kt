package com.example.panwest

import android.app.Activity

object ActivityCollector {

    private val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun onlyActivity(only: Activity) {
        for (activity in activities) {
            if (!activity.isFinishing && !activity.equals(only)) {
                activity.finish()
            }
        }
        activities.clear()
    }

    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }

}