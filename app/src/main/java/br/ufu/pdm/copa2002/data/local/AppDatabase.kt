package br.ufu.pdm.copa2002.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.ufu.pdm.copa2002.data.local.entity.CoachEntity
import br.ufu.pdm.copa2002.data.local.entity.CompetitionMetaEntity
import br.ufu.pdm.copa2002.data.local.entity.PlayerEntity
import br.ufu.pdm.copa2002.data.local.entity.TeamEntity

@Database(
    entities = [
        CompetitionMetaEntity::class,
        TeamEntity::class,
        CoachEntity::class,
        PlayerEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao

    companion object {
        const val NAME = "copa2002.db"
    }
}
