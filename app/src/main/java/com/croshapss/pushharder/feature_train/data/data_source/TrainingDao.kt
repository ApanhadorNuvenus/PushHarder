package com.croshapss.pushharder.feature_train.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.croshapss.pushharder.feature_train.domain.model.Training
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDao {
    @Query("SELECT * FROM training")
    fun getAllTrainings(): Flow<List<Training>>

    @Query("SELECT * FROM training WHERE id = :trainingId")
    suspend fun getTrainingById(trainingId: String): Training? // Changed parameter type to String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTraining(training: Training)

    @Delete
    suspend fun deleteTraining(training: Training)

    @Update
    suspend fun updateTraining(training: Training)
}