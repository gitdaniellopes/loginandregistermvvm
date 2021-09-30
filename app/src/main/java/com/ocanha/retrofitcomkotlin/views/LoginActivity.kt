package com.ocanha.retrofitcomkotlin.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ocanha.retrofitcomkotlin.databinding.ActivityLoginBinding
import com.ocanha.retrofitcomkotlin.model.LoginRequest
import com.ocanha.retrofitcomkotlin.repositories.UserRepository
import com.ocanha.retrofitcomkotlin.rest.RetrofitService
import com.ocanha.retrofitcomkotlin.utils.Validator
import com.ocanha.retrofitcomkotlin.viewmodel.login.LoginViewModel
import com.ocanha.retrofitcomkotlin.viewmodel.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        viewModel =
            ViewModelProvider(this, LoginViewModelFactory(UserRepository(retrofitService), application)).get(
                LoginViewModel::class.java
            )
        setupUi()
    }

    private fun setupUi() = _binding.apply {
        btnLogin.setOnClickListener {
            if (!Validator.validateEmail(edtEmail.text.toString())) {
                edtEmail.error = "Preencha o email corretamente."
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Validator.validatePassword(edtPassword.text.toString())) {
                edtPassword.error = "Preencha a senha de acesso"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            viewModel.login(
                LoginRequest(
                    edtEmail.text.trim().toString(),
                    edtPassword.text.trim().toString()
                )
            )
            loadingView.show()
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.success.observe(this, {
//            UserSession.setToken(it.token)
//            lifecycleScope.launch {
//                session.saveToken(it.token)
//            }
            viewModel.saveToken(it.token)
            startActivity(Intent(this, MainActivity::class.java))
        })

        viewModel.errorMessage.observe(this, {
            _binding.loadingView.dismiss()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}

//        session.token.asLiveData().observe(this, Observer {
//            val activity = if (it == null) LoginActivity::class.java else MainActivity::class.java
//            startActivity(Intent(this, activity))
//        })