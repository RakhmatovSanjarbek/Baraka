package sanjarbek.uz.baraka.ui.custom_view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sanjarbek.uz.baraka.core.data.dto.ProductModel
import sanjarbek.uz.baraka.databinding.CustomDetailBottomSheetDialogBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class CustomDetailBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: CustomDetailBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CustomDetailBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productModel = arguments?.getParcelable<ProductModel>("userData")

        binding.tvData.text = productModel?.date?.let { convertMillisToDate(it) }
        productModel?.let {
            binding.tvScore.text = "${it.score.toString()} ball"
            binding.tvStatus.text = it.status.toString()
            binding.tvAccepted.text = it.recipient.toString()
            binding.tvProductStatus.text = it.description.toString()
        }
    }

    companion object {
        @RequiresApi(35)
        fun newInstance(productModel: ProductModel): CustomDetailBottomSheetDialog {
            val fragment = CustomDetailBottomSheetDialog()
            val args = Bundle()
            args.putParcelable("userData", productModel)
            fragment.arguments = args
            return fragment
        }
    }


    private fun convertMillisToDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = Date(millis)
        return sdf.format(date)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}