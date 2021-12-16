package com.example.food_ordering_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtherUsersActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_users)
        val string=intent.getStringExtra("string")

        val apiclient=application as ApiClient
        session=SessionManager(this)
        var intent= Intent(this,User_Login::class.java)

        var token=session.fetchAuthToken()
        val items:MutableList<OtherUsersData> = mutableListOf<OtherUsersData>()
        if(session.fetchAuthToken()!=null){
            CoroutineScope(Dispatchers.IO).launch{
                val result=apiclient.apiService.GetUsers("Bearer "+token)
                var i=0
                if(result.isSuccessful){
                    while(i<result.body()?.users!!.size){
                        items.add(result.body()?.users!![i])
                        i+=1
                    }
                }
                else{
                    startActivity(intent)
                }
                withContext(Dispatchers.Main){
                    val recycle=findViewById<RecyclerView>(R.id.recycle1)
                    recycle.adapter=OtherUserAdapterClass(items)
                    recycle.layoutManager= LinearLayoutManager(this@OtherUsersActivity)
                }

            }
        }
        else
            startActivity(intent)
    }
    }
