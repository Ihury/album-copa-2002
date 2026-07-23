package br.ufu.pdm.copa2002

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

/**
 * Ponto de entrada da aplicação. A anotação @HiltAndroidApp inicializa o
 * container de injeção de dependências (gera o SingletonComponent do Hilt).
 *
 * Implementa [ImageLoaderFactory] para fornecer um ImageLoader global do Coil
 * com um User-Agent explícito — a Wikimedia Commons responde 403 a requisições
 * sem User-Agent, então as fotos de jogadores/técnicos só carregam com ele.
 */
@HiltAndroidApp
class AlbumApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Copa2002Album/1.0 (UFU PDM; projeto educacional)")
                    .build()
                chain.proceed(request)
            }
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .crossfade(true)
            .build()
    }
}
