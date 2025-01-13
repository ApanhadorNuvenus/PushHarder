package com.example.apptest.feature_train.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExerciseSet(exerciseSet: ExerciseSet)

    @Query("SELECT * FROM exerciseset WHERE teId = :teID")
    fun getExerciseSetsForTE(teID: String): Flow<List<ExerciseSet>>

    @Delete
    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet)
}