package cl.itau.challenge.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cl.itau.challenge.R

class FilterView(
    context: Context,
    attributeSet: AttributeSet
): ConstraintLayout(context, attributeSet) {

    private val label: TextView
    private val date: TextView

    init {
        inflate(context, R.layout.view_filter, this)

        label = findViewById(R.id.filter_view_label)
        date = findViewById(R.id.filter_view_date)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.FilterView)
        label.text = attributes.getString(R.styleable.FilterView_label)
        attributes.recycle()
    }

    fun setDate(date: String) {
        this.date.text = date
        invalidate()
        requestLayout()
    }

    fun getDate() = date.text.toString()
}