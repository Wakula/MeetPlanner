package com.example.meetplanner.authentication

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.meetplanner.R
import com.example.meetplanner.authentication.AuthViewModel.AuthenticationState
import com.example.meetplanner.databinding.AuthFragmentBinding
import kotlinx.android.synthetic.main.activity_main.*


class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    private lateinit var binding: AuthFragmentBinding

    private lateinit var emailEditText: EditText

    private lateinit var passwordEditText: EditText

    private lateinit var loginButton: Button

    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.auth_fragment, container, false
        )
        setHasOptionsMenu(true)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText = view.findViewById(R.id.email)
        passwordEditText = view.findViewById(R.id.password)
        registerButton = view.findViewById(R.id.registration_submit_btn)
        loginButton = view.findViewById(R.id.login_submit_btn)

        loginButton.setOnClickListener {
            viewModel.login(
                emailEditText.text.toString(), passwordEditText.text.toString()
            )
        }

        registerButton.setOnClickListener {
            viewModel.register(
                emailEditText.text.toString(), passwordEditText.text.toString()
            )
        }

        val navController = findNavController()

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            viewModel.refuseAuthentication()
//            navController.popBackStack(R.id.overviewFragment, false)
//        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    Log.v("IM navigating", "yes")
                    navController.popBackStack()
                }
                AuthenticationState.INVALID_AUTHENTICATION -> showErrorMessage()
            }
        })
    }

    private fun showErrorMessage() {
        Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.v("Working", "V")
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
        val drawerLayout = activity!!.findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
