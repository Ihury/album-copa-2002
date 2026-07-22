package br.ufu.pdm.copa2002.di

import br.ufu.pdm.copa2002.data.repository.MockCompetitionRepository
import br.ufu.pdm.copa2002.domain.repository.CompetitionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Liga a interface de domínio à sua implementação concreta.
 * @Binds (módulo abstrato) é a forma mais performática de associar
 * Interface -> Implementação — o DIP (Inversão de Dependência) materializado.
 *
 * Para trocar a fonte de dados (ex.: FirebaseCompetitionRepository), basta
 * alterar a implementação vinculada aqui; nenhuma outra camada muda.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompetitionRepository(
        impl: MockCompetitionRepository
    ): CompetitionRepository
}
