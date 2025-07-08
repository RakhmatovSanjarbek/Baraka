package sanjarbek.uz.baraka.ui.screens.sign_in

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import sanjarbek.uz.baraka.MyApplication
import sanjarbek.uz.baraka.R
import sanjarbek.uz.baraka.core.constants.Constants
import sanjarbek.uz.baraka.core.event.SignInEvent
import sanjarbek.uz.baraka.databinding.FragmentSignInBinding
import sanjarbek.uz.barakaadmin.core.extension.toast

class SignInFragment : Fragment() {

    private val vm: SignInViewModel by viewModels()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private var isShow = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEditTextListeners()

        with(binding) {
            btnSignIn.setOnClickListener {

                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnShowHidePassword.setOnClickListener {
                isShow = togglePasswordVisibility(
                    etUserPassword,
                    btnShowHidePassword,
                    isShow
                )
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                requireActivity().finish()
            }

            btnSignIn.setOnClickListener {
                vm.signIn(
                    etUserEmail.text.toString().trim(),
                    etUserPassword.text.toString().trim()
                )
            }
            vm.signInEvent.observe(viewLifecycleOwner) {
                when (it) {
                    is SignInEvent.Error -> {
                        toast(it.message)
                        sv.visibility = View.VISIBLE
                        btnSignIn.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }

                    SignInEvent.Loading -> {
                        sv.visibility = View.GONE
                        btnSignIn.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }

                    is SignInEvent.Success -> {
                        toast(it.message)
                        MyApplication.sharedPrefs.edit()
                            .putString(Constants.TOKEN, it.token)
                            .apply()
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    }
                }
            }
        }

    }

    private fun setupEditTextListeners() {
        with(binding) {
            etUserEmail.setOnFocusChangeListener { _, hasFocus ->
                selectEditText(hasFocus, cardUserId)
            }

            etUserPassword.setOnFocusChangeListener { _, hasFocus ->
                selectEditText(hasFocus, cardUserPassword)
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkInput()
                }

                override fun afterTextChanged(p0: Editable?) {}

            }
            etUserEmail.addTextChangedListener(textWatcher)
            etUserPassword.addTextChangedListener(textWatcher)
        }
    }

    private fun checkInput() = with(binding) {
        val isUserPassword = etUserPassword.text.toString().trim().length >= 6
        val isUserEmail =
            Patterns.EMAIL_ADDRESS.matcher(etUserEmail.text.toString().trim()).matches()
        btnSignIn.isEnabled = isUserEmail && isUserPassword
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        button: ImageView,
        isShowing: Boolean,
    ): Boolean {
        val newIsShow = !isShowing
        if (newIsShow) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            button.setImageResource(R.drawable.ic_show_password)
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            button.setImageResource(R.drawable.ic_hide_password)
        }
        editText.setSelection(editText.text.length)
        return newIsShow
    }

    private fun selectEditText(hasFocus: Boolean, materialCardView: MaterialCardView) {
        if (hasFocus) {
            materialCardView.strokeColor =
                ContextCompat.getColor(requireContext(), R.color.primary_color)
            materialCardView.strokeWidth = 3
        } else {
            materialCardView.strokeColor =
                ContextCompat.getColor(requireContext(), R.color.primary_light)
            materialCardView.strokeWidth = 1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}