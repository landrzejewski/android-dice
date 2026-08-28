package pl.training.dice

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/*
    Testy jednostkowe klasy Kosc - kazdy przypadek testowy sprawdzany jest osobna metoda.
*/
class KoscTest {

    private companion object {
        const val LICZBA_POWTORZEN = 1_000
        const val WARTOSC_NIEPOPRAWNA = 7
    }

    @Test
    fun `rzut zwraca wartosc z zakresu od jeden do szesc`() {
        val kosc = Kosc()

        repeat(LICZBA_POWTORZEN) {
            kosc.rzut()

            assertTrue(
                "Liczba oczek poza zakresem: ${kosc.liczbaOczek}",
                kosc.liczbaOczek in Kosc.MIN_LICZBA_OCZEK..Kosc.MAX_LICZBA_OCZEK
            )
        }
    }

    @Test
    fun `rzut nie zmienia wartosci gdy kosc jest niedostepna`() {
        val kosc = Kosc(3)
        kosc.zablokuj()

        repeat(LICZBA_POWTORZEN) {
            kosc.rzut()
        }

        assertEquals(3, kosc.liczbaOczek)
        assertEquals(3, kosc.identyfikatorPliku)
    }

    @Test
    fun `rzut zmienia identyfikator pliku razem z liczba oczek`() {
        val kosc = Kosc()

        repeat(LICZBA_POWTORZEN) {
            kosc.rzut()

            assertEquals(kosc.liczbaOczek, kosc.identyfikatorPliku)
        }
    }

    @Test
    fun `konstruktor jednoargumentowy przyjmuje poprawna wartosc`() {
        val kosc = Kosc(5)

        assertEquals(5, kosc.liczbaOczek)
        assertEquals(5, kosc.identyfikatorPliku)
        assertTrue(kosc.dostepna)
    }

    @Test
    fun `konstruktor jednoargumentowy ustawia zero dla niepoprawnej wartosci`() {
        val kosc = Kosc(WARTOSC_NIEPOPRAWNA)

        assertEquals(Kosc.BRAK_OCZEK, kosc.liczbaOczek)
        assertEquals(Kosc.BRAK_OCZEK, kosc.identyfikatorPliku)
    }

    @Test
    fun `konstruktor bezargumentowy losuje wartosc z zakresu od jeden do szesc`() {
        repeat(LICZBA_POWTORZEN) {
            val kosc = Kosc()

            assertTrue(kosc.liczbaOczek in Kosc.MIN_LICZBA_OCZEK..Kosc.MAX_LICZBA_OCZEK)
            assertTrue(kosc.dostepna)
        }
    }

    // Pole z licznikiem instancji jest statyczne, dlatego sprawdzany jest przyrost jego wartosci
    @Test
    fun `kazdy konstruktor zwieksza licznik instancji`() {
        val licznikPoczatkowy = Kosc.liczbaInstancji

        Kosc()
        Kosc(2)

        assertEquals(licznikPoczatkowy + 2, Kosc.liczbaInstancji)
    }

    @Test
    fun `zablokowanie i udostepnienie zmienia dostepnosc kosci`() {
        val kosc = Kosc(1)

        kosc.zablokuj()
        assertFalse(kosc.dostepna)

        kosc.udostepnij()
        assertTrue(kosc.dostepna)
    }

    @Test
    fun `nazwa pliku odpowiada liczbie oczek`() {
        assertEquals("kosc0.png", Kosc(WARTOSC_NIEPOPRAWNA).nazwaPliku)
        assertEquals("kosc3.png", Kosc(3).nazwaPliku)
        assertEquals("kosc6.png", Kosc(6).nazwaPliku)
    }

    @Test
    fun `opis slowny zwraca liczbe oczek w postaci tekstu`() {
        assertEquals("zero", Kosc(WARTOSC_NIEPOPRAWNA).opisSlowny())
        assertEquals("trzy", Kosc(3).opisSlowny())
        assertEquals("szesc", Kosc(6).opisSlowny())
    }

}
