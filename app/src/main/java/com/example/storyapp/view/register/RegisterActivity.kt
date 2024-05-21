package com.example.storyapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.preferences.dataStore
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.login.LoginActivity
import com.example.storyapp.view.welcome.WelcomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                LoginPreferences.getInstance(dataStore)
            )
        val viewModel: RegisterViewModel =
            ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.register(name, email, password).observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is com.example.storyapp.data.Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is com.example.storyapp.data.Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val response = result.data
                            AlertDialog.Builder(this).apply {
                                setTitle(getString(R.string.success))
                                setMessage(response.message)
                                setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            WelcomeActivity::class.java
                                        )
                                    )
                                }
                                create()
                                show()
                            }.apply {
                                setOnCancelListener {
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            WelcomeActivity::class.java
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
}