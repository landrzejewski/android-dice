package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

/*
    Rozdzial 02 - zmienne, typy, operatory, nullowalnosc

    val - zmienna tylko do odczytu, var - zmienna modyfikowalna.
    Kotlin jest jezykiem statycznie typowanym: typ podajemy sami albo wnioskuje go kompilator.
    Wszystkie wartosci sa obiektami - nie ma typow prymitywnych. Konwersje sa jawne (toInt(), toDouble()...).
*/
class T02TypyINullowalnosc {

    @Test
    fun `val jest niemodyfikowalne a var mozna zmieniac`() {
        val liczba = 1
        // liczba = 5 // blad kompilacji - val nie da sie przypisac ponownie

        var innaLiczba = 20
        innaLiczba = 30
        assertEquals(1, liczba)
        assertEquals(30, innaLiczba)
    }

    // Domyslny typ liczby calkowitej to Int, zmiennoprzecinkowej Double
    @Test
    fun `kompilator wnioskuje typ na podstawie literalu`() {
        val malaWartosc = 1                  // Int
        val duzaWartosc = 5_000_000_000      // Long (nie miesci sie w Int)
        val wynik = 4.5                      // Double
        val kwota = 1.4F                     // Float

        assertEquals("Int", malaWartosc::class.simpleName)
        assertEquals("Long", duzaWartosc::class.simpleName)
        assertEquals("Double", wynik::class.simpleName)
        assertEquals("Float", kwota::class.simpleName)

        // przypisanie do zmiennej o jawnym typie udaje sie tylko wtedy,
        // gdy kompilator wywnioskowal dokladnie ten sam typ
        val jawnyInt: Int = malaWartosc
        assertEquals(1, jawnyInt)
    }

    // Podkreslnik poprawia czytelnosc, sufiksy L/F/u wymuszaja typ
    @Test
    fun `literaly - separatory, sufiksy, zapis szesnastkowy i binarny`() {
        val numerKarty = 1234_5678_9012_3456L // Long
        val bajty = 0xFF                      // zapis szesnastkowy
        val maska = 0b0000_1111               // zapis binarny
        val bezZnaku: UInt = 42u              // typ bez znaku

        assertEquals(1234567890123456L, numerKarty)
        assertEquals(255, bajty)
        assertEquals(15, maska)
        assertEquals(42u, bezZnaku)
    }

    // W Kotlinie nie ma automatycznej konwersji typow liczbowych - robimy ja jawnie
    @Test
    fun `konwersje typow sa jawne`() {
        val liczba = 100
        val bajt: Byte = liczba.toByte()
        val podwojna: Double = liczba.toDouble()

        assertEquals(100.toByte(), bajt)
        assertEquals(100.0, podwojna, 0.0)
        assertEquals('A', 65.toChar())
    }

    /*
        Dzielenie dwoch liczb calkowitych zawsze daje liczbe calkowita (czesc ulamkowa jest odrzucana).
        Typ wyniku jest promowany do najszerszego z uzytych typow.
    */
    @Test
    fun `dzielenie calkowite obcina czesc ulamkowa`() {
        assertEquals(1L, 5L / 3)                    // Long, nie 1.66
        assertEquals(2.5f, 5 / 2.toFloat(), 0.001f) // Float
        assertEquals(2.5, 5.0 / 2.toFloat(), 0.001) // Double - najszerszy typ wygrywa
    }

    // a += b to skrot od a = a + b; ++ i -- zmieniaja wartosc o 1
    @Test
    fun `operatory przypisania oraz inkrementacja`() {
        var licznik = 10
        licznik += 5
        licznik -= 3
        licznik *= 2
        licznik %= 7
        assertEquals(3, licznik)

        var i = 0
        assertEquals(0, i++) // post-inkrementacja: najpierw zwraca, potem zwieksza
        assertEquals(2, ++i) // pre-inkrementacja: najpierw zwieksza, potem zwraca
    }

    /*
        Operacje na bitach wykonujemy metodami:
            and - zostawia bity ustawione w obu liczbach
            or  - zostawia bity ustawione w co najmniej jednej liczbie
            xor - zostawia bity ustawione dokladnie w jednej z liczb
            shl / shr / ushr - przesuniecia w lewo / w prawo / w prawo bez znaku
    */
    @Test
    fun `operacje na bitach`() {
        assertEquals(0b0001, 0b0101 and 0b0001)
        assertEquals(0b0101, 0b0101 or 0b0001)
        assertEquals(0b0100, 0b0101 xor 0b0001)
        assertEquals(8, 1 shl 3)   // 0b0001 -> 0b1000
        assertEquals(2, 8 shr 2)   // 0b1000 -> 0b0010
        assertEquals(1, -2 ushr 31) // wypelnia zerami, wiec bit znaku znika
    }

    /*
        Boolean przechowuje true albo false. Wyrazenia logiczne (&&, ||, !) sa wyliczane leniwie:
        drugi argument nie jest sprawdzany, jesli wynik jest juz znany.
        Kotlin nie konwertuje niczego automatycznie na Boolean.
    */
    @Test
    fun `wyrazenia logiczne sa wyliczane leniwie`() {
        val slad = mutableListOf<String>()

        fun sprawdz(nazwa: String, wynik: Boolean): Boolean {
            slad.add(nazwa)
            return wynik
        }

        val czasNiePrzekroczony = false
        // prawa strona nie zostanie wykonana, bo lewa juz przesadza o wyniku
        val sukces = czasNiePrzekroczony && sprawdz("wynikGotowy", true)

        assertFalse(sukces)
        assertEquals(emptyList<String>(), slad)
    }

    // Char to pojedynczy znak reprezentowany jako numer Unicode
    @Test
    fun `typ Char i jego kod Unicode`() {
        val litera: Char = 'a'
        val nowaLinia = '\n'

        assertEquals(65, 'A'.code)
        assertEquals('a', 97.toChar())
        assertEquals(97, litera.code)
        assertTrue(nowaLinia.isWhitespace())
    }

    /*
        String to sekwencja znakow. Kazda modyfikacja tworzy nowy obiekt (typ niemodyfikowalny).
        W literale mozna osadzac wyrazenia: $zmienna oraz ${wyrazenie}.
    */
    @Test
    fun `interpolacja lancuchow wstawia wartosci i wyrazenia`() {
        val imie = "Jan"
        val powitanie = "Hello $imie"

        assertEquals("Hello Jan", powitanie)
        assertEquals("Hello Jan ma dlugosc 9", "$powitanie ma dlugosc ${powitanie.length}")
        assertEquals('H', powitanie[0]) // dostep do znaku przez indeks
    }

    // Surowy lancuch """ zachowuje formatowanie; trimMargin usuwa wciecia do znaku |
    @Test
    fun `surowy lancuch znakow i trimMargin`() {
        val tekst = """
            |Programowanie w Kotlinie
            |jest przyjemne
            |a programista zarabia ${'$'}
            """.trimMargin()

        assertEquals("Programowanie w Kotlinie\njest przyjemne\na programista zarabia $", tekst)
    }

    // Modyfikacja lancucha tworzy nowy obiekt - oryginal pozostaje bez zmian
    @Test
    fun `lancuchy sa niemodyfikowalne`() {
        val oryginal = "kotlin"
        val wielkimi = oryginal.uppercase()

        assertEquals("kotlin", oryginal)
        assertEquals("KOTLIN", wielkimi)
        assertNotSame(oryginal, wielkimi)
    }

    /*
        Tablice reprezentuje typ Array. Maja stala dlugosc, a do elementow siegamy operatorem []
        z indeksem od 0 do n-1. Istnieja tez warianty dla typow prostych, np. IntArray.
    */
    @Test
    fun `tablice - tworzenie, rozmiar, dostep przez indeks`() {
        val liczby = arrayOf(1, 2, 3, 4, 5)

        assertEquals(5, liczby.size)
        assertEquals(2, liczby[1])

        liczby[1] = 20 // rozmiar jest staly, ale elementy mozna podmieniac
        assertEquals(20, liczby[1])
    }

    // Konstruktor Array przyjmuje rozmiar i funkcje wyliczajaca element dla kazdego indeksu
    @Test
    fun `tablica tworzona z funkcji inicjalizujacej`() {
        val liczby = Array(5) { indeks -> indeks + 1 }
        assertEquals(listOf(1, 2, 3, 4, 5), liczby.toList())

        val kwadraty = IntArray(4) { it * it } // `it` to domyslna nazwa jedynego argumentu
        assertEquals(listOf(0, 1, 4, 9), kwadraty.toList())
    }

    /*
        Operator `is` sprawdza przynaleznosc do typu. Po udanym sprawdzeniu kompilator sam
        rzutuje zmienna (smart cast) - jawne rzutowanie nie jest potrzebne.
    */
    @Test
    fun `operator is i inteligentne rzutowanie`() {
        val odpowiedz: Any = "Jakis tekst"

        if (odpowiedz is String) {
            assertEquals(11, odpowiedz.length) // odpowiedz jest juz widziana jako String
        }

        val opis = when (odpowiedz) {
            is String -> "tekst o dlugosci ${odpowiedz.length}"
            is Int -> "liczba $odpowiedz"
            else -> "cos innego"
        }
        assertEquals("tekst o dlugosci 11", opis)
    }

    // `as` rzuca wyjatkiem przy niepowodzeniu, `as?` zwraca null
    @Test
    fun `rzutowanie jawne operatorem as oraz as z pytajnikiem`() {
        val wartosc: Any = "tekst"

        assertEquals("tekst", wartosc as String)
        assertNull(wartosc as? Int)                       // bezpieczne rzutowanie
        assertThrows(ClassCastException::class.java) {
            @Suppress("UNUSED_EXPRESSION")
            wartosc as Int                                 // rzutowanie niemozliwe
        }
    }

    /*
        == porownuje wartosci (wywoluje equals), === porownuje referencje (czy to ten sam obiekt).
        Typy implementujace Comparable mozna tez porownywac operatorami <, >, <=, >=.
    */
    @Test
    fun `rownosc wartosci kontra rownosc referencji`() {
        val pierwszy = StringBuilder("abc").toString()
        val drugi = StringBuilder("abc").toString()

        assertTrue(pierwszy == drugi)   // ta sama wartosc
        assertFalse(pierwszy === drugi) // ale dwa rozne obiekty

        val ten = pierwszy
        assertSame(pierwszy, ten)
        assertTrue("abc" < "abd")       // porownanie przez Comparable
    }

    /*
        Nullowalnosc
        Kazda zmienna musi miec jawna wartosc - nie ma niejawnego null.
        Typ nullowalny deklarujemy przez dopisanie znaku zapytania do nazwy typu.
    */
    @Test
    fun `typ nullowalny i bezpieczne wywolanie`() {
        // val kontakt: Kontakt = null // blad kompilacji - typ nienullowalny
        val kontakt: Kontakt? = null

        // val dlugosc = kontakt.nazwa.length // blad kompilacji - trzeba sprawdzic null
        val dlugoscNazwy: Int? = kontakt?.nazwa?.length // bezpieczne wywolanie, typ Int?
        assertNull(dlugoscNazwy)

        val istniejacy: Kontakt? = Kontakt("Jan")
        assertEquals(3, istniejacy?.nazwa?.length)
    }

    // Operator Elvisa ?: podstawia wartosc domyslna, gdy lewa strona jest null
    @Test
    fun `operator Elvisa podstawia wartosc domyslna`() {
        val kontakt: Kontakt? = null
        val dlugosc: Int = kontakt?.nazwa?.length ?: 0 // typ juz nienullowalny

        assertEquals(0, dlugosc)
        assertEquals("brak", kontakt?.nazwa ?: "brak")
    }

    // !! wymusza wartosc nienullowalna - jesli jest null, leci wyjatek
    @Test
    fun `asercja not null rzuca wyjatek dla wartosci null`() {
        val kontakt: Kontakt? = null
        assertThrows(NullPointerException::class.java) { kontakt!!.nazwa }

        val istniejacy: Kontakt? = Kontakt("Jan")
        assertEquals("Jan", istniejacy!!.nazwa) // tu bezpiecznie, wartosc istnieje
    }

    // Po sprawdzeniu != null kompilator sam traktuje zmienna jako nienullowalna
    @Test
    fun `inteligentne rzutowanie po sprawdzeniu null`() {
        val kontakt: Kontakt? = Kontakt("Anna")

        val dlugosc = if (kontakt != null) kontakt.nazwa.length else -1
        assertEquals(4, dlugosc)
    }

    /*
        Czasem chcemy typ nienullowalny, ale wartosci nie znamy w momencie tworzenia obiektu
        (typowe w Androidzie dla widokow). Sluzy do tego lateinit.
    */
    @Test
    fun `lateinit odklada inicjalizacje wlasciwosci`() {
        val ekran = Ekran()
        assertFalse(ekran.czyGotowy())

        assertThrows(UninitializedPropertyAccessException::class.java) { ekran.model }

        ekran.model = "ModelEkranu"
        assertTrue(ekran.czyGotowy())
        assertEquals("ModelEkranu", ekran.model)
    }

    /*
        Kotlin pozwala przeciazac operatory - wystarczy funkcja o zarezerwowanej nazwie
        oznaczona slowem `operator`.
    */
    @Test
    fun `przeciazenie operatora plus`() {
        val suma = Pieniadze(1.5, "EUR") + Pieniadze(2.5, "EUR")

        assertEquals(4.0, suma.wartosc, 0.0)
        assertEquals("EUR", suma.waluta)
    }
}

// --- elementy pomocnicze ---

class Kontakt(val nazwa: String)

class Ekran {

    lateinit var model: String // typ nienullowalny bez wartosci poczatkowej

    fun czyGotowy() = this::model.isInitialized
}

class Pieniadze(val wartosc: Double, val waluta: String) {

    operator fun plus(inne: Pieniadze) = Pieniadze(wartosc + inne.wartosc, waluta)
}
