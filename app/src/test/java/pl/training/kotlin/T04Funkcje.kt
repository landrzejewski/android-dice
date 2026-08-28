package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Test

/*
    Rozdzial 04 - funkcje

    Funkcje deklarujemy slowem `fun`. Moga byc zdefiniowane:
        - w pliku, poza klasami (funkcje najwyzszego poziomu)
        - w klasie lub obiekcie (metody)
        - wewnatrz innej funkcji (funkcje lokalne) - maja dostep do jej zmiennych

    Parametry funkcji sa niemodyfikowalne, a kazda funkcja ma typ wyniku (domyslnie Unit).
*/
class T04Funkcje {

    // Klasyczna funkcja z blokiem i jawnym typem wyniku
    @Test
    fun `funkcja z blokiem i slowem return`() {
        assertEquals(9.0, kwadrat(3.0), 0.0)
    }

    // Funkcja jednowyrazeniowa - typ wyniku wnioskuje kompilator
    @Test
    fun `funkcja jednowyrazeniowa`() {
        assertEquals(true, czyParzysta(4))
        assertEquals(false, czyParzysta(5))
    }

    // Funkcja bez jawnego wyniku zwraca Unit
    @Test
    fun `funkcja bez wyniku zwraca Unit`() {
        val wynik: Unit = zapiszLog("cokolwiek")
        assertEquals(Unit, wynik)
    }

    // vararg pozwala przekazac dowolna liczbe argumentow (wewnatrz to tablica)
    @Test
    fun `parametr vararg przyjmuje dowolna liczbe argumentow`() {
        assertEquals(0, suma())
        assertEquals(6, suma(1, 2, 3))

        val liczby = intArrayOf(4, 5)
        assertEquals(9, suma(*liczby)) // operator * rozwija tablice na argumenty
    }

    // Wartosci domyslne pozwalaja pominac argumenty, nazwy - podac je w dowolnej kolejnosci
    @Test
    fun `wartosci domyslne i argumenty nazwane`() {
        assertEquals("Hello, there", przywitanie())
        assertEquals("Hi there", przywitanie("Hi"))
        assertEquals("Hi Jan", przywitanie("Hi", "Jan"))
        assertEquals("Hello, John", przywitanie(kto = "John")) // pomijamy pierwszy argument
    }

    // Przeciazanie - ta sama nazwa, inne typy lub inna liczba parametrow
    @Test
    fun `przeciazanie funkcji`() {
        assertEquals("Int 2", jakoTekst(2))
        assertEquals("Long 2", jakoTekst(2L))
    }

    // Funkcja infix (jeden parametr) moze byc wywolana bez kropki i nawiasow
    @Test
    fun `funkcja infix`() {
        assertEquals(2, 20.mojModulo(3)) // zapis zwyczajny
        assertEquals(2, 20 mojModulo 3)  // zapis infiksowy
    }

    // Funkcja lokalna widzi i moze modyfikowac zmienne funkcji, w ktorej powstala
    @Test
    fun `funkcja lokalna ma dostep do zmiennych otoczenia`() {
        var licznik = 0

        fun zwieksz(o: Int) {
            licznik += o // zmienna z zakresu zewnetrznego
        }

        zwieksz(2)
        zwieksz(3)
        assertEquals(5, licznik)
    }

    /*
        Zwykly `return` wewnatrz lambdy konczy cala funkcje zewnetrzna (tzw. non-local return).
        Ponizej dla 3 przerywamy wszystko - napis koncowy nigdy nie zostanie dodany.
    */
    @Test
    fun `return w lambdzie konczy funkcje zewnetrzna`() {
        assertEquals(listOf("1", "2"), przerwijCalaFunkcje())
    }

    // return@etykieta konczy tylko biezacy obieg lambdy - reszta funkcji sie wykonuje
    @Test
    fun `return z etykieta konczy tylko biezacy obieg lambdy`() {
        assertEquals(listOf("1", "2", "koniec"), przerwijTylkoObieg())
        assertEquals(listOf("1", "2", "koniec"), przerwijTylkoObiegWlasnaEtykieta())
    }
}

// --- elementy pomocnicze ---

private fun kwadrat(x: Double): Double {
    return x * x
}

private fun czyParzysta(wartosc: Int) = wartosc % 2 == 0

private fun zapiszLog(komunikat: String) {
    komunikat.length // funkcja niczego nie zwraca, wiec jej typ wyniku to Unit
}

private fun suma(vararg liczby: Int): Int {
    var suma = 0
    for (liczba in liczby) suma += liczba
    return suma
}

private fun przywitanie(jak: String = "Hello,", kto: String = "there") = "$jak $kto"

private fun jakoTekst(wartosc: Int) = "Int $wartosc"

private fun jakoTekst(wartosc: Long) = "Long $wartosc"

private infix fun Int.mojModulo(wartosc: Int) = this % wartosc

private fun przerwijCalaFunkcje(): List<String> {
    val zebrane = mutableListOf<String>()
    listOf(1, 2, 3).forEach {
        if (it == 3) return zebrane // wychodzi z calej funkcji przerwijCalaFunkcje
        zebrane.add(it.toString())
    }
    zebrane.add("koniec") // ta linia nigdy nie zostanie wykonana
    return zebrane
}

private fun przerwijTylkoObieg(): List<String> {
    val zebrane = mutableListOf<String>()
    listOf(1, 2, 3).forEach {
        if (it == 3) return@forEach // pomija tylko biezacy element
        zebrane.add(it.toString())
    }
    zebrane.add("koniec")
    return zebrane
}

private fun przerwijTylkoObiegWlasnaEtykieta(): List<String> {
    val zebrane = mutableListOf<String>()
    listOf(1, 2, 3).forEach wewnetrzna@{
        if (it == 3) return@wewnetrzna // to samo, ale z wlasna etykieta lambdy
        zebrane.add(it.toString())
    }
    zebrane.add("koniec")
    return zebrane
}
