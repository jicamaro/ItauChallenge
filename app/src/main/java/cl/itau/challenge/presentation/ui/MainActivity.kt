package cl.itau.challenge.presentation.ui

import android.os.Bundle
import android.widget.Toast.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import cl.itau.challenge.R
import cl.itau.challenge.databinding.ActivityMainBinding
import cl.itau.challenge.presentation.utilities.SnackbarHost

class MainActivity : AppCompatActivity(), SnackbarHost {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val snackbar: Snackbar by lazy { Snackbar.make(binding.root, "", LENGTH_LONG) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun show(message: String) {
        if (message.isNotEmpty()) {
            snackbar.setText(message)
            snackbar.show()
        }
    }
}