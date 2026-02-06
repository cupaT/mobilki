package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val recipes = listOf(
        Recipe("Pasta Carbonara", "Classic Italian pasta with bacon and parmesan."),
        Recipe("Chicken Curry", "Spicy chicken curry with coconut milk."),
        Recipe("Chocolate Cake", "Rich and moist chocolate cake.")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = RecipeAdapter(recipes) { recipe ->
            // Переход на DetailsFragment с передачей имени рецепта через Bundle
            val bundle = Bundle().apply {
                putString("recipeName", recipe.name)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment,
                bundle
            )
        }

        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = adapter

        binding.btnAbout.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}