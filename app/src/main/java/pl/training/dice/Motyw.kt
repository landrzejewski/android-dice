package pl.training.dice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/*
    Motyw aplikacji - zastepuje motyw definiowany dotad w res/values/themes.xml.
*/
@Composable
fun MotywDice(
    ciemnyMotyw: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (ciemnyMotyw) darkColorScheme() else lightColorScheme(),
        content = content,
    )
}
