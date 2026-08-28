package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
// Import z aliasem - dlugiej nazwy typu uzywamy pod wlasna, krotsza nazwa
import java.time.LocalDateTime as DataICzas

/*
    Rozdzial 01 - pakiety, importy, widocznosc

    Pakiet grupuje pliki i zapobiega konfliktom nazw. Deklaracja pakietu musi byc
    pierwsza znaczaca linia pliku, importy zaraz po niej.

    Domyslnie zaimportowane sa m.in.: kotlin.*, kotlin.collections.*, java.lang.*
    - dlatego String, Int czy listOf dzialaja bez zadnego importu.
*/
class T01Podstawy {

    // Alias importu tworzy nowa nazwe dla typu tylko w obrebie tego pliku
    @Test
    fun `alias importu pozwala uzywac typu pod inna nazwa`() {
        val termin = DataICzas.of(2026, 8, 23, 10, 30)
        assertEquals(2026, termin.year)
        assertEquals(30, termin.minute)
    }

    // Typy z pakietow domyslnych sa dostepne bez importu
    @Test
    fun `pakiety domyslne nie wymagaja importu`() {
        assertEquals(listOf(1, 2, 3), listOf(1, 2, 3)) // kotlin.collections
        assertEquals(3, "abc".length)                  // kotlin.text
    }

    /*
        Modyfikatory dostepu dla elementow globalnych (poza klasa):
            public   - widoczny wszedzie (domyslny)
            internal - widoczny w calym module (Gradle/Maven)
            private  - widoczny tylko w pliku, w ktorym go zadeklarowano
    */
    @Test
    fun `widocznosc elementow globalnych`() {
        assertEquals("publiczna", funkcjaPubliczna())
        assertEquals("modulowa", funkcjaModulowa())  // internal - ten sam modul
        assertEquals("prywatna", funkcjaPrywatna())  // private - ten sam plik
    }

    /*
        Modyfikatory dostepu dla elementow klasy:
            public    - widoczny wszedzie (domyslny)
            internal  - widoczny w calym module
            protected - widoczny w klasie i jej podklasach
            private   - widoczny tylko wewnatrz klasy
    */
    @Test
    fun `protected jest widoczny w podklasie a private nie`() {
        val pracownik = Pracownik("Jan")
        // podklasa siega po skladowa protected z klasy bazowej
        assertEquals("Jan (identyfikator: 1)", pracownik.opis())
    }

    @Test
    fun `private jest widoczny tylko wewnatrz klasy`() {
        val pracownik = Pracownik("Jan")
        // pracownik.pensja        // blad kompilacji - private
        // pracownik.identyfikator // blad kompilacji - protected, jestesmy poza hierarchia
        assertTrue(pracownik.zarabiaPowyzej(1000.0)) // dostep tylko przez metode publiczna
    }
}

// --- elementy pomocnicze ---

public fun funkcjaPubliczna() = "publiczna"

internal fun funkcjaModulowa() = "modulowa"

private fun funkcjaPrywatna() = "prywatna"

open class Osoba(val imie: String) {

    protected val identyfikator = 1 // dostepne tutaj i w podklasach

    private val pensja = 5000.0     // dostepne wylacznie w tej klasie

    fun zarabiaPowyzej(kwota: Double) = pensja > kwota
}

class Pracownik(imie: String) : Osoba(imie) {

    // podklasa ma dostep do skladowej protected, ale nie do private
    fun opis() = "$imie (identyfikator: $identyfikator)"
}
