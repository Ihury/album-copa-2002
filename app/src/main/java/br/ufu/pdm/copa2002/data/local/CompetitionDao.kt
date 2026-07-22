package br.ufu.pdm.copa2002.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.ufu.pdm.copa2002.data.local.entity.CoachEntity
import br.ufu.pdm.copa2002.data.local.entity.CompetitionMetaEntity
import br.ufu.pdm.copa2002.data.local.entity.PlayerEntity
import br.ufu.pdm.copa2002.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompetitionDao {

    // Leituras reativas (Flow) — a UI recompõe automaticamente a cada mudança.
    @Query("SELECT * FROM competition_meta WHERE id = ${CompetitionMetaEntity.SINGLE_ROW_ID}")
    fun observeMeta(): Flow<CompetitionMetaEntity?>

    @Transaction
    @Query("SELECT * FROM teams")
    fun observeTeamsWithDetails(): Flow<List<TeamWithDetails>>

    // Leituras pontuais para as telas de detalhe.
    @Transaction
    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getTeamWithDetails(teamId: String): TeamWithDetails?

    @Query("SELECT * FROM players WHERE id = :playerId")
    suspend fun getPlayer(playerId: String): PlayerEntity?

    @Query("SELECT * FROM coaches WHERE id = :coachId")
    suspend fun getCoach(coachId: String): CoachEntity?

    @Query("SELECT COUNT(*) FROM teams")
    suspend fun teamCount(): Int

    // Escritas.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeta(meta: CompetitionMetaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCoaches(coaches: List<CoachEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlayers(players: List<PlayerEntity>)

    @Query("DELETE FROM teams")
    suspend fun clearTeams()

    @Query("DELETE FROM coaches")
    suspend fun clearCoaches()

    @Query("DELETE FROM players")
    suspend fun clearPlayers()

    /**
     * Substitui todo o acervo em uma única transação (usado no refresh a partir
     * da fonte remota). Mantém o banco como Single Source of Truth consistente.
     */
    @Transaction
    suspend fun replaceAll(
        meta: CompetitionMetaEntity,
        teams: List<TeamEntity>,
        coaches: List<CoachEntity>,
        players: List<PlayerEntity>
    ) {
        clearPlayers()
        clearCoaches()
        clearTeams()
        upsertMeta(meta)
        upsertTeams(teams)
        upsertCoaches(coaches)
        upsertPlayers(players)
    }
}
