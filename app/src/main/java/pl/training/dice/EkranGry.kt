package pl.training.dice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.training.dice.ui.theme.MotywDice

}

@Preview(showBackground = true)
@Composable
private fun PodgladEkranuGry() {
    MotywDice {
    }
}

private val MARGINES = 10.dp
private val ROZMIAR_KOSCI = 80.dp
private val ROZMIAR_SUMY = 40.sp

private const val PELNA_WIDOCZNOSC = 1.0f
private const val CZESCIOWA_WIDOCZNOSC = 0.5f

private val PIERWSZY_RZAD = 0..1
private val DRUGI_RZAD = 2..4

// Obrazy kosci - indeks tablicy odpowiada identyfikatorowi pliku (liczbie oczek)
private val OBRAZY_KOSCI = intArrayOf(
    R.drawable.kosc0, R.drawable.kosc1, R.drawable.kosc2, R.drawable.kosc3,
    R.drawable.kosc4, R.drawable.kosc5, R.drawable.kosc6
)
