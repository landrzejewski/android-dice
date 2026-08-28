package pl.training.dice

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Niemutowalny opis pojedynczej kosci prezentowany na ekranie
data class WidokKosci(
    val liczbaOczek: Int,
    val identyfikatorPliku: Int,
    val dostepna: Boolean,
)

/*
    Stan gry - opakowuje logike klasy Kosc i udostepnia ja Compose w postaci obserwowalnej.
    Kosc jest mutowalna, dlatego po kazdej akcji publikowany jest nowy zrzut listy,
    ktory wyzwala rekompozycje.
*/
class StanGry(liczbaKosci: Int = LICZBA_KOSCI) {

    private val kosci = List(liczbaKosci) { Kosc(Kosc.BRAK_OCZEK) }

    var widokKosci by mutableStateOf(utworzWidok())
        private set

    // Suma liczona jest ze wszystkich kosci, takze tych zablokowanych
    val sumaOczek: Int
        get() = widokKosci.sumOf { it.liczbaOczek }

    // Rzut wykonywany jest tylko dla dostepnych kosci
    fun rzut() {
        kosci.forEach { it.rzut() }
        widokKosci = utworzWidok()
    }

    fun przelaczDostepnosc(indeks: Int) {
        val kosc = kosci[indeks]
        if (kosc.dostepna) kosc.zablokuj() else kosc.udostepnij()
        widokKosci = utworzWidok()
    }

    private fun utworzWidok() =
        kosci.map { WidokKosci(it.liczbaOczek, it.identyfikatorPliku, it.dostepna) }

    private companion object {

        const val LICZBA_KOSCI = 5

    }

}
