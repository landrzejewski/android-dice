package pl.training.dice

import kotlin.random.Random

/*
    Klasa realizujaca logike pojedynczej kosci do gry.
    Kazda sciana kosci zawiera unikalna liczbe oczek z zakresu 1 - 6.
*/
class Kosc(wartosc: Int) {

    // Liczba oczek wyrzucona kescia (0 oznacza brak poprawnej wartosci)
    var liczbaOczek: Int = if (wartosc in MIN_LICZBA_OCZEK..MAX_LICZBA_OCZEK) wartosc else BRAK_OCZEK
        private set

    // Indeks tablicy NAZWY_PLIKOW wskazujacy plik graficzny odpowiadajacy liczbie oczek
    var identyfikatorPliku: Int = liczbaOczek
        private set

    // Informacja czy kosc jest dostepna (czy mozna nia rzucac)
    var dostepna: Boolean = true
        private set

    init {
        liczbaInstancji++
    }

    // Konstruktor bezargumentowy losuje liczbe oczek z zakresu 1 - 6
    constructor() : this(losujLiczbeOczek())

    // Nazwa pliku graficznego odpowiadajacego aktualnej liczbie oczek
    val nazwaPliku: String
        get() = NAZWY_PLIKOW[identyfikatorPliku]

    // Rzut kescia wykonywany tylko wtedy, gdy kosc jest dostepna
    fun rzut() {
        if (!dostepna) {
            return
        }
        liczbaOczek = losujLiczbeOczek()
        identyfikatorPliku = liczbaOczek
    }

    // Blokuje kosc - od tej chwili rzut nie zmienia jej wartosci
    fun zablokuj() {
        dostepna = false
    }

    // Ponownie udostepnia kosc
    fun udostepnij() {
        dostepna = true
    }

    // Wartosc wyrzucona na kosci w postaci tekstu, np. 3 -> "trzy"
    fun opisSlowny(): String = NAZWY_SLOWNE[liczbaOczek]

    companion object {

        const val MIN_LICZBA_OCZEK = 1
        const val MAX_LICZBA_OCZEK = 6
        const val BRAK_OCZEK = 0

        // Nazwy plikow z obrazami kosci, indeks odpowiada liczbie oczek
        val NAZWY_PLIKOW = arrayOf(
            "kosc0.png", "kosc1.png", "kosc2.png", "kosc3.png", "kosc4.png", "kosc5.png", "kosc6.png"
        )

        private val NAZWY_SLOWNE = arrayOf(
            "zero", "jeden", "dwa", "trzy", "cztery", "piec", "szesc"
        )

        // Pole statyczne przechowujace liczbe utworzonych instancji klasy
        var liczbaInstancji = 0
            private set

        private fun losujLiczbeOczek() = Random.nextInt(MIN_LICZBA_OCZEK, MAX_LICZBA_OCZEK + 1)

    }

}
