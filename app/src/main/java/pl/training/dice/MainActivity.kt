package pl.training.dice

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/*
    Gra w kosci - rzut pieciema szesciennymi koscmi.
    Klikniecie w obraz kosci blokuje ja (przezroczystosc 50%) lub ponownie udostepnia.
*/
class MainActivity : AppCompatActivity() {

    private val kosci = List(LICZBA_KOSCI) { Kosc(Kosc.BRAK_OCZEK) }
    private lateinit var obrazyKosci: List<ImageView>
    private lateinit var sumaOczek: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sumaOczek = findViewById(R.id.sumaOczek)
        obrazyKosci = IDENTYFIKATORY_OBRAZOW.map { findViewById<ImageView>(it) }
        obrazyKosci.forEachIndexed { indeks, obraz ->
            obraz.setOnClickListener { przelaczDostepnosc(indeks) }
        }
        findViewById<Button>(R.id.przyciskRzutu).setOnClickListener { wykonajRzut() }
        odswiezWidok()
    }

    // Rzut wykonywany jest tylko dla dostepnych kosci, suma liczona jest ze wszystkich
    private fun wykonajRzut() {
        kosci.forEach { it.rzut() }
        odswiezWidok()
    }

    private fun przelaczDostepnosc(indeks: Int) {
        val kosc = kosci[indeks]
        if (kosc.dostepna) kosc.zablokuj() else kosc.udostepnij()
        odswiezWidok()
    }

    private fun odswiezWidok() {
        kosci.forEachIndexed { indeks, kosc ->
            val obraz = obrazyKosci[indeks]
            obraz.setImageResource(OBRAZY_KOSCI[kosc.identyfikatorPliku])
            obraz.alpha = if (kosc.dostepna) PELNA_WIDOCZNOSC else CZESCIOWA_WIDOCZNOSC
        }
        sumaOczek.text = kosci.sumOf { it.liczbaOczek }.toString()
    }

    private companion object {

        const val LICZBA_KOSCI = 5
        const val PELNA_WIDOCZNOSC = 1.0f
        const val CZESCIOWA_WIDOCZNOSC = 0.5f

        // Obrazy kosci - indeks tablicy odpowiada identyfikatorowi pliku (liczbie oczek)
        val OBRAZY_KOSCI = intArrayOf(
            R.drawable.kosc0, R.drawable.kosc1, R.drawable.kosc2, R.drawable.kosc3,
            R.drawable.kosc4, R.drawable.kosc5, R.drawable.kosc6
        )

        val IDENTYFIKATORY_OBRAZOW = intArrayOf(
            R.id.kosc1, R.id.kosc2, R.id.kosc3, R.id.kosc4, R.id.kosc5
        )

    }

}
