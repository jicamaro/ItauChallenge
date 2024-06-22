package cl.itau.challenge.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import cl.itau.challenge.BuildConfig
import cl.itau.challenge.R
import cl.itau.challenge.databinding.FragmentDetailBinding
import cl.itau.challenge.presentation.utilities.invisible
import cl.itau.challenge.presentation.utilities.visible
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.OverlayItem

const val DETAIL_TITLE = "DETAIL_TITLE"
const val DETAIL_PLACE = "DETAIL_PLACE"
const val DETAIL_MAGNITUDE = "DETAIL_MAGNITUDE"
const val DETAIL_COORDINATES = "DETAIL_COORDINATES"

private const val DEFAULT_ZOOM = 5.0

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var title: String = ""
    private var magnitude: Double = 0.0
    private var place: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var depth: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().run {
            title = getString(DETAIL_TITLE, "")
            magnitude = getDouble(DETAIL_MAGNITUDE)
            place = getString(DETAIL_PLACE, "")

            getDoubleArray(DETAIL_COORDINATES)?.run {
                longitude = getOrElse(0) { 0.0 }
                latitude = getOrElse(1) { 0.0 }
                depth = getOrElse(2) { 0.0 }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().run {
            onBackPressedDispatcher.addCallback {
                findNavController().navigateUp()
            }
        }

        binding.earthquakeItemTitle.text = title
        binding.earthquakeItemPlace.text = place
        binding.earthquakeItemMagnitude.text = requireContext().resources.getString(
            R.string.earthquake_item_magnitude, "$magnitude"
        )
        binding.earthquakeItemDepth.text = requireContext().resources.getString(
            R.string.earthquake_item_depth, "$depth"
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpMap() {
        Configuration.getInstance().load(requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext()))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        Configuration.getInstance().isDebugMode = true
        when {
            latitude == 0.0 && longitude == 0.0 -> binding.map.invisible()
            else -> {
                binding.map.run {
                    viewLifecycleOwner.lifecycleScope.launch {
                        visible()
                        setTileSource(TileSourceFactory.MAPNIK)
                        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                        setMultiTouchControls(false)

                        controller.setZoom(DEFAULT_ZOOM)
                        binding.map.addMapListener(object : MapListener {
                            override fun onScroll(event: ScrollEvent?) = false

                            override fun onZoom(event: ZoomEvent?) = false
                        })
                        binding.map.isFlingEnabled = false
                        binding.map.setOnTouchListener { _, _ ->
                            true
                        }
                        val startPoint = GeoPoint(latitude, longitude)
                        controller.setCenter(startPoint)

                        val item = OverlayItem("", "", GeoPoint(latitude, longitude))
                        val overlay = ItemizedIconOverlay(listOf(item), object:
                            OnItemGestureListener<OverlayItem> {
                            override fun onItemSingleTapUp(index:Int, item:OverlayItem):Boolean {
                                return true
                            }
                            override fun onItemLongPress(index:Int, item:OverlayItem):Boolean {
                                return false
                            }
                        }, context)

                        overlays.add(overlay)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        setUpMap()
    }

    override fun onPause() {
        binding.map.onPause()
        removeOverlay()
        super.onPause()
    }

    private fun removeOverlay() {
        binding.map.overlays.clear()
    }
}