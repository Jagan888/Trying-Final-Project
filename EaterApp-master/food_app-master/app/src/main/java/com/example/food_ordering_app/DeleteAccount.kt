package com.example.food_ordering_app

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteAccount : AppCompatActivity() {
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)
        val button = findViewById<Button>(R.id.delbutton)
        val email=findViewById<TextView>(R.id.delemail)
        session = SessionManager(this@DeleteAccount)
        email.text="${session.FetchUserID()}"
        val intnt=Intent(this,MainActivity::class.java)
        val apiClient = application as ApiClient

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_screen)
        dialog.setCanceledOnTouchOutside(false)

        var token = session.fetchAuthToken()
        button.setOnClickListener {
            dialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    var result = apiClient.apiService.delete(
                        "Bearer " + token

                    )
                    startActivity(intnt)

                }

            }

        }
    }
}