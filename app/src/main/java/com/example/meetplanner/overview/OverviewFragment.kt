package com.example.meetplanner.overview

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.meetplanner.R
import com.example.meetplanner.authentication.AuthViewModel
import com.example.meetplanner.authentication.AuthViewModel.AuthenticationState
import com.example.meetplanner.databinding.OverviewFragmentBinding
import kotlinx.android.synthetic.main.activity_main.*

class OverviewFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    private lateinit var binding: OverviewFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.overview_fragment, container, false
        )
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.overviewList.adapter = OverviewAdapter(OverviewAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })
        val topSpacing = TopSpacingItemDecoration(30)
        binding.overviewList.addItemDecoration(topSpacing)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        authViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState == AuthenticationState.UNAUTHENTICATED) {
                navController.navigate(R.id.authFragment)
            }
            if (authenticationState == AuthenticationState.AUTHENTICATED) {
                viewModel.getMeetings()
            }
        })
        viewModel.authenticationFailed.observe(viewLifecycleOwner, Observer { authenticationFailed ->
            if (authenticationFailed == true) {
                viewModel.resetAuthenticationFailed()
                authViewModel.logout()
            }
        })
        activity?.navView?.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout_btn -> {
                    authViewModel.logout()
                    true
                }
                R.id.create_btn -> {
                    this.findNavController().navigate(
                        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment()
                    )
                    true
                }
                else -> false
            }
        }
        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    OverviewFragmentDirections
                        .actionOverviewFragmentToDetailsFragment(it))
                viewModel.displayDetailsComplete()
            }
        })
    }
}
