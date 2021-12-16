package com.example.food_ordering_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity2 : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout


    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val profile=findViewById<TextView>(R.id.DisplayUserEmail)
       // profile.text="user@gmail.com"
        drawerLayout=findViewById(R.id.drawerLayout)

        val intent1=Intent(this,userProfile::class.java)
        val order= Intent(this,MyOrders::class.java)

        val otheruser=Intent(this,OtherUsersActivity::class.java)
        val pic=Intent(this,Camera::class.java)
        val navView=findViewById<NavigationView>(R.id.navView)

     //   val imgages=findViewById<ImageButton>(R.id.imageView)
        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       /* profile.text="${session.FetchUserID()}"*/
        navView.setNavigationItemSelectedListener {

            when(it.itemId) {

               R.id.profile -> {startActivity(intent1)}
                R.id.otherusers-> {startActivity(otheruser)}
                R.id.profilepic->{startActivity(pic)}
                R.id.order->{startActivity(order)}

            }
            true
        }
        val string=intent.getStringExtra("string")

        val apiclient=application as ApiClient
        session=SessionManager(this)
        var intent= Intent(this,User_Login::class.java)

        var token=session.fetchAuthToken()

        val items:MutableList<FooditemData> = mutableListOf<FooditemData>()
        if(session.fetchAuthToken()!=null){
            CoroutineScope(Dispatchers.IO).launch{
                val result=apiclient.apiService.GetDishes("Bearer "+token)
                var i=0
                if(result.isSuccessful){
                    while(i<result.body()?.dishes!!.size){
                        items.add(result.body()?.dishes!![i])
                        i+=1
                    }
                }
                else{
                    startActivity(intent)
                }
                withContext(Dispatchers.Main){

                    val recycle=findViewById<RecyclerView>(R.id.recycle)
                    recycle.adapter=AdapterClass(items,this@MainActivity2)
                    recycle.layoutManager= LinearLayoutManager(this@MainActivity2)
                }

            }
        }
      else
            startActivity(intent)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){

            return true
        }
        return super.onOptionsItemSelected(item)
    }


}