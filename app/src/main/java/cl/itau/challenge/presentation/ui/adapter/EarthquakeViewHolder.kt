package cl.itau.challenge.presentation.ui.adapter

import android.view.View
import android.widget.TextView
import cl.itau.challenge.R
import cl.itau.challenge.presentation.model.EarthquakeModel

class EarthquakeViewHolder(
    itemView: View
): ListItemViewHolder(itemView) {

    val earthquakeTitle: TextView = itemView.findViewById(R.id.earthquake_item_title)
    val earthquakePlace: TextView = itemView.findViewById(R.id.earthquake_item_place)
    val earthquakeMagnitude: TextView = itemView.findViewById(R.id.earthquake_item_magnitude)
    val earthquakeDepth: TextView = itemView.findViewById(R.id.earthquake_item_depth)
    val earthquakeDetails: TextView = itemView.findViewById(R.id.earthquake_item_details)

    fun bind(
        item: EarthquakeModel,
        onItemClickListener: (EarthquakeModel) -> Unit = {}
    ) {
        earthquakeTitle.text = item.properties.title
        earthquakePlace.text = item.properties.place
        earthquakeMagnitude.text = itemView.context.resources.getString(R.string.earthquake_item_magnitude, "${item.properties.mag}")
        earthquakeDepth.text = itemView.context.resources.getString(R.string.earthquake_item_depth, "${item.geometryModel.coordinates.last()}")
        earthquakeDetails.setOnClickListener {
            onItemClickListener(item)
        }
    }
}