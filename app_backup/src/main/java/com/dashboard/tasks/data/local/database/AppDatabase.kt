package com.dashboard.tasks.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dashboard.tasks.data.local.entity.TaskEntity
import com.dashboard.tasks.data.local.entity.UserEntity

@Database(
    entities = [TaskEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "task_dashboard_db"
    }
}
