package pl.training.dice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pl.training.dice.ui.theme.MotywDice

/*
    Gra w kosci - rzut pieciema szesciennymi koscmi.
    Caly interfejs zbudowany jest w Jetpack Compose (patrz EkranGry).
*/
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotywDice {
                EkranGry()
            }
        }
    }

}
