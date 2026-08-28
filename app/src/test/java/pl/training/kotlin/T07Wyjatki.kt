package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/*
    Rozdzial 07 - wyjatki

    Wyjatek to zdarzenie przerywajace normalny przebieg programu. Wszystkie wyjatki dziedzicza
    po typie Throwable, ktory ma dwie wazne galezie:
        Error     - sytuacje, z ktorych nie da sie sensownie wyjsc (nie lapiemy ich)
        Exception - sytuacje, po ktorych program moze kontynuowac prace

    Wyjatek zglaszamy slowem `throw`, a obslugujemy konstrukcja try-catch.
    W Kotlinie nie ma wyjatkow kontrolowanych - nie trzeba deklarowac, co funkcja moze rzucic.
*/
class T07Wyjatki {

    @Test
    fun `funkcja zwraca wynik gdy nie ma bledu`() {
        assertEquals(5.0, podziel(10.0, 2.0), 0.0)
    }

    // Wyjatek wypuszczony na zewnatrz przerywa wykonanie funkcji wywolujacej
    @Test
    fun `funkcja zglasza wlasny wyjatek`() {
        val wyjatek = assertThrows(DzieleniePrzezZeroException::class.java) {
            podziel(10.0, 0.0)
        }

        assertEquals("Dzielenie przez zero", wyjatek.message)
    }

    // try-catch to wyrazenie - zwraca wartosc ostatniej linii wybranego bloku
    @Test
    fun `try catch jako wyrazenie zwraca wartosc`() {
        val wynik = try {
            podziel(10.0, 0.0)
        } catch (_: DzieleniePrzezZeroException) {
            0.0 // wartosc awaryjna
        }

        assertEquals(0.0, wynik, 0.0)
    }

    // Bloki catch sprawdzane sa po kolei - od typu najbardziej szczegolowego
    @Test
    fun `kolejnosc blokow catch`() {
        val slad = mutableListOf<String>()

        val wynik = try {
            podziel(10.0, 0.0)
        } catch (_: DzieleniePrzezZeroException) {
            slad.add("szczegolowy")
            -1.0
        } catch (_: Throwable) { // zlapalby kazdy inny wyjatek
            slad.add("ogolny")
            -2.0
        }

        assertEquals(-1.0, wynik, 0.0)
        assertEquals(listOf("szczegolowy"), slad)
    }

    // Blok finally wykonuje sie zawsze - niezaleznie od tego, czy byl wyjatek
    @Test
    fun `finally wykonuje sie zawsze`() {
        val slad = mutableListOf<String>()

        try {
            podziel(10.0, 2.0)
            slad.add("bez bledu")
        } finally {
            slad.add("finally")
        }

        try {
            podziel(10.0, 0.0)
        } catch (_: DzieleniePrzezZeroException) {
            slad.add("blad")
        } finally {
            slad.add("finally")
        }

        assertEquals(listOf("bez bledu", "finally", "blad", "finally"), slad)
    }

    // Zlapanie po nadtypie dziala dla wszystkich jego podtypow
    @Test
    fun `lapanie po nadtypie`() {
        val komunikat = try {
            throw DzieleniePrzezZeroException()
        } catch (wyjatek: Exception) { // DzieleniePrzezZeroException dziedziczy po Exception
            "obsluzono: ${wyjatek.message}"
        }

        assertEquals("obsluzono: Dzielenie przez zero", komunikat)
    }

    // Wyjatki z biblioteki standardowej zglaszamy tak samo jak wlasne
    @Test
    fun `wyjatki wbudowane`() {
        assertThrows(IllegalArgumentException::class.java) {
            require(false) { "wartosc musi byc dodatnia" } // skrot na rzucenie wyjatku
        }
        assertThrows(IndexOutOfBoundsException::class.java) {
            listOf(1, 2)[5]
        }
    }
}

// --- elementy pomocnicze ---

class DzieleniePrzezZeroException : Exception("Dzielenie przez zero")

private fun podziel(wartosc: Double, przez: Double): Double {
    if (przez == 0.0) {
        throw DzieleniePrzezZeroException()
    }
    return wartosc / przez
}
