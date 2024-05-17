package com.test.landmarkremark.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.landmarkremark.data.models.NoteEntity


@Database(entities = [NoteEntity::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [
        //AutoMigration(from = 1, to = 2)
    ])
abstract class AppDatabase : RoomDatabase() {

}