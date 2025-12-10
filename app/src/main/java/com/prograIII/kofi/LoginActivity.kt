package com.prograIII.kofi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prograIII.kofi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = auth.currentUser
        if(currentUser != null){
            val intentUsuarioLogueado = Intent(this, MainActivity::class.java) //para redirigir a los usuarios que sya se loguearon
            startActivity(intentUsuarioLogueado)
        }


        binding.signIn.setOnClickListener{
            val correo = binding.correo.text.toString()
            val password = binding.pass.text.toString()
            loginUsuario(correo,password)

        }

        binding.crearCuenta.setOnClickListener {
            val correo = binding.correo.text.toString()
            val password = binding.pass.text.toString()
            crearUsuario(correo,password)
        }
    }


    fun loginUsuario(correo: String, password: String)
    {
        auth.signInWithEmailAndPassword(correo,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //Nuestro usuaro se logeo correctamente
                    val intentLogueado = Intent(this, MainActivity::class.java)
                    startActivity(intentLogueado)
                } else {
                    Toast.makeText(
                        baseContext,
                        "No pudo loguearse",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

    fun crearUsuario(
        correo: String,
        password: String
    ){
        auth.createUserWithEmailAndPassword(correo,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //mi usuario se creó correctamente
                } else{
                    Toast.makeText(
                        baseContext,
                        "No se pudo crear usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}