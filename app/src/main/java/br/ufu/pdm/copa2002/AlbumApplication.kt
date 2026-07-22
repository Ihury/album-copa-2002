package br.ufu.pdm.copa2002

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Ponto de entrada da aplicação. A anotação @HiltAndroidApp inicializa o
 * container de injeção de dependências (gera o SingletonComponent do Hilt).
 */
@HiltAndroidApp
class AlbumApplication : Application()
