package com.example.apptest.di

import android.app.Application
import androidx.room.Room
import com.example.apptest.feature_train.data.data_source.TrainDatabase
import com.example.apptest.feature_train.data.repository.ExerciseRepositoryImpl
import com.example.apptest.feature_train.data.repository.ExerciseSetRepositoryImpl
import com.example.apptest.feature_train.data.repository.TrainingExerciseRepositoryImpl
import com.example.apptest.feature_train.data.repository.TrainingRepositoryImpl
import com.example.apptest.feature_train.domain.repository.ExerciseRepository
import com.example.apptest.feature_train.domain.repository.ExerciseSetRepository
import com.example.apptest.feature_train.domain.repository.TrainingExerciseRepository
import com.example.apptest.feature_train.domain.repository.TrainingRepository
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.AddExerciseSet
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.DeleteExerciseSet
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.GetSetsForTrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.AddExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.DeleteExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.GetExercises
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.GetExerciseById
import com.example.apptest.feature_train.domain.use_case.training_use_case.AddTraining
import com.example.apptest.feature_train.domain.use_case.training_use_case.DeleteTraining
import com.example.apptest.feature_train.domain.use_case.training_use_case.GetAllTrainings
import com.example.apptest.feature_train.domain.use_case.training_use_case.GetTrainingById
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.example.apptest.feature_train.domain.use_case.training_use_case.UpdateTraining
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.AddTrainingExercise
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.DeleteTrainingExercise
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.GetExercisesForTraining
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTrainDatabase(app: Application): TrainDatabase {
        return Room.databaseBuilder(
            app,
            TrainDatabase::class.java,
            TrainDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTrainingRepository(db: TrainDatabase): TrainingRepository {
        return TrainingRepositoryImpl(db.trainingDao)
    }

    @Provides
    @Singleton
    fun provideTrainingUseCases(repository: TrainingRepository) : TrainingUseCases {
        return TrainingUseCases(
            addTraining = AddTraining(repository),
            deleteTraining = DeleteTraining(repository),
            getAllTrainings = GetAllTrainings(repository),
            getTrainingById = GetTrainingById(repository),
            updateTraining = UpdateTraining(repository)
        )
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(db: TrainDatabase): ExerciseRepository {
        return ExerciseRepositoryImpl(db.exerciseDao)
    }

    @Provides
    @Singleton
    fun provideExerciseUseCases(repository: ExerciseRepository) : ExerciseUseCases {
        return ExerciseUseCases(
            addExercise = AddExercise(repository),
            deleteExercise = DeleteExercise(repository),
            getAllExercises = GetExercises(repository),
            getExerciseById = GetExerciseById(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTrainingExerciseRepository(db: TrainDatabase): TrainingExerciseRepository {
        return TrainingExerciseRepositoryImpl(db.trainingExerciseDao)
    }

    @Provides
    @Singleton
    fun provideTrainingExerciseUseCases(
        trainingExerciseRepository: TrainingExerciseRepository
    ): TrainingExerciseUseCases {
        return TrainingExerciseUseCases(
            addTrainingExercise = AddTrainingExercise(trainingExerciseRepository),
            deleteTrainingExercise = DeleteTrainingExercise(trainingExerciseRepository),
            getExercisesForTraining = GetExercisesForTraining(trainingExerciseRepository)
        )
    }


    @Provides
    @Singleton
    fun provideExerciseSetRepository(db: TrainDatabase): ExerciseSetRepository {
        return ExerciseSetRepositoryImpl(db.exerciseSetDao)
    }

    @Provides
    @Singleton
    fun provideExerciseSetUseCases(repository: ExerciseSetRepository): ExerciseSetUseCases {
        return ExerciseSetUseCases(
            getSetsForTrainingExercise = GetSetsForTrainingExercise(repository),
            addExerciseSet = AddExerciseSet(repository),
            deleteExerciseSet = DeleteExerciseSet(repository)
        )
    }
}