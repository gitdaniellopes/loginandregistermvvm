package com.ocanha.retrofitcomkotlin.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ocanha.retrofitcomkotlin.databinding.ActivityRegisterBinding
import com.ocanha.retrofitcomkotlin.model.User
import com.ocanha.retrofitcomkotlin.repositories.UserRepository
import com.ocanha.retrofitcomkotlin.rest.RetrofitService
import com.ocanha.retrofitcomkotlin.utils.Validator
import com.ocanha.retrofitcomkotlin.viewmodel.register.RegisterViewModel
import com.ocanha.retrofitcomkotlin.viewmodel.register.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        viewModel =
            ViewModelProvider(this, RegisterViewModelFactory(UserRepository(retrofitService))).get(
                RegisterViewModel::class.java
            )

        setupUi()

    }

    private fun setupUi() = _binding.apply {
        btnRegister.setOnClickListener {
            if (!Validator.validateName(edtName.text.toString())) {
                edtName.error = "Preencha o nome completo."
                edtName.requestFocus()
                return@setOnClickListener
            }

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

            loadingView.show()
            viewModel.register(
                User(
                    edtName.text.trim().toString(),
                    edtEmail.text.trim().toString(),
                    edtPassword.text.trim().toString()
                )
            )
        }
    }

    /**
     * no onCreate vai ser renderizada a tela, é um processamento caro, quer dizer, pesado.
     * O ponStart vai ser chamado depois do onCreate, quando a tela já foi renderizada
     * */
    override fun onStart() {
        super.onStart()

        viewModel.status.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Usuario registrado com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Erro ao registrar usuario. Tente novamente", Toast.LENGTH_SHORT
                ).show()
                _binding.loadingView.dismiss()
            }
        })
    }
}