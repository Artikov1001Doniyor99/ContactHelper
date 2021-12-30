package uz.artikov.modul_6_lesson_4_1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.artikov.modul_6_lesson_4_1.databinding.FragmentSendMessageBinding
import uz.artikov.modul_6_lesson_4_1.models.Contact

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "contact"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SendMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendMessageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Contact? = null
    private var param2: String? = null


    lateinit var binding: FragmentSendMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSendMessageBinding.inflate(inflater,container,false)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Contact
        }

        binding.name.text = param1!!.name
        binding.phone.text = param1!!.number

        binding.sendMessage.setOnClickListener {
            if(ContextCompat.checkSelfPermission(binding.root.context,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                val message = binding.message.text.toString().trim()
                if (message.isNotEmpty()){
                    val number = param1!!.number
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(number,null,message,null,null)
                    findNavController().popBackStack()
                }else{
                    Toast.makeText(binding.root.context, "Bo'sh", Toast.LENGTH_SHORT).show()
                }
            }else{
                checkAllPermission()
            }
        }


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    private fun checkAllPermission() {
        askPermission(
            Manifest.permission.SEND_SMS,
        ) {
            //all permissions already granted or just granted


        }.onDeclined { e ->
            if (e.hasDenied()) {
                //the list of denied permissions
                AlertDialog.Builder(binding.root.context)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->

                        dialog.dismiss()
                    }
                    .show()

            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'
                // you need to open setting manually if you really need it
                e.goToSettings()
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SendMessageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendMessageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}