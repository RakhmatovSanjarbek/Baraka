package sanjarbek.uz.baraka.ui.screens.splash_screen.sign_up_splash.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sanjarbek.uz.baraka.ui.screens.splash_screen.sign_up_splash.FirstSplashFragment
import sanjarbek.uz.baraka.ui.screens.splash_screen.sign_up_splash.SecondSplashFragment
import sanjarbek.uz.baraka.ui.screens.splash_screen.sign_up_splash.ThirdSplashFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        FirstSplashFragment(),
        SecondSplashFragment(),
        ThirdSplashFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}