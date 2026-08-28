package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/*
    Rozdzial 06 - programowanie obiektowe, czesc 2: typy specjalne

    Kotlin ma kilka rodzajow klas przygotowanych pod konkretne zastosowania:
    data (dane), object (singleton), enum (staly zbior wartosci), sealed (zamknieta hierarchia)
    oraz value (opakowanie jednej wartosci bez kosztu w czasie wykonania).
*/
class T06ObiektowoscTypySpecjalne {

    /*
        Klasa `data` sluzy do przechowywania danych. Kompilator generuje dla niej:
        equals()/hashCode(), toString(), componentN() (dekompozycja) oraz copy().
        Wymagania: konstruktor glowny z co najmniej jednym parametrem, kazdy jako val/var.
    */
    @Test
    fun `data class generuje toString oraz equals`() {
        val pierwsze = Zamowienie("1", "ksiazka")
        val drugie = Zamowienie("1", "ksiazka")

        assertEquals("Zamowienie(identyfikator=1, produkt=ksiazka)", pierwsze.toString())
        assertEquals(pierwsze, drugie) // porownanie po wartosciach, nie po referencji
        assertEquals(pierwsze.hashCode(), drugie.hashCode())
        assertNotEquals(pierwsze, Zamowienie("2", "ksiazka"))
    }

    // copy() tworzy nowy obiekt, podmieniajac tylko wskazane wlasciwosci
    @Test
    fun `data class udostepnia metode copy`() {
        val oryginal = Zamowienie("1", "ksiazka")
        val kopia = oryginal.copy(identyfikator = "3")

        assertEquals("3", kopia.identyfikator)
        assertEquals("ksiazka", kopia.produkt) // reszta bez zmian
        assertEquals("1", oryginal.identyfikator) // oryginal nietkniety
    }

    // Dekompozycja rozklada obiekt na zmienne wedlug kolejnosci wlasciwosci
    @Test
    fun `dekompozycja obiektu na zmienne`() {
        val (identyfikator, produkt) = Zamowienie("7", "monitor")

        assertEquals("7", identyfikator)
        assertEquals("monitor", produkt)
        assertEquals("7", Zamowienie("7", "monitor").component1()) // to samo wywolane wprost
    }

    // Wlasciwosci zadeklarowane w ciele klasy nie wchodza do equals, toString ani copy
    @Test
    fun `wlasciwosci z ciala klasy sa pomijane w wygenerowanym kodzie`() {
        val pierwsze = Zamowienie("1", "ksiazka")
        val drugie = Zamowienie("1", "ksiazka")
        drugie.uwagi = "pilne"

        assertEquals(pierwsze, drugie) // rozne uwagi, a obiekty nadal rowne
        assertFalse(drugie.toString().contains("pilne"))
    }

    // `object` tworzy singleton - zawsze istnieje dokladnie jedna instancja
    @Test
    fun `object jest singletonem`() {
        Poczatek.x = 10
        Poczatek.y = 20

        assertSame(Poczatek, Poczatek)
        assertEquals(10, Poczatek.x) // stan wspoldzielony w calej aplikacji
        assertEquals("(10, 20)", Poczatek.opis())
    }

    /*
        Enum reprezentuje staly zbior wartosci. Kazda stala ma nazwe (name) i pozycje (ordinal),
        a enum moze miec wlasciwosci, implementowac interfejsy i zawierac metody.
    */
    @Test
    fun `enum - nazwa, pozycja, lista wartosci i wyszukiwanie`() {
        assertEquals("ZIEMIA", Planeta.ZIEMIA.name)
        assertEquals(0, Planeta.ZIEMIA.ordinal)
        assertEquals(1, Planeta.MARS.ordinal)
        assertEquals(listOf(Planeta.ZIEMIA, Planeta.MARS), Planeta.entries)
        assertEquals(Planeta.MARS, Planeta.valueOf("MARS"))
    }

    @Test
    fun `enum z wlasciwosciami, metoda open i interfejsem`() {
        assertEquals(5.9, Planeta.ZIEMIA.masa, 0.0)
        assertEquals("""{"nazwa":"ZIEMIA"}""", Planeta.ZIEMIA.doJson()) // implementacja interfejsu

        // MARS przeslania metode grawitacji wlasna wersja
        assertTrue(Planeta.MARS.grawitacja() < Planeta.ZIEMIA.grawitacja())
    }

    /*
        Sealed - zamknieta hierarchia typow. Kompilator zna wszystkie podtypy, wiec `when`
        nie potrzebuje galezi `else` (i przypomni, gdy dopiszemy nowy podtyp).
    */
    @Test
    fun `sealed interface i wyczerpujacy when`() {
        val sukces: Wynik = Sukces("dane")
        val blad: Wynik = Blad(IllegalStateException("awaria"))

        assertEquals("Sukces z: dane", obsluz(sukces))
        assertEquals("Blad: awaria", obsluz(blad))
    }

    /*
        Funkcja rozszerzajaca dokleja nowa funkcje do istniejacego typu - takze takiego,
        ktorego kodu nie kontrolujemy. To tylko lukier skladniowy: nic nie jest dodawane do klasy.
    */
    @Test
    fun `funkcja rozszerzajaca dla typu String`() {
        assertEquals("tekst", """ "tekst" """.usunCudzyslowy())
    }

    // Wlasciwosc rozszerzajaca jest mozliwa, o ile opiera sie wylacznie na akcesorach
    @Test
    fun `wlasciwosc rozszerzajaca`() {
        assertEquals(2, listOf("a", "b", "c").ostatniIndeks)
    }

    // Rozszerzenie mozna dodac takze do companion object
    @Test
    fun `rozszerzenie companion object`() {
        assertEquals("Wersja: 1.0", Raport.wersja())
    }

    /*
        Adnotacja to metadane doklejane do elementu kodu. Aby odczytac ja w czasie dzialania
        programu, musi miec retencje RUNTIME.
    */
    @Test
    fun `wlasna adnotacja opisuje klase`() {
        assertTrue(Fabryka::class.java.isAnnotation)
        assertTrue(SterownikPlatnosci::class.java.isAnnotationPresent(Fabryka::class.java))
        assertFalse(Raport::class.java.isAnnotationPresent(Fabryka::class.java))
    }

    /*
        Klasa `value` opakowuje jedna niemodyfikowalna wartosc. Podczas kompilacji opakowanie
        znika, a uzywana jest wartosc wewnetrzna - zysk to bezpieczenstwo typow bez kosztu.
    */
    @Test
    fun `value class chroni przed pomyleniem argumentow`() {
        val profil = Profil(Login("jan"), Haslo("tajne"))

        assertEquals("jan", profil.login.wartosc)
        // Profil(Haslo("tajne"), Login("jan")) // blad kompilacji - typy nie do pomylenia
        assertEquals(Login("jan"), profil.login) // value class porownuje sie po wartosci
    }
}

// --- elementy pomocnicze ---

data class Zamowienie(val identyfikator: String, val produkt: String) {

    var uwagi: String = "" // wlasciwosc spoza konstruktora - pomijana w equals/toString/copy
}

object Poczatek { // singleton

    var x = 0
    var y = 0

    fun opis() = "($x, $y)"
}

interface Json {

    fun doJson(): String
}

enum class Planeta(val masa: Double, val promien: Double) : Json {

    ZIEMIA(5.9, 6.3),
    MARS(3.2, 11.1) {

        // pojedyncza stala moze przeslonic metode oznaczona jako open
        override fun grawitacja() = super.grawitacja() * 0.91
    };

    open fun grawitacja() = G * masa / (promien * promien)

    override fun doJson() = """{"nazwa":"$name"}"""

    companion object {

        const val G = 6.6
    }
}

sealed interface Wynik

class Sukces(val dane: String) : Wynik

class Blad(val wyjatek: Throwable) : Wynik

// `when` bez `else` - kompilator wie, ze innych podtypow Wynik nie ma
fun obsluz(wynik: Wynik) = when (wynik) {
    is Sukces -> "Sukces z: ${wynik.dane}"
    is Blad -> "Blad: ${wynik.wyjatek.message}"
}

fun String.usunCudzyslowy() = replace("\"", "").trim()

val <T> List<T>.ostatniIndeks: Int
    get() = size - 1

class Raport {

    companion object
}

fun Raport.Companion.wersja() = "Wersja: 1.0"

/*
    Adnotacje sa metadanymi. @Target okresla, co wolno nia oznaczyc,
    a @Retention - jak dlugo informacja o niej jest dostepna.
*/
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Fabryka

@Fabryka
class SterownikPlatnosci

@JvmInline
value class Login(val wartosc: String)

@JvmInline
value class Haslo(val wartosc: String)

class Profil(val login: Login, val haslo: Haslo)
