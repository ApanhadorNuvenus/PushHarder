package com.croshapss.pushharder.feature_train.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.croshapss.pushharder.feature_train.domain.model.Exercise
import com.croshapss.pushharder.feature_train.domain.model.ExerciseSet
import com.croshapss.pushharder.feature_train.domain.model.Training
import com.croshapss.pushharder.feature_train.domain.model.TrainingExercise

@Database(
    entities = [Training::class, Exercise::class, TrainingExercise::class, ExerciseSet::class],
    version = 3 // Incremented database version
)
abstract class TrainDatabase : RoomDatabase() {

    abstract val exerciseSetDao: ExerciseSetDao
    abstract val trainingDao: TrainingDao
    abstract val exerciseDao: ExerciseDao
    abstract val trainingExerciseDao: TrainingExerciseDao

    companion object {
        const val DATABASE_NAME = "train_db"
    }
}