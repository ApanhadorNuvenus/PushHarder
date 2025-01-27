package com.croshapss.pushharder.feature_train.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.croshapss.pushharder.feature_train.domain.model.TrainingExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrainingExercise(trainingExercise: TrainingExercise)

    @Query("SELECT * FROM trainingexercise WHERE trainingId = :trainingId")
    fun getExercisesForTraining(trainingId: String): Flow<List<TrainingExercise>> // Parameter type changed to String

    @Delete
    suspend fun deleteTrainingExercise(trainingExercise: TrainingExercise)
}