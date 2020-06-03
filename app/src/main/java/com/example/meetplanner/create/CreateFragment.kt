package com.example.meetplanner.create

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.meetplanner.authentication.AuthViewModel
import com.example.meetplanner.databinding.CreateMeetingFragmentBinding
import kotlinx.android.synthetic.main.activity_main.*

class CreateFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private val viewModel: CreateViewModel by lazy {
        ViewModelProviders.of(this).get(CreateViewModel::class.java)
    }

    private lateinit var binding: CreateMeetingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateMeetingFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.meeting.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context,"Meeting ${it.name} created", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        })
        viewModel.authenticationFailed.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                viewModel.resetAuthenticationFailed()
                authViewModel.logout()
            }
        })
        binding.createMeetingBtn.setOnClickListener{
            viewModel.createMeeting(
                binding.nameInput.text.toString(),
                binding.descriptionInput.text.toString()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val drawerLayout = activity!!.drawerLayout
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
