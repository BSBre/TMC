package com.example.tmc

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tmc.beans.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_user.*


class MainActivity : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var users: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        users = mutableListOf()

        ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    users.clear()
                    for(h in p0.children) {
                        val user = h.getValue(User::class.java)
                        users.add(user!!)
                    }
                }
            }
        })

        registerButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        logInButton.setOnClickListener{
            val email: String = emailEditText.text.toString()
            val password: String = passwordEditText.text.toString()

            if(email.equals("") || password.equals("")) {
                error("Empty username or password")
            } else if(userExists(email, password)) {
                val intent = Intent(this, TMCActivity::class.java)
                startActivity(intent)
            } else {
                error("Wrong username or password")
            }
        }
    }

    private fun userExists(email: String, password: String): Boolean {
        for(user in users) {
            if(email.equals(user.email) && password.equals(user.password)){
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        val flag: Boolean = intent.getBooleanExtra("registered", false)
        if(flag) {
            Toast.makeText(applicationContext, "User saved successfully", Toast.LENGTH_LONG).show()
        }
        intent.removeExtra("registered")
    }

    private fun error(errorMessage: String) {
        val builder = AlertDialog.Builder(this)

        val positiveButtonClick = { _: DialogInterface, _: Int  ->
            Toast.makeText(applicationContext, android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        with(builder) {
            setTitle("Log In error")
            setMessage(errorMessage)
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }
}
