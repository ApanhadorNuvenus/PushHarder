package com.crashapss.pushharder.feature_train.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSetDao {
    @Query("SELECT * FROM ExerciseSet WHERE trainingExerciseId = :trainingExerciseId")
    fun getSetsForTrainingExercise(trainingExerciseId: String): Flow<List<ExerciseSet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExerciseSet(exerciseSet: ExerciseSet)

    @Delete
    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet)
}