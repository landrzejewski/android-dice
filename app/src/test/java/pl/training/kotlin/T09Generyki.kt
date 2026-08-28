package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/*
    Rozdzial 09 - typy generyczne

    Typ generyczny jest sparametryzowany innym typem. W Kotlinie generyczne moga byc
    funkcje, klasy oraz interfejsy.
*/
class T09Generyki {

    // Parametr typu funkcji podajemy w nawiasach ostrych przed jej nazwa
    @Test
    fun `funkcja generyczna`() {
        val wybrana = jednaZ("a", "b")
        assertTrue(wybrana == "a" || wybrana == "b")

        assertEquals(5, jednaZ(5, 5)) // ta sama funkcja dziala dla dowolnego typu
    }

    // Klasa generyczna przechowuje wartosc dowolnego typu, zachowujac bezpieczenstwo typow
    @Test
    fun `klasa generyczna`() {
        val liczba = Opakowanie(2)
        val tekst = Opakowanie("Test")

        assertEquals(2, liczba.wartosc)          // typ wnioskowany jako Opakowanie<Int>
        assertEquals(4, tekst.wartosc.length)    // tutaj Opakowanie<String>
    }

    // Interfejs generyczny moze miec kilka parametrow typu - tu typ zrodlowy i docelowy
    @Test
    fun `interfejs generyczny z dwoma parametrami typu`() {
        val naInt: Konwerter<Double, Int> = DoubleNaInt()
        val naDouble: Konwerter<Int, Double> = IntNaDouble()

        assertEquals(2, naInt.konwertuj(2.7))
        assertEquals(2.0, naDouble.konwertuj(2), 0.0)
    }

    /*
        Informacja o typie generycznym jest usuwana w czasie kompilacji (type erasure),
        dlatego rzutowanie na konkretny typ generyczny jest niesprawdzalne - stad `as?`.
    */
    @Test
    fun `bezpieczne rzutowanie elementu z mapy konwerterow`() {
        @Suppress("UNCHECKED_CAST")
        val konwerter = konwertery["doubleNaInt"] as? Konwerter<Double, Int>

        assertEquals(2, konwerter?.konwertuj(2.0))
        assertNull(konwertery["nieistniejacy"])
    }

    // Ograniczenie <T : Pojazd> pozwala uzywac skladowych typu bazowego wewnatrz klasy
    @Test
    fun `ograniczenie parametru typu`() {
        val garaz = Garaz<Samochod>()
        garaz.dodaj(Samochod("Opel"))

        assertEquals("Opel", garaz.ostatni().nazwa) // wiemy, ze T ma wlasciwosc nazwa
    }

    /*
        Brak modyfikatora wariancji oznacza inwariancje: Pudelko<Dziecko> i Pudelko<Rodzic>
        nie sa ze soba w zadnej relacji.

        Modyfikator `out` (kowariancja) - typ tylko zwraca T, wiec Pudelko<Dziecko>
        mozna podstawic tam, gdzie oczekiwane jest Pudelko<Rodzic>.
    */
    @Test
    fun `out oznacza kowariancje`() {
        val pudelkoDziecka = PudelkoProducent(Samochod("Fiat"))
        val pudelkoRodzica: PudelkoProducent<Pojazd> = pudelkoDziecka // OK dzieki `out`

        assertEquals("Fiat", pudelkoRodzica.pobierz().nazwa)
    }

    /*
        Modyfikator `in` (kontrawariancja) - typ tylko przyjmuje T, wiec Pudelko<Rodzic>
        mozna podstawic tam, gdzie oczekiwane jest Pudelko<Dziecko>.
    */
    @Test
    fun `in oznacza kontrawariancje`() {
        val konsumentPojazdow = PudelkoKonsument<Pojazd>()
        val konsumentSamochodow: PudelkoKonsument<Samochod> = konsumentPojazdow // OK dzieki `in`

        konsumentSamochodow.przyjmij(Samochod("Skoda"))
        assertEquals(listOf("Skoda"), konsumentPojazdow.nazwy())
    }

    // Gdy konkretny typ nie ma znaczenia, mozemy uzyc projekcji gwiazdkowej
    @Test
    fun `projekcja gwiazdkowa akceptuje dowolny typ`() {
        val cos: Any = listOf("A", "B")

        assertTrue(cos is List<*>) // wiemy, ze to lista, ale nie znamy typu jej elementow
        // cos is List<Int> // blad kompilacji - typ elementow jest usuwany w czasie kompilacji

        // funkcja z parametrem Collection<*> przyjmuje kolekcje o dowolnym typie elementow
        assertEquals(2, policzElementy(listOf("A", "B")))
        assertEquals(3, policzElementy(setOf(1, 2, 3)))
    }
}

// --- elementy pomocnicze ---

private fun <T> jednaZ(a: T, b: T): T = if (Random.nextBoolean()) a else b

class Opakowanie<V>(val wartosc: V)

interface Konwerter<S, T> {

    fun konwertuj(zrodlo: S): T
}

class DoubleNaInt : Konwerter<Double, Int> {

    override fun konwertuj(zrodlo: Double) = zrodlo.toInt()
}

class IntNaDouble : Konwerter<Int, Double> {

    override fun konwertuj(zrodlo: Int) = zrodlo.toDouble()
}

class StringNaBoolean : Konwerter<String, Boolean> {

    override fun konwertuj(zrodlo: String) = zrodlo.toBoolean()
}

val konwertery = mapOf(
    "doubleNaInt" to DoubleNaInt(),
    "intNaDouble" to IntNaDouble(),
    "stringNaBoolean" to StringNaBoolean()
)

open class Pojazd(val nazwa: String)

class Samochod(nazwa: String) : Pojazd(nazwa)

// T moze byc tylko Pojazdem lub jego podtypem
class Garaz<T : Pojazd> {

    private val pojazdy = mutableListOf<T>()

    fun dodaj(pojazd: T) = pojazdy.add(pojazd)

    fun ostatni(): T = pojazdy.last()
}

// `out` - typ tylko produkuje wartosci T (moze je zwracac, nie moze przyjmowac)
class PudelkoProducent<out T>(private val wartosc: T) {

    fun pobierz(): T = wartosc
}

// `in` - typ tylko konsumuje wartosci T (moze je przyjmowac, nie moze zwracac)
class PudelkoKonsument<in T : Pojazd> {

    private val przyjete = mutableListOf<String>()

    fun przyjmij(pojazd: T) {
        przyjete.add(pojazd.nazwa)
    }

    fun nazwy(): List<String> = przyjete
}

private fun policzElementy(kolekcja: Collection<*>) = kolekcja.size
