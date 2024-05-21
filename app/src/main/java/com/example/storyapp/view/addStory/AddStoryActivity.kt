package com.example.storyapp.view.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.preferences.dataStore
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.util.getImageUri
import com.example.storyapp.util.uriToFile
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private var currentImageUri: Uri? = null
    private var file: File? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .asGif()
            .load(R.drawable.wawan)
            .into(binding.ivPreview)

        binding.progressBar.visibility = View.GONE

        setSupportActionBar(binding.toolbar)
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                LoginPreferences.getInstance(dataStore)
            )

        viewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]

        binding.apply {
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnAdd.setOnClickListener {
                uploadStory()
            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean -> }

    private fun requestLocationPermissionAndGetLocation(callback: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                callback(location)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                file = uriToFile(it!!, this)
            }
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.let {
                file = uriToFile(it!!, this)
            }
            showImage()
        }
    }

    private fun uploadStory() {
        val description = binding.edDescription.text.toString()
        if (binding.switchLocation.isChecked) {
            requestLocationPermissionAndGetLocation {
                if (it != null) {
                    upload(description, it.latitude.toFloat(), it.longitude.toFloat())
                } else {
                    Toast.makeText(this, "Location failed.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            upload(description)
        }
    }

    private fun upload(description: String, lat: Float? = null, lon: Float? = null) {
        viewModel.uploadStory(file, description, lat, lon).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val response = result.data
                        Toast.makeText(
                            this,
                            response.message,
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.error))
                            setMessage(result.error)
                            setPositiveButton(getString(R.string.continue_dialog)) { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }
}
