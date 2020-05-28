package com.example.meetplanner.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.meetplanner.databinding.MeetingDetailsLayoutBinding

class DetailsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = MeetingDetailsLayoutBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val property = DetailsFragmentArgs.fromBundle(arguments!!).property
        val viewModelFactory = DetailViewModelFactory(property, application)
        binding.viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(DetailsViewModel::class.java)
        return binding.root
    }
}