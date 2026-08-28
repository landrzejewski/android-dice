package pl.training.kotlin

import org.junit.Assert.assertEquals
import org.junit.Test

/*
    Rozdzial 05 - programowanie obiektowe, czesc 1: klasy

    Klase deklarujemy slowem `class`. Konstruktor glowny jest czescia naglowka klasy
    i moze od razu deklarowac wlasciwosci (val/var przed nazwa parametru).
    Kazda klasa dziedziczy domyslnie po typie Any.
*/
class T05ObiektowoscKlasy {

    // Parametr konstruktora oznaczony val/var staje sie wlasciwoscia obiektu
    @Test
    fun `konstruktor glowny moze deklarowac wlasciwosci`() {
        val konto = Konto("111111")

        konto.saldo = 100.0        // wywolanie settera, mozliwe bo wlasciwosc jest `var`
        assertEquals(100.0, konto.saldo, 0.0) // wywolanie gettera
    }

    /*
        Wlasciwosc moze miec wlasny getter i setter. Wewnatrz nich `field` wskazuje
        na pole zapasowe przechowujace wartosc.
    */
    @Test
    fun `wlasny getter i setter`() {
        val konto = Konto("111111")

        konto.wlasciciel = "Jan"
        assertEquals("JAN", konto.wlasciciel) // getter zwraca wartosc wielkimi literami

        konto.wlasciciel = "   "              // setter odrzuca pusta wartosc
        assertEquals("JAN", konto.wlasciciel) // stara wartosc pozostala
    }

    // Jesli akcesor nie uzywa `field`, pole zapasowe w ogole nie powstaje
    @Test
    fun `wlasciwosc wyliczana bez pola zapasowego`() {
        val konto = Konto("111111")
        konto.wlasciciel = "Jan"
        konto.saldo = 50.0

        assertEquals("111111 JAN 50.0", konto.opisKonta) // wyliczane przy kazdym odczycie
    }

    /*
        Blok `init` to cialo konstruktora glownego. Konstruktor dodatkowy musi wywolac
        konstruktor glowny (slowo `this`), ktory wykonuje sie jako pierwszy.
    */
    @Test
    fun `kolejnosc wykonania init i konstruktora dodatkowego`() {
        sladKonta.clear()

        Konto(200.0, "222222") // konstruktor dodatkowy

        assertEquals(listOf("init 222222", "konstruktor dodatkowy"), sladKonta)
    }

    @Test
    fun `konstruktor dodatkowy ustawia dodatkowy stan`() {
        val konto = Konto(200.0, "222222")
        assertEquals(200.0, konto.saldo, 0.0)
    }

    /*
        `companion object` przechowuje elementy zwiazane z klasa, a nie z instancja
        (odpowiednik elementow statycznych z Javy). `const val` to stala znana w czasie kompilacji.
    */
    @Test
    fun `companion object i stala kompilacji`() {
        assertEquals("PLN", Konto.DOMYSLNA_WALUTA)
        assertEquals("KONTO-1", Konto.utworzIdentyfikator(1))
    }

    /*
        Klasy sa domyslnie zamkniete na dziedziczenie - zeby po klasie dziedziczyc,
        trzeba oznaczyc ja slowem `open`.
    */
    @Test
    fun `dziedziczenie po klasie open`() {
        val konto = KontoPremium("333333")

        konto.saldo = 1000.0
        assertEquals(1000.0, konto.saldo, 0.0) // odziedziczona wlasciwosc
        assertEquals(50.0, konto.bonus(), 0.0) // wlasna metoda podklasy

        // podklase mozna podstawic wszedzie tam, gdzie oczekiwana jest klasa bazowa
        val jakoKonto: Konto = konto
        assertEquals(1000.0, jakoKonto.saldo, 0.0)
    }

    // Klasa abstrakcyjna moze miec metody bez implementacji; `super` siega do klasy bazowej
    @Test
    fun `klasa abstrakcyjna i przeslanianie metody`() {
        val administrator: Uzytkownik = Administrator()

        assertEquals("Uzytkownik: Administrator", administrator.informacja())
        assertEquals("Uzytkownik: ", administrator.informacjaOgolna())
    }

    /*
        Interfejs z jedna metoda abstrakcyjna mozna oznaczyc jako `fun interface` (interfejs SAM).
        Dzieki temu zamiast obiektu mozna przekazac lambde.
    */
    @Test
    fun `interfejs funkcyjny mozna zaimplementowac obiektem lub lambda`() {
        val zebrane = mutableListOf<String>()

        // wyrazenie obiektowe - obiekt anonimowej klasy implementujacej interfejs
        wygenerujRaport(object : Drukowalny {
            override fun drukuj(wartosc: String) {
                zebrane.add("obiekt: $wartosc")
            }
        })

        // ten sam interfejs przekazany jako lambda
        wygenerujRaport { zebrane.add("lambda: $it") }

        assertEquals(listOf("obiekt: podsumowanie", "lambda: podsumowanie"), zebrane)
    }

    // Klasa moze implementowac wiele interfejsow jednoczesnie
    @Test
    fun `klasa implementujaca interfejs`() {
        val dokument = Dokument()
        dokument.drukuj("tresc")

        assertEquals(listOf("tresc"), dokument.wydruki)
    }

    /*
        Klasy zagniezdzone sa domyslnie statyczne - nie widza klasy zewnetrznej i mozna je
        tworzyc bez jej instancji. Slowo `inner` daje dostep do klasy zewnetrznej.
    */
    @Test
    fun `klasa zagniezdzona kontra klasa inner`() {
        val zagniezdzona = Zewnetrzna.Zagniezdzona() // bez instancji klasy zewnetrznej
        assertEquals("dzialam samodzielnie", zagniezdzona.uruchom())

        val wewnetrzna = Zewnetrzna("konfiguracja").Wewnetrzna() // wymaga instancji
        assertEquals("dzialam z dostepem do: konfiguracja", wewnetrzna.uruchom())
    }
}

// --- elementy pomocnicze ---

val sladKonta = mutableListOf<String>()

open class Konto(private val numer: String) { // konstruktor glowny z prywatna wlasciwoscia

    var saldo = 0.0 // zwykla wlasciwosc z domyslnym getterem i setterem

    var wlasciciel: String = ""
        get() = field.uppercase()   // wlasny getter, `field` to pole zapasowe
        set(value) {                // wlasny setter
            if (value.isNotBlank()) {
                field = value
            }
        }

    // Akcesor nie uzywa `field`, wiec pole zapasowe nie powstaje - wartosc jest wyliczana
    val opisKonta: String
        get() = "$numer $wlasciciel $saldo"

    init { // cialo konstruktora glownego
        sladKonta.add("init $numer")
    }

    // Konstruktor dodatkowy musi wywolac konstruktor glowny
    constructor(saldo: Double, numer: String) : this(numer) {
        this.saldo = saldo
        sladKonta.add("konstruktor dodatkowy")
    }

    companion object { // odpowiednik elementow statycznych

        const val DOMYSLNA_WALUTA = "PLN" // stala znana w czasie kompilacji

        fun utworzIdentyfikator(numer: Int) = "KONTO-$numer"
    }
}

class KontoPremium(numer: String) : Konto(numer) {

    fun bonus() = saldo * 0.05
}

abstract class Uzytkownik {

    abstract fun informacja(): String // brak implementacji - musi ja dac podklasa

    fun informacjaOgolna() = "Uzytkownik: "
}

class Administrator : Uzytkownik() {

    override fun informacja() = super.informacjaOgolna() + "Administrator"
}

fun interface Drukowalny { // interfejs funkcyjny (SAM)

    fun drukuj(wartosc: String)
}

fun wygenerujRaport(drukowalny: Drukowalny) = drukowalny.drukuj("podsumowanie")

class Dokument : Drukowalny {

    val wydruki = mutableListOf<String>()

    override fun drukuj(wartosc: String) {
        wydruki.add(wartosc)
    }
}

class Zewnetrzna(private val nazwa: String = "") {

    class Zagniezdzona { // brak dostepu do skladowych klasy zewnetrznej

        fun uruchom() = "dzialam samodzielnie"
    }

    inner class Wewnetrzna { // ma referencje do obiektu klasy zewnetrznej

        fun uruchom() = "dzialam z dostepem do: $nazwa"
    }
}
