package cl.itau.challenge.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import cl.itau.challenge.R
import cl.itau.challenge.presentation.model.EarthquakeModel
import cl.itau.challenge.presentation.model.ListItem
import java.time.LocalDate

private const val LIST_ITEM_HEADER = 0
private const val LIST_ITEM_EARTHQUAKE = 1

class EarthquakeAdapter(
    private val onItemClickListener: (EarthquakeModel) -> Unit = {},
    private val onApplyFiltersClickListener: (LocalDate, LocalDate) -> Unit = { _, _ -> }
): ListAdapter<ListItem, ListItemViewHolder>(diffCallback) {

    companion object {

        val diffCallback = object: DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) = when {
                oldItem is ListItem.Item && newItem is ListItem.Item -> {
                    oldItem.earthquakeModel.id.compareTo(newItem.earthquakeModel.id) == 0
                }
                else -> {
                    oldItem.hashCode().compareTo(newItem.hashCode()) == 0
                }
            }

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        LIST_ITEM_HEADER -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_header_item, parent, false)

            HeaderViewHolder(view)
        }
        LIST_ITEM_EARTHQUAKE -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_earthquake_item, parent, false)

            EarthquakeViewHolder(view)
        }
        else -> { throw NoWhenBranchMatchedException() }
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> LIST_ITEM_HEADER
        else -> LIST_ITEM_EARTHQUAKE
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(
                    startDate = (getItem(position) as ListItem.Header).startDate,
                    endDate = (getItem(position) as ListItem.Header).endDate,
                    onApplyFiltersClickListener = onApplyFiltersClickListener
                )
            }
            is EarthquakeViewHolder -> {
                holder.bind(
                    item = (getItem(position) as ListItem.Item).earthquakeModel,
                    onItemClickListener = onItemClickListener
                )
            }
            else -> { throw NoWhenBranchMatchedException() }
        }
    }
}