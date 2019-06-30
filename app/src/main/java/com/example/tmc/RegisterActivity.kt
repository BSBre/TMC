package com.example.tmc

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tmc.beans.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_user.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var ref: DatabaseReference
    lateinit var users: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        users = mutableListOf()

        ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addValueEventListener(object: ValueEventListener{
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

        registerUserButton.setOnClickListener {
            val email: String = registerEmailEditText.text.toString()
            val password: String = registerPasswordEditText.text.toString()

            if(email.equals("") || password.equals("")) {
                error("Empty username or password")
            } else if(userExists(email)) {
                error("Username already taken")
            } else {
                insertUser(email, password)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("registered", true)
                startActivity(intent)
            }
        }
    }

    private fun insertUser(email: String, password: String) {
        database = FirebaseDatabase.getInstance().getReference("users")
        val userId = database.push().key

        val user = User(userId, email, password)

        userId?.let {
            database.child(it).setValue(user)
        }

    }

    private fun userExists(email: String): Boolean {
        for(user in users) {
            if(email.equals(user.email)){
                return true
            }
        }
        return false
    }

    private fun error(errorMessage: String) {
        val builder = AlertDialog.Builder(this)

        val positiveButtonClick = { _: DialogInterface, _: Int  ->
            Toast.makeText(applicationContext, android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        with(builder) {
            setTitle("Registration error")
            setMessage(errorMessage)
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }
}
