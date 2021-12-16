package com.example.food_ordering_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyOrders : AppCompatActivity() {
    lateinit var session:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        var apiclient=application as ApiClient
        session= SessionManager(this)
        var dishes:MutableList<dish> = mutableListOf<dish>()
        var token=session.fetchAuthToken()
        CoroutineScope(Dispatchers.IO).launch{
            var result=apiclient.apiService.fetchOrders("Bearer "+token)
            if(result.isSuccessful) {
                var i = 0
                while(i<result.body()!!.dishOrders!!.size){
                    var res=apiclient.apiService.GetDishes("Bearer "+token)
                    var j=0
                    while(j<res.body()!!.dishes!!.size){
                        if(result.body()!!.dishOrders!![i].dishId==res.body()!!.dishes!![j].id)
                            dishes.add(dish(res.body()!!.dishes!![j].id,res.body()!!.dishes!![j].url,res.body()!!.dishes!![j].name,res.body()!!.dishes!![j].price,result.body()!!.dishOrders!![i].count,result.body()!!.dishOrders!![i].orderId,res.body()!!.dishes!![j].type))
                        j+=1

                    }
                    i+=1
                }

            }
            withContext(Dispatchers.Main){
                var recycle=findViewById<RecyclerView>(R.id.recyclerr)
                recycle.adapter=OrdersAdapterClass(dishes,this@MyOrders,application)
                recycle.layoutManager= LinearLayoutManager(this@MyOrders)
            }
        }

    }
}