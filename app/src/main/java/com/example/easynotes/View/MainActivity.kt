package com.example.easynotes.View

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.easynotes.R
import com.example.easynotes.databinding.ActivityMainBinding
import com.example.easynotes.databinding.LoadingMainBinding

class MainActivity : AppCompatActivity() {



    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingBinding: LoadingMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        loadingBinding = LoadingMainBinding.inflate(layoutInflater)
        setContentView(loadingBinding.root)

        Handler().postDelayed({
            switchToMainLayout()
        }, 1500)


    }

    private fun switchToMainLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}