package sanjarbek.uz.baraka.ui.screens.profile_screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import sanjarbek.uz.baraka.MyApplication
import sanjarbek.uz.baraka.R
import sanjarbek.uz.baraka.core.constants.Constants
import sanjarbek.uz.baraka.core.data.moc_data.Data.imageList
import sanjarbek.uz.baraka.core.event.GetUsersEvent
import sanjarbek.uz.baraka.databinding.FragmentProfileBinding
import sanjarbek.uz.baraka.ui.screens.home_screen.HomeViewModel
import sanjarbek.uz.barakaadmin.core.extension.toast
import java.util.Calendar

@SuppressLint("SetTextI18n")
class ProfileFragment : Fragment() {

    private val vm: HomeViewModel by viewModels()

    private var imageIndex = 0

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getUsers()
        observe()
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnLogUot.setOnClickListener {
                showAlertDialog()
            }
            btnSelectLanguage.setOnClickListener {
                showToast()
            }
            btnSelectThem.setOnClickListener {
                showToast()
            }
            btnAppInfo.setOnClickListener {
                showToast()
            }
            val index =
                MyApplication.sharedPrefs.getString(Constants.SELECT_IMAGE, "0")?.toIntOrNull() ?: 0
            ivAvatar.setImageResource(imageList[index])
            selectImage.setOnClickListener {
                imageIndex = (imageIndex + 1) % imageList.size
                uploadImage(imageIndex)
                ivAvatar.setImageResource(imageList[imageIndex])
            }
        }
    }

    private fun observe() = with(binding) {
        vm.getUserEvent.observe(viewLifecycleOwner) {
            when (it) {
                is GetUsersEvent.Error -> {
                    toast(it.message)
                    llUserInfo.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }

                GetUsersEvent.Loading -> {
                    llUserInfo.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }

                is GetUsersEvent.Success -> {
                    llUserInfo.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    tvUserFullName.text = "${it.user.userSurname} ${it.user.userName}"
                    tvFullName.text = "${it.user.userSurname} ${it.user.userName}"
                    tvAge.text = "${getAgeFromMillis(it.user.userBornDate)} yosh"
                    tvUserAddress.text = it.user.userAddress
                    tvAllScore.text = "${it.user.userAllScore} ball"
                }
            }
        }
    }

    private fun uploadImage(index: Int) {
        MyApplication.sharedPrefs
            .edit()
            .putString(Constants.SELECT_IMAGE, index.toString()).apply()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showAlertDialog() {

        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val title = dialogView.findViewById<TextView>(R.id.tv_title)
        val message = dialogView.findViewById<TextView>(R.id.tv_message)
        val btnConfirm = dialogView.findViewById<MaterialButton>(R.id.btn_confirm)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)


        title.text = "Diqqat!"
        message.text = "Siz haqiqatdan ham hisobdan chiqmoqchimisiz"

        btnCancel.text = getString(R.string.txt_no)
        btnConfirm.text = getString(R.string.txt_yes)

        val dialog = builder.create()

        btnConfirm.setOnClickListener {
            MyApplication.sharedPrefs.edit().clear().apply()
            findNavController().navigate(
                R.id.action_profileFragment_to_signInFragment
            )
            dialog.dismiss()

        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        dialog.setCancelable(false)
    }

    private fun getAgeFromMillis(birthMillis: Long): Int {
        val birthCalendar = Calendar.getInstance()
        birthCalendar.timeInMillis = birthMillis

        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        if (today.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))
        ) {
            age--
        }

        return age
    }

    private fun showToast() {
        toast("Tez kunda ishga tushadi")
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        vm.removeListener()
    }
}