package cl.itau.challenge.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import cl.itau.challenge.R
import cl.itau.challenge.databinding.FragmentDashboardBinding
import cl.itau.challenge.presentation.model.EarthquakeModel
import cl.itau.challenge.presentation.model.UiState
import cl.itau.challenge.presentation.ui.adapter.EarthquakeAdapter
import cl.itau.challenge.presentation.utilities.gone
import cl.itau.challenge.presentation.utilities.visible
import cl.itau.challenge.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

private const val LIMIT = 10

class DashboardFragment : Fragment(), MenuProvider {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModel<MainViewModel>()

    private val onItemClickListener = { earthquake: EarthquakeModel ->
        findNavController().navigate(
            resId = R.id.action_dashboardFragment_to_detailFragment,
            args = Bundle().apply {
                putString(DETAIL_TITLE, earthquake.properties.title)
                putString(DETAIL_PLACE, earthquake.properties.place)
                putDouble(DETAIL_MAGNITUDE, earthquake.properties.mag)
                putDoubleArray(DETAIL_COORDINATES, earthquake.geometryModel.coordinates.toDoubleArray())
            }
        )
    }

    private val onApplyFilter = { startDate: LocalDate, endDate: LocalDate ->
        this.startDate = startDate
        this.endDate = endDate

        mainViewModel.getEarthquakes(this.startDate, this.endDate)
    }

    private var isLoading = false

    private val earthquakeAdapter by lazy {
        EarthquakeAdapter(
            onItemClickListener,
            onApplyFilter
        )
    }

    private var startDate: LocalDate = LocalDate.now().minusDays(1)
    private var endDate: LocalDate = LocalDate.now()
    private var limit: Int = LIMIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getEarthquakes(startDate, endDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        requireActivity().run {
            addMenuProvider(
                this@DashboardFragment,
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
            onBackPressedDispatcher.addCallback {
                requireActivity().finish()
            }

            if (this is AppCompatActivity) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        ).also {
            binding.recycler.layoutManager = it
            binding.recycler.addOnScrollListener(object : OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val visibleItemCount = it.childCount
                        val totalItemCount = it.itemCount
                        val pastVisibleItems = it.findFirstVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isLoading) {
                            loadMoreItems()
                            isLoading = true
                        }
                    }
                }
            })
        }

        binding.recycler.adapter = earthquakeAdapter
        binding.swipeRefresh.setOnRefreshListener {
            startDate = LocalDate.now().minusDays(1)
            endDate = LocalDate.now()

            mainViewModel.getEarthquakes(startDate, endDate)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.dashboardUiState.distinctUntilChanged { old, new ->
                    old.hashCode() == new.hashCode()
                }.collect { uiState ->
                    when (uiState) {
                        is UiState.PrivateDashboard -> {
                            isLoading = false
                            binding.swipeRefresh.isRefreshing = false
                            binding.swipeRefresh.visible()
                            binding.error.gone()

                            earthquakeAdapter.submitList(uiState.listItems)
                        }
                        is UiState.PrivateDashboardError -> {
                            binding.swipeRefresh.isRefreshing = false
                            binding.swipeRefresh.gone()
                            binding.error.visible()

                            binding.errorText.text = uiState.throwable?.message ?: ""
                            binding.errorButton.setOnClickListener {
                                mainViewModel.getEarthquakes(startDate, endDate)
                            }
                        }
                        is UiState.PrivateDashboardLoading -> {
                            binding.swipeRefresh.visible()
                            binding.error.gone()

                            binding.swipeRefresh.isRefreshing = true
                        }
                        else -> { }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_sign_out -> {
                findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
                true
            }
            else -> { false }
        }
    }

    private fun loadMoreItems() {
        limit += LIMIT
        mainViewModel.getEarthquakes(startDate, endDate, limit = limit)
    }
}