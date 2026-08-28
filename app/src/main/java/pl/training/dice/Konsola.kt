package pl.training.dice

import java.io.BufferedReader

/*
    Program glowny sprawdzajacy dzialanie klasy Kosc.
    Tworzy dwa obiekty - kazdy za pomoca innego konstruktora - i wyswietla ich stan.
*/
fun main() {
    uruchomKonsole(System.`in`.bufferedReader(), System.out)
}

// Logika programu glownego wydzielona tak, aby dalo sie ja przetestowac bez klawiatury
fun uruchomKonsole(wejscie: BufferedReader, wyjscie: Appendable) {
    wyjscie.appendLine("=== Gra w kosci ===")

    wyjscie.appendLine()
    wyjscie.appendLine("Kosc nr 1 (konstruktor bezargumentowy - liczba oczek jest losowana):")
    wyswietlKosc(Kosc(), wyjscie)

    wyjscie.appendLine()
    wyjscie.appendLine("Kosc nr 2 (konstruktor jednoargumentowy):")
    wyjscie.appendLine("Podaj liczbe oczek z zakresu ${Kosc.MIN_LICZBA_OCZEK} - ${Kosc.MAX_LICZBA_OCZEK}: ")
    val podanaWartosc = wczytajLiczbe(wejscie)
    wyswietlKosc(Kosc(podanaWartosc), wyjscie)
}

private fun wczytajLiczbe(wejscie: BufferedReader) = wejscie.readLine()?.trim()?.toIntOrNull() ?: Kosc.BRAK_OCZEK

private fun wyswietlKosc(kosc: Kosc, wyjscie: Appendable) {
    wyjscie.appendLine("Liczba utworzonych instancji klasy Kosc: ${Kosc.liczbaInstancji}")
    wyjscie.appendLine("Liczba wyrzuconych oczek: ${kosc.liczbaOczek} (slownie: ${kosc.opisSlowny()})")
    wyjscie.appendLine("Nazwa pliku z obrazem kosci: ${kosc.nazwaPliku}")
}
