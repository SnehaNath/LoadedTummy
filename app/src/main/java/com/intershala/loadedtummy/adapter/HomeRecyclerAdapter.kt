package com.intershala.loadedtummy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.intershala.loadedtummy.R
import com.intershala.loadedtummy.model.RestaurantsDetails
import com.squareup.picasso.Picasso


class HomeRecyclerAdapter (val context : Context, val itemList : ArrayList<RestaurantsDetails>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgRestroImage : ImageView = view.findViewById(R.id.imgRestroImage)
        val txtRestaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val txtCostPerPerson : TextView = view.findViewById(R.id.txtCostPerPerson)
        val txtRating : TextView = view.findViewById(R.id.txtRating)

        val llHomePageItem : LinearLayout = view.findViewById(R.id.llHomePageItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurantsDetails = itemList[position]
        Picasso.get().load(restaurantsDetails.restaurantImageUrl).error(R.drawable.bydefault_restro_pic).into(holder.imgRestroImage)
        holder.txtRestaurantName.text = restaurantsDetails.restaurantName
        holder.txtCostPerPerson.text = restaurantsDetails.restaurantCostForOne
        holder.txtRating.text = restaurantsDetails.restaurantRating

        holder.llHomePageItem.setOnClickListener {
            Toast.makeText(context, "Item details will be showed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
