package br.ufu.pdm.copa2002.di

import android.content.Context
import androidx.room.Room
import br.ufu.pdm.copa2002.data.local.AppDatabase
import br.ufu.pdm.copa2002.data.local.CompetitionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()

    @Provides
    fun provideCompetitionDao(database: AppDatabase): CompetitionDao =
        database.competitionDao()
}
