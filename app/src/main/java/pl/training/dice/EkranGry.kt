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

@Composable
fun EkranGry(
    modifier: Modifier = Modifier,
    stanGry: StanGry = remember { StanGry() }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.tlo_gry))
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RzadKosci(stanGry, PIERWSZY_RZAD)
        RzadKosci(stanGry, DRUGI_RZAD)
        Button(
            onClick = stanGry::rzut,
            modifier = Modifier.padding(MARGINES),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.tlo_przycisku),
                contentColor = colorResource(R.color.white)
            )
        ) {
            Text(text = stringResource(R.string.rzut))
        }
        Text(
            text = stanGry.sumaOczek.toString(),
            fontSize = ROZMIAR_SUMY
        )
    }
}

@Composable
private fun RzadKosci(stanGry: StanGry, indeksy: IntRange) {
    Row {
        for (indeks in indeksy) {
            ObrazKosci(
                stanGry.widokKosci[indeks],
                { stanGry.przelaczDostepnosc(indeks) }
            )
        }
    }
}

@Composable
private fun ObrazKosci(kosc: WidokKosci, onKlik: () -> Unit) {
    Image(
        painter = painterResource(OBRAZY_KOSCI[kosc.identyfikatorPliku]),
        contentDescription = stringResource(R.string.opis_kosci),
        modifier = Modifier
            .padding(MARGINES)
            .size(ROZMIAR_KOSCI)
            .alpha(if (kosc.dostepna) PELNA_WIDOCZNOSC else CZESCIOWA_WIDOCZNOSC)
            .clickable(onClick = onKlik)
    )
}

@Preview(showBackground = true)
@Composable
private fun PodgladEkranuGry() {
    MotywDice {
        EkranGry()
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
