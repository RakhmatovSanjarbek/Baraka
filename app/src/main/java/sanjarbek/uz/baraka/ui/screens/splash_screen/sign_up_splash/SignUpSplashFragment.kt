package sanjarbek.uz.baraka.ui.screens.splash_screen.sign_up_splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import sanjarbek.uz.baraka.R
import sanjarbek.uz.baraka.databinding.FragmentSignUpSplashBinding
import sanjarbek.uz.baraka.ui.screens.splash_screen.sign_up_splash.adapter.ViewPagerAdapter

class SignUpSplashFragment : Fragment() {

    private var _binding: FragmentSignUpSplashBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ViewPagerAdapter(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpSplashFragment_to_signInFragment)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == adapter.itemCount - 1) {
                    binding.btnSignIn.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.GONE
                    binding.icNext.visibility = View.GONE
                } else {
                    binding.btnSignIn.visibility = View.GONE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.icNext.visibility = View.VISIBLE
                }
            }
        })


        binding.viewPager.adapter = adapter

        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem += 1
            }
            if (binding.viewPager.currentItem == 2){
                binding.btnNext.visibility = View.GONE
                binding.icNext.visibility = View.GONE
                binding.btnSignIn.visibility=View.VISIBLE
            }
        }

        binding.btnPrevious.setOnClickListener {
            if (binding.viewPager.currentItem > 0) {
                binding.viewPager.currentItem -= 1
            }
            if (binding.viewPager.currentItem < 2){
                binding.btnNext.visibility = View.VISIBLE
                binding.icNext.visibility = View.VISIBLE
                binding.btnSignIn.visibility=View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
