package sanjarbek.uz.baraka.ui.screens.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import sanjarbek.uz.baraka.MyApplication
import sanjarbek.uz.baraka.R
import sanjarbek.uz.baraka.core.constants.Constants
import sanjarbek.uz.baraka.core.data.moc_data.Data.imageList
import sanjarbek.uz.baraka.core.event.GetProductsEvent
import sanjarbek.uz.baraka.core.event.GetUsersEvent
import sanjarbek.uz.baraka.databinding.FragmentHomeBinding
import sanjarbek.uz.baraka.ui.custom_view.CustomDetailBottomSheetDialog

@RequiresApi(35)
class HomeFragment : Fragment() {

    private val vm: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HomeScreenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            loading()
            swipeRefresh.setOnRefreshListener {
                loading()
            }
            llToProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
            llScore.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_scoreFragment)
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                requireActivity().finish()
            }
        }
    }

    private fun loading() = with(binding) {
        vm.getUsers()
        vm.getProducts()
        with(binding){
            lottieEmpty.visibility = View.VISIBLE
            lottieEmpty.playAnimation()
            rv.visibility = View.GONE
        }
        val imageIndex =
            MyApplication.sharedPrefs.getString(Constants.SELECT_IMAGE, "0")?.toIntOrNull() ?: 0
        profileImage.setImageResource(imageList[imageIndex])
        adapter = HomeScreenAdapter { homeData ->
            val bottomSheet = CustomDetailBottomSheetDialog.newInstance(homeData)
            bottomSheet.show(parentFragmentManager, "CustomBottomSheet")
        }
        observe()
    }

    private fun observe() = with(binding) {
        vm.getUserEvent.observe(viewLifecycleOwner) {
            when (it) {
                is GetUsersEvent.Error -> {
                    swipeRefresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                }

                GetUsersEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    swipeRefresh.isRefreshing = true
                    rv.visibility = View.GONE
                    toolbar.visibility = View.GONE
                }

                is GetUsersEvent.Success -> {
                    swipeRefresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                    toolbar.visibility = View.VISIBLE
                    tvUserName.text = it.user.userName
                }
            }
        }

        vm.getProductEvent.observe(viewLifecycleOwner) { it2 ->
            when (it2) {
                is GetProductsEvent.Error -> {
                    swipeRefresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    rv.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }

                GetProductsEvent.Loading -> {
                    toolbar.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE
                    swipeRefresh.isRefreshing = true
                    rv.visibility = View.GONE
                }

                is GetProductsEvent.Success -> {
                    swipeRefresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                    toolbar.visibility = View.VISIBLE

                    rv.adapter = adapter

                    println("::::::::::: ${it2.products}")

                    if (it2.products.isEmpty()) {
                        lottieEmpty.visibility = View.VISIBLE
                        lottieEmpty.playAnimation()
                        rv.visibility = View.GONE
                    } else {
                        lottieEmpty.cancelAnimation()
                        lottieEmpty.visibility = View.GONE
                        rv.visibility = View.VISIBLE

                        val sortedList = it2.products.sortedByDescending { it.date }
                        adapter.updateList(sortedList)

                        val totalScore = it2.products.sumOf { it.score ?: 0.0 }
                        val roundedTotal = "%.1f".format(totalScore)
                        tvScore.text = roundedTotal
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        vm.removeListener()
    }
}