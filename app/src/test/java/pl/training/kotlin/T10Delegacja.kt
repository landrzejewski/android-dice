package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/*
    Rozdzial 10 - delegacja

    Delegacja to oddanie odpowiedzialnosci innemu obiektowi. Kotlin wspiera ja skladniowo
    slowem `by` na dwoch poziomach: calej klasy oraz pojedynczej wlasciwosci.
*/
class T10Delegacja {

    /*
        Delegacja na poziomie klasy: `Bazowy by obiekt` sprawia, ze kompilator generuje
        wszystkie metody interfejsu i przekazuje je do wskazanego obiektu.
    */
    @Test
    fun `delegacja calego interfejsu do innego obiektu`() {
        val implementacja = BazowyImpl(10)

        assertEquals("BazowyImpl: 10", Przekazujacy(implementacja).nazwa())
    }

    // Metode przekazana mozna przeslonic - wtedy uzywana jest wlasna implementacja
    @Test
    fun `przeslonieta metoda ma pierwszenstwo przed delegatem`() {
        val implementacja = BazowyImpl(10)
        val przekazujacy = PrzekazujacyZPrzeslonieciem(implementacja)

        assertEquals("wlasna implementacja", przekazujacy.drukuj()) // wersja wlasna
        assertEquals("BazowyImpl: 10", przekazujacy.nazwa())        // ta idzie do delegata
    }

    /*
        Delegat `lazy` liczy wartosc dopiero przy pierwszym odczycie, a potem ja zapamietuje.
        Przydatne, gdy obliczenie jest kosztowne i moze sie w ogole nie przydac.
    */
    @Test
    fun `lazy wylicza wartosc tylko raz i to przy pierwszym odczycie`() {
        val obliczenia = Obliczenia()

        assertEquals(0, obliczenia.licznikWywolan) // nic jeszcze nie policzono

        assertEquals("wynik", obliczenia.wartosc)
        assertEquals("wynik", obliczenia.wartosc)
        assertEquals(1, obliczenia.licznikWywolan) // mimo dwoch odczytow policzone raz
    }

    // Delegat `observable` powiadamia o kazdej zmianie - dostajemy stara i nowa wartosc
    @Test
    fun `observable reaguje na zmiane wartosci`() {
        val licznik = Licznik()

        licznik.wartosc = 5
        licznik.wartosc++

        assertEquals(6, licznik.wartosc)
        assertEquals(listOf("0 => 5", "5 => 6"), licznik.zmiany)
    }

    /*
        Wlasciwosci moga byc przechowywane w mapie zamiast w osobnych polach - klucz mapy
        odpowiada nazwie wlasciwosci. Czesty wzorzec przy danych z JSON-a.
    */
    @Test
    fun `wlasciwosci czytane z mapy`() {
        val uczestnik = Uczestnik(mapOf("imie" to "Jan Kowalski", "wiek" to 25))

        assertEquals("Jan Kowalski", uczestnik.imie)
        assertEquals(25, uczestnik.wiek)
        assertEquals("Jan Kowalski: 25", uczestnik.toString())
    }

    /*
        Wlasny delegat to klasa z operatorami getValue i setValue. Pozwala opakowac
        odczyt i zapis wlasnym zachowaniem, np. logowaniem albo walidacja.
    */
    @Test
    fun `wlasny delegat przechwytuje odczyt i zapis`() {
        val osoba = Kandydat()

        osoba.imie = "Jan"
        assertEquals("Jan", osoba.imie)
        assertEquals(listOf("zapis imie=Jan", "odczyt imie"), dziennikDelegata)

        dziennikDelegata.clear()
    }

    // Delegat moze tez pilnowac reguly - tutaj odczyt przed zapisem konczy sie bledem
    @Test
    fun `wlasny delegat zglasza blad dla wartosci nieustawionej`() {
        val osoba = Kandydat()

        assertThrows(IllegalStateException::class.java) { osoba.imie }

        dziennikDelegata.clear()
    }
}

// --- elementy pomocnicze ---

interface Bazowy {

    fun drukuj(): String

    fun nazwa(): String
}

class BazowyImpl(val x: Int) : Bazowy {

    override fun drukuj() = "BazowyImpl drukuje $x"

    override fun nazwa() = "BazowyImpl: $x"
}

// wszystkie metody interfejsu sa przekazywane do obiektu `bazowy`
class Przekazujacy(bazowy: Bazowy) : Bazowy by bazowy

class PrzekazujacyZPrzeslonieciem(bazowy: Bazowy) : Bazowy by bazowy {

    override fun drukuj() = "wlasna implementacja"
}

class Obliczenia {

    var licznikWywolan = 0
        private set

    val wartosc: String by lazy {
        licznikWywolan++ // wykona sie tylko przy pierwszym odczycie
        "wynik"
    }
}

class Licznik {

    val zmiany = mutableListOf<String>()

    var wartosc: Int by observable(0) { _: KProperty<*>, stara: Int, nowa: Int ->
        zmiany.add("$stara => $nowa")
    }
}

class Uczestnik(mapa: Map<String, Any>) {

    val imie: String by mapa // wartosc pobierana z mapy pod kluczem "imie"
    val wiek: Int by mapa

    override fun toString() = "$imie: $wiek"
}

val dziennikDelegata = mutableListOf<String>()

class DelegatZDziennikiem<T> {

    private var wartosc: T? = null

    operator fun getValue(wlasciciel: Any, wlasciwosc: KProperty<*>): T {
        dziennikDelegata.add("odczyt ${wlasciwosc.name}")
        return wartosc ?: throw IllegalStateException("Wlasciwosc ${wlasciwosc.name} nie ma wartosci")
    }

    operator fun setValue(wlasciciel: Any, wlasciwosc: KProperty<*>, wartosc: T) {
        dziennikDelegata.add("zapis ${wlasciwosc.name}=$wartosc")
        this.wartosc = wartosc
    }
}

class Kandydat {

    var imie: String by DelegatZDziennikiem()
}
