package cl.itau.challenge.presentation.model

import java.time.LocalDate

sealed interface ListItem {

    data class Item(val earthquakeModel: EarthquakeModel): ListItem
    data class Header(val startDate: LocalDate, val endDate: LocalDate): ListItem
}