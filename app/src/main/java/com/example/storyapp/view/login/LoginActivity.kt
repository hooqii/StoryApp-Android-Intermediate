package com.example.storyapp.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.preferences.dataStore
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                LoginPreferences.getInstance(dataStore)
            )
        val viewModel: LoginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnLoginPage.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = getString(R.string.input_email)
                }

                password.isEmpty() -> {
                    binding.edLoginPassword.error = getString(R.string.input_password)
                }

                else -> {
                    viewModel.login(email, password).observe(this) {
                        if (it != null) {
                            when (it) {
                                is com.example.storyapp.data.Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is com.example.storyapp.data.Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    val response = it.data
                                    viewModel.saveState(response.token.toString())
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.success))
                                        setMessage(getString(R.string.welcome_back) + " " + "${response.name}")
                                        setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                                            startActivity(
                                                Intent(
                                                    this@LoginActivity,
                                                    MainActivity::class.java
                                                )
                                            )
                                        }
                                        create()
                                        show()
                                    }.apply {
                                        setOnCancelListener {// Set an OnCancelListener to handle the case when the user clicks outside of the dialog
                                            startActivity(
                                                Intent(
                                                    this@LoginActivity,
                                                    MainActivity::class.java
                                                )
                                            )
                                        }
                                        show()
                                    }
                                }

                                is com.example.storyapp.data.Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.error))
                                        setMessage(it.error)
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
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}