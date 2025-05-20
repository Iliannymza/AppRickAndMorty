package com.example.apprickandmorty.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apprickandmorty.R
import com.example.apprickandmorty.data.Character

class CharacterAdapter : ListAdapter
<Character, CharacterAdapter.ViewHolder>(CharacterDiffCallback()) {

    //Capturar el Clic con Lambda
    private var onItemClickListener: ((Character) -> Unit)? = null

    fun setOnItemClickListener(listener: (Character) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    // No necesitas implementar getItemCount() con ListAdapter, lo maneja automáticamente.

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCharacter: ImageView = itemView.findViewById(R.id.imageViewCharacter)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)

        // Inicializa el listener del clic en el ViewHolder [cite: 53, 119]
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                // Asegúrate de que la posición sea válida y que haya un listener
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener?.invoke(getItem(position)) // Llama a la función lambda con el personaje clickeado [cite: 54, 119]
                }
            }
        }

        fun bind(character: Character) {
            textViewName.text = character.name
            textViewStatus.text = "${character.status} - ${character.species}"


            Glide.with(itemView.context)
                .load(character.image)
                .placeholder(R.drawable.ic_launcher_background) // Imagen placeholder
                .error(R.drawable.ic_launcher_background) // Imagen en caso de error
                .into(imageViewCharacter)
        }
    }

    // DiffUtil para actualizaciones eficientes del RecyclerView
    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}