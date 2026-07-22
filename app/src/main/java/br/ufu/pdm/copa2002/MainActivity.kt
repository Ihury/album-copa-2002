package br.ufu.pdm.copa2002

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.ufu.pdm.copa2002.ui.navigation.AppNavigation
import br.ufu.pdm.copa2002.ui.theme.Copa2002Theme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity — toda a navegação acontece em Compose via Navigation Compose.
 * @AndroidEntryPoint habilita a injeção de dependências do Hilt nesta Activity
 * e nos ViewModels obtidos por hiltViewModel() dentro dela.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Copa2002Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
