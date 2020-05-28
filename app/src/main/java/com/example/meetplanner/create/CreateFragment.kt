package com.example.meetplanner.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import com.example.meetplanner.authentication.AuthViewModel
import com.example.meetplanner.databinding.CreateMeetingFragmentBinding
import com.example.meetplanner.overview.OverviewViewModel

class CreateFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreateMeetingFragmentBinding.inflate(inflater)
        return binding.root
    }
}