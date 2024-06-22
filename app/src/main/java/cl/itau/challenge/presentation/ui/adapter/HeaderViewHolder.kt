package cl.itau.challenge.presentation.ui.adapter

import android.app.DatePickerDialog
import android.view.View
import android.widget.TextView
import cl.itau.challenge.R
import cl.itau.challenge.presentation.ui.views.FilterView
import cl.itau.challenge.presentation.utilities.FORMAT
import cl.itau.challenge.presentation.utilities.toFormattedString
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HeaderViewHolder(
    itemView: View
): ListItemViewHolder(itemView) {

    private val startDateView: FilterView = itemView.findViewById(R.id.view_filter_start_date)
    private val endDateView: FilterView = itemView.findViewById(R.id.view_filter_end_date)
    private val applyFilter: TextView = itemView.findViewById(R.id.view_filter_apply)

    private val startDateCalendar by lazy { Calendar.getInstance() }
    private val endDateCalendar by lazy { Calendar.getInstance() }

    private val startDatePickerCallback =
        DatePickerDialog.OnDateSetListener { _, year, month, day ->
            setCalendar(startDateCalendar, year, month, day)
            updateLabel(startDateCalendar, startDateView)
        }

    private val endDatePickerCallback =
        DatePickerDialog.OnDateSetListener { _, year, month, day ->
            setCalendar(endDateCalendar, year, month, day)
            setCalendar(startDateCalendar, year, month, day - 1)

            updateLabel(endDateCalendar, endDateView)
            updateLabel(startDateCalendar, startDateView)
        }

    fun bind(
        startDate: LocalDate,
        endDate: LocalDate,
        onApplyFiltersClickListener: (LocalDate, LocalDate) -> Unit = { _, _ -> },
    ) {
        setUpStartCalendar(startDate)
        setUpEndCalendar(endDate)

        applyFilter.setOnClickListener {
            onApplyFiltersClickListener(
                LocalDate.parse(startDateView.getDate(), DateTimeFormatter.ofPattern(FORMAT)),
                LocalDate.parse(endDateView.getDate(), DateTimeFormatter.ofPattern(FORMAT))
            )
        }
    }

    private fun setUpStartCalendar(startDate: LocalDate) {
        setCalendar(
            startDateCalendar,
            startDate.year,
            startDate.monthValue - 1,
            startDate.dayOfMonth
        )

        startDateView.setDate(startDate.toFormattedString())
        startDateView.setOnClickListener {
            DatePickerDialog(
                itemView.context,
                startDatePickerCallback,
                startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = with (Calendar.getInstance()) {
                    set(
                        endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH) - 1
                    )
                    time.time
                }
                show()
            }
        }
    }

    private fun setUpEndCalendar(endDate: LocalDate) {
        setCalendar(
            endDateCalendar,
            endDate.year,
            endDate.month.value - 1,
            endDate.dayOfMonth
        )

        endDateView.setDate(endDate.toFormattedString())
        endDateView.setOnClickListener {
            DatePickerDialog(
                itemView.context,
                endDatePickerCallback,
                endDateCalendar.get(Calendar.YEAR),
                endDateCalendar.get(Calendar.MONTH),
                endDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = Calendar.getInstance().time.time
                show()
            }
        }
    }

    private fun setCalendar(calendar: Calendar, year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
    }

    private fun updateLabel(calendar: Calendar, view: FilterView) {
        val myFormat = "dd-MM-yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale("es", "CL"))

        view.setDate(dateFormat.format(calendar.time))
    }
}