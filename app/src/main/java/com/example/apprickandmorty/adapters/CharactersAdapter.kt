package com.example.apprickandmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apprickandmorty.data.Character
import com.squareup.picasso.Picasso

class CharactersAdapter(
    private var items: List<Character>,
    val onItemClick: (position: Int) -> Unit
): Adapter<SuperheroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_superhero, parent, false)
        val binding = ItemSuperheroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperheroViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        val superhero = items[position]
        holder.render(superhero)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    fun updateItems(items: List<Character>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class SuperheroViewHolder(val binding: ItemSuperheroBinding) : ViewHolder(binding.root) {

    fun render(superhero: Character) {
        binding.nameTextView.text = superhero.name
        Picasso.get().load(character.image.url).into(binding.avatarImageView)
    }
}