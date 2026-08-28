package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Test

/*
    Rozdzial 03 - instrukcje warunkowe i petle

    W Kotlinie `if` oraz `when` sa wyrazeniami - zwracaja wartosc, wiec czesto zastepuja
    operator warunkowy (?:) znany z Javy.
*/
class T03WarunkiIPetle {

    // `if` uzyty jako instrukcja - wykonuje kod, nic nie zwraca
    @Test
    fun `if jako instrukcja`() {
        val liczba = 5
        var opis = ""

        if (liczba % 2 == 0) {
            opis = "$liczba jest parzysta"
        } else {
            opis = "$liczba jest nieparzysta"
        }

        assertEquals("5 jest nieparzysta", opis)
    }

    // `if` uzyty jako wyrazenie - wynikiem jest ostatnie wyrazenie wybranej galezi
    @Test
    fun `if jako wyrazenie zwraca wartosc`() {
        val litera: Char = if (5 < 3) 'a' else 'b'
        val etykieta = if (5 % 2 == 0) "parzysta" else "nieparzysta"

        assertEquals('b', litera)
        assertEquals("nieparzysta", etykieta)
    }

    /*
        `when` porownuje wartosc z kolejnymi galeziami. Uzyty jako wyrazenie musi byc
        wyczerpujacy: pokrywac wszystkie przypadki albo miec galaz `else`.
    */
    @Test
    fun `when dopasowuje wartosc, zakres, typ i null`() {
        assertEquals("Jeden", opisz(1))
        assertEquals("Trzy, cztery lub piec", opisz(4)) // kilka wartosci w jednej galezi
        assertEquals("Pomiedzy 6 a 10", opisz(8))       // zakres sprawdzany operatorem `in`
        assertEquals("To tekst", opisz("abc"))          // sprawdzenie typu operatorem `is`
        assertEquals("To null", opisz(null))            // null jest zwyklym przypadkiem
        assertEquals("Cos innego", opisz(99))           // galaz else
    }

    // `when` bez argumentu dziala jak drabinka warunkow - pierwszy prawdziwy wygrywa
    @Test
    fun `when bez argumentu sprawdza warunki po kolei`() {
        assertEquals("Nie ma szans", ocen(20))
        assertEquals("Prawdopodobnie", ocen(70))
        assertEquals("Tak", ocen(90))
        assertEquals("Kto wie", ocen(120))
    }

    // Zmienna zadeklarowana w nawiasach `when` jest widoczna we wszystkich galeziach
    @Test
    fun `when moze deklarowac wlasna zmienna`() {
        fun pobierzWartosc(): Any = 5

        val wynik = when (val odpowiedz = pobierzWartosc()) {
            is Number -> "liczba ${odpowiedz.toInt() * 2}"
            is String -> "tekst o dlugosci ${odpowiedz.length}"
            else -> "inny typ"
        }

        assertEquals("liczba 10", wynik)
    }

    // Petla for przechodzi po elementach dowolnej kolekcji lub tablicy
    @Test
    fun `for po elementach tablicy`() {
        val liczby = arrayOf(1, 2, 3, 5)
        val zebrane = mutableListOf<Int>()

        for (liczba in liczby) {
            zebrane.add(liczba)
        }

        assertEquals(listOf(1, 2, 3, 5), zebrane)
    }

    // indices daje zakres indeksow, withIndex() pary indeks + wartosc
    @Test
    fun `for po indeksach oraz po parach indeks i wartosc`() {
        val liczby = arrayOf(10, 20, 30)
        val indeksy = mutableListOf<Int>()
        val opisy = mutableListOf<String>()

        for (indeks in liczby.indices) {
            indeksy.add(indeks)
        }
        for ((indeks, liczba) in liczby.withIndex()) {
            opisy.add("$indeks: $liczba")
        }

        assertEquals(listOf(0, 1, 2), indeksy)
        assertEquals(listOf("0: 10", "1: 20", "2: 30"), opisy)
    }

    /*
        Zakresy:
            1..10   - od 1 do 10 wlacznie
            1..<10  - od 1 do 9 (nowsza skladnia, rownowazna `until`)
            downTo  - zakres malejacy
            step    - co ile elementow
    */
    @Test
    fun `for po zakresach liczb`() {
        assertEquals(listOf(1, 2, 3), zbierz(1..3))
        assertEquals(listOf(1, 2), zbierz(1..<3))
        assertEquals(listOf(1, 2), zbierz(1 until 3))
        assertEquals(listOf(10, 8, 6, 4, 2, 0), zbierz(10 downTo 0 step 2))
    }

    // while sprawdza warunek przed kazdym obiegiem
    @Test
    fun `petla while`() {
        var liczba = 3
        val zebrane = mutableListOf<Int>()

        while (liczba > 0) {
            zebrane.add(liczba)
            liczba--
        }

        assertEquals(listOf(3, 2, 1), zebrane)
    }

    // do-while wykonuje cialo co najmniej raz - warunek sprawdzany jest na koncu
    @Test
    fun `petla do while wykonuje sie przynajmniej raz`() {
        var liczba = 0
        val zebrane = mutableListOf<Int>()

        do {
            zebrane.add(liczba)
            liczba--
        } while (liczba > 0)

        assertEquals(listOf(0), zebrane) // warunek od poczatku falszywy, a obieg i tak byl
    }

    /*
        Sterowanie przebiegiem petli:
            break    - przerywa najblizsza petle
            continue - przechodzi do nastepnego obiegu najblizszej petli
    */
    @Test
    fun `break i continue`() {
        val zebrane = mutableListOf<Int>()

        for (liczba in 1..10) {
            if (liczba % 2 == 0) continue // pomijamy liczby parzyste
            if (liczba > 7) break         // konczymy petle
            zebrane.add(liczba)
        }

        assertEquals(listOf(1, 3, 5, 7), zebrane)
    }

    // Etykieta pozwala przerwac petle zewnetrzna z wnetrza petli zagniezdzonej
    @Test
    fun `etykieta pozwala przerwac petle zewnetrzna`() {
        val zebrane = mutableListOf<String>()

        petlaZewnetrzna@ for (wiersz in 1..3) {
            for (kolumna in 1..3) {
                if (kolumna == 2 && wiersz == 2) {
                    break@petlaZewnetrzna // konczy obie petle
                }
                zebrane.add("$wiersz-$kolumna")
            }
        }

        assertEquals(listOf("1-1", "1-2", "1-3", "2-1"), zebrane)
    }
}

// --- elementy pomocnicze ---

private fun opisz(wartosc: Any?) = when (wartosc) {
    1 -> "Jeden"
    2 -> "Dwa"
    3, 4, 5 -> "Trzy, cztery lub piec"
    in 6..10 -> "Pomiedzy 6 a 10"
    is String -> "To tekst"
    null -> "To null"
    else -> "Cos innego"
}

private fun ocen(prawdopodobienstwo: Int) = when {
    prawdopodobienstwo < 40 -> "Nie ma szans"
    prawdopodobienstwo <= 80 -> "Prawdopodobnie"
    prawdopodobienstwo < 100 -> "Tak"
    else -> "Kto wie"
}

private fun zbierz(zakres: Iterable<Int>): List<Int> {
    val wynik = mutableListOf<Int>()
    for (liczba in zakres) {
        wynik.add(liczba)
    }
    return wynik
}
