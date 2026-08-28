package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/*
    Rozdzial 08 - kolekcje

    Kolekcje to typy reprezentujace grupy elementow:
        List - uporzadkowany ciag elementow, dopuszcza powtorzenia
        Set  - zbior elementow unikalnych
        Map  - zbior par klucz-wartosc

    Kazdy z tych typow ma wariant tylko do odczytu (listOf, setOf, mapOf) oraz modyfikowalny
    (mutableListOf, mutableSetOf, mutableMapOf).
*/
class T08Kolekcje {

    // List zachowuje kolejnosc i pozwala siegac po elementy przez indeks
    @Test
    fun `lista - kolejnosc, indeksy, powtorzenia`() {
        val produkty = listOf("chleb", "mleko", "chleb")

        assertEquals(3, produkty.size)
        assertEquals("chleb", produkty[0])
        assertEquals("mleko", produkty[1])
        assertTrue(produkty.contains("mleko"))
    }

    // Kolekcja tylko do odczytu nie ma metod modyfikujacych - zmieniac mozna wersje mutable
    @Test
    fun `lista modyfikowalna kontra tylko do odczytu`() {
        val doOdczytu = listOf(1, 2, 3)
        // doOdczytu.add(4) // blad kompilacji - brak metody add

        val modyfikowalna = mutableListOf(1, 2, 3)
        modyfikowalna.add(4)
        modyfikowalna.removeAt(0)

        assertEquals(listOf(1, 2, 3), doOdczytu)
        assertEquals(listOf(2, 3, 4), modyfikowalna)
    }

    // Set przechowuje tylko wartosci unikalne - dodanie duplikatu nic nie zmienia
    @Test
    fun `zbior odrzuca duplikaty`() {
        val liczby = mutableSetOf(1, 2, 3)

        assertFalse(liczby.add(3)) // juz jest, wiec zbior sie nie zmienia
        assertTrue(liczby.add(6))
        assertEquals(setOf(1, 2, 3, 6), liczby)
    }

    // Map laczy klucze z wartosciami; para tworzona jest operatorem `to`
    @Test
    fun `mapa - dostep do wartosci po kluczu`() {
        val ceny = mapOf("chleb" to 5.0, "mleko" to 3.5)

        assertEquals(5.0, ceny["chleb"]!!, 0.0)
        assertNull(ceny["maslo"])                          // brak klucza to null
        assertEquals(0.0, ceny.getOrElse("maslo") { 0.0 }, 0.0) // wartosc domyslna
        assertEquals(setOf("chleb", "mleko"), ceny.keys)
    }

    @Test
    fun `mapa modyfikowalna i iteracja po parach`() {
        val magazyn = mutableMapOf("chleb" to 2)
        magazyn["mleko"] = 5
        magazyn["chleb"] = 3 // nadpisanie istniejacego klucza

        val opisy = mutableListOf<String>()
        for ((produkt, sztuki) in magazyn) {
            opisy.add("$produkt=$sztuki")
        }

        assertEquals(listOf("chleb=3", "mleko=5"), opisy)
    }

    // filter zostawia elementy spelniajace warunek, map przeksztalca kazdy element
    @Test
    fun `filter i map tworza nowe kolekcje`() {
        val liczby = listOf(1, 2, 3, 4, 5)

        val parzyste = liczby.filter { it % 2 == 0 }
        val kwadraty = liczby.map { it * it }

        assertEquals(listOf(2, 4), parzyste)
        assertEquals(listOf(1, 4, 9, 16, 25), kwadraty)
        assertEquals(listOf(1, 2, 3, 4, 5), liczby) // oryginal pozostaje bez zmian
    }

    // forEach wykonuje operacje dla kazdego elementu (nie zwraca nowej kolekcji)
    @Test
    fun `forEach przechodzi po wszystkich elementach`() {
        val zebrane = mutableListOf<String>()

        listOf("a", "b").forEach { zebrane.add(it.uppercase()) }

        assertEquals(listOf("A", "B"), zebrane)
    }

    // sortedBy sortuje wedlug wybranej cechy elementu
    @Test
    fun `sortowanie kolekcji`() {
        val produkty = listOf(Produkt("monitor", 900.0), Produkt("mysz", 80.0))

        val wedlugCeny = produkty.sortedBy { it.cena }
        val wedlugNazwyMalejaco = produkty.sortedByDescending { it.nazwa }

        assertEquals(listOf("mysz", "monitor"), wedlugCeny.map { it.nazwa })
        assertEquals(listOf("mysz", "monitor"), wedlugNazwyMalejaco.map { it.nazwa })
    }

    // groupBy buduje mape: klucz wyliczony z elementu -> lista pasujacych elementow
    @Test
    fun `grupowanie elementow`() {
        val slowa = listOf("kot", "pies", "kura", "mysz")

        val wedlugPierwszejLitery = slowa.groupBy { it.first() }

        assertEquals(listOf("kot", "kura"), wedlugPierwszejLitery['k'])
        assertEquals(setOf('k', 'p', 'm'), wedlugPierwszejLitery.keys)
    }

    // Wyszukiwanie i podsumowania - wersje z OrNull nie rzucaja wyjatku dla pustego wyniku
    @Test
    fun `wyszukiwanie i agregacja`() {
        val liczby = listOf(3, 8, 1, 8)

        assertEquals(8, liczby.first { it > 5 })
        assertNull(liczby.firstOrNull { it > 100 })
        assertEquals(20, liczby.sum())
        assertEquals(8, liczby.maxOrNull())
        assertEquals(listOf(3, 8, 1), liczby.distinct())
    }

    // Operacje mozna laczyc w lancuch - kazda dostaje wynik poprzedniej
    @Test
    fun `laczenie operacji w lancuch`() {
        val produkty = listOf(
            Produkt("monitor", 900.0),
            Produkt("mysz", 80.0),
            Produkt("klawiatura", 150.0)
        )

        val tanieNazwy = produkty
            .filter { it.cena < 200 }
            .sortedBy { it.cena }
            .map { it.nazwa }

        assertEquals(listOf("mysz", "klawiatura"), tanieNazwy)
    }
}

// --- elementy pomocnicze ---

data class Produkt(val nazwa: String, val cena: Double)
