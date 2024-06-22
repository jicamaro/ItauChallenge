package cl.itau.challenge.presentation.utilities

import android.view.View
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val FORMAT = "dd-MM-yyyy"
const val YEAR_FIRST_FORMAT = "yyyy-MM-dd"

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun LocalDate.toFormattedString(format: String = FORMAT): String = format(DateTimeFormatter.ofPattern(format))