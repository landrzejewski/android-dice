package pl.training.dice

import org.junit.Assert.assertTrue
import org.junit.Test

/*
    Testy programu glownego - wejscie i wyjscie sa podstawiane, dzieki czemu
    nie jest potrzebna klawiatura ani konsola.
*/
class KonsolaTest {

    @Test
    fun `program wyswietla stan kosci utworzonej z podanej liczby oczek`() {
        val wyjscie = uruchomZWejsciem("4")

        assertTrue(wyjscie.contains("Liczba wyrzuconych oczek: 4 (slownie: cztery)"))
        assertTrue(wyjscie.contains("Nazwa pliku z obrazem kosci: kosc4.png"))
    }

    @Test
    fun `program ustawia zero gdy podana liczba oczek jest niepoprawna`() {
        val wyjscie = uruchomZWejsciem("9")

        assertTrue(wyjscie.contains("Liczba wyrzuconych oczek: 0 (slownie: zero)"))
        assertTrue(wyjscie.contains("Nazwa pliku z obrazem kosci: kosc0.png"))
    }

    @Test
    fun `program wyswietla liczbe utworzonych instancji klasy`() {
        val licznikPoczatkowy = Kosc.liczbaInstancji

        val wyjscie = uruchomZWejsciem("2")

        assertTrue(wyjscie.contains("Liczba utworzonych instancji klasy Kosc: ${licznikPoczatkowy + 1}"))
        assertTrue(wyjscie.contains("Liczba utworzonych instancji klasy Kosc: ${licznikPoczatkowy + 2}"))
    }

    private fun uruchomZWejsciem(wejscie: String): String {
        val wyjscie = StringBuilder()

        uruchomKonsole(wejscie.reader().buffered(), wyjscie)

        return wyjscie.toString()
    }

}
