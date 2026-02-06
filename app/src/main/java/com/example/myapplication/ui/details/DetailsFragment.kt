package com.example.myapplication.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    // Без SafeArgs – читаем аргумент напрямую из arguments
    private val recipeName: String
        get() = arguments?.getString("recipeName") ?: "Unknown recipe"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvRecipeTitle.text = recipeName
        binding.tvRecipeDetails.text = "Here you can show full recipe for $recipeName.\n\n" +
                "In a real app this text would be loaded from a database or network."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}