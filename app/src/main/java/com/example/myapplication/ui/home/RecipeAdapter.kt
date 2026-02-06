package com.example.myapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRecipeBinding

data class Recipe(
    val name: String,
    val description: String
)

class RecipeAdapter(
    private val items: List<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(
        private val binding: ItemRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvRecipeName.text = recipe.name
            binding.tvRecipeDescription.text = recipe.description
            binding.root.setOnClickListener {
                onClick(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}