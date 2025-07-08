package sanjarbek.uz.baraka.ui.screens.splash_screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sanjarbek.uz.baraka.MyApplication
import sanjarbek.uz.baraka.R
import sanjarbek.uz.baraka.core.constants.Constants
import sanjarbek.uz.baraka.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = MyApplication.sharedPrefs.getString(Constants.TOKEN, "")
        Handler(Looper.getMainLooper()).postDelayed({
            if (token.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_splashFragment_to_signUpSplashFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}