package uz.artikov.modul_6_lesson_4_1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.artikov.modul_6_lesson_4_1.adapters.ContactHelperAdapter
import uz.artikov.modul_6_lesson_4_1.databinding.FragmentHomeBinding
import uz.artikov.modul_6_lesson_4_1.db.MyDbHelper
import uz.artikov.modul_6_lesson_4_1.models.Contact
import uz.artikov.modul_6_lesson_4_1.models.ContactHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding:FragmentHomeBinding
    lateinit var myDbHelper: MyDbHelper

    lateinit var list:ArrayList<Contact>
    lateinit var listSort:ArrayList<Contact>
    lateinit var contactHelperAdapter: ContactHelperAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)




        myDbHelper = MyDbHelper(requireContext())

        val contactHelper = ContactHelper()

        list = ArrayList()
        listSort = ArrayList()

        //list = myDbHelper.getAllContact()


        checkAllPermission()









        return binding.root
    }

    @SuppressLint("Range")
    private fun loadContacts() {
        val contentResolver = requireActivity().contentResolver

        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (phones!!.moveToNext()){
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

            val number =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            list.add(Contact(name, number))

/*            list.forEach {
                if (it.name)
            }*/

        }
        phones.close()

        list.sortBy {
            it.name
        }

    }

    private fun checkAllPermission() {
        askPermission(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE
        ) {
            //all permissions already granted or just granted
            loadContacts()
            loadAdapter()

        }.onDeclined { e ->
            if (e.hasDenied()) {
                //the list of denied permissions
                if (ContextCompat.checkSelfPermission(
                        binding.root.context,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    loadContacts()
                }
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

    private fun loadAdapter() {
        contactHelperAdapter = ContactHelperAdapter(list,object : ContactHelperAdapter.OnItemClickListener{
            override fun onPhoneClick(contactHelper: Contact) {
                if(ContextCompat.checkSelfPermission(
                        binding.root.context,
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED){
                    val phoneNumber = contactHelper.number
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:$phoneNumber")
                    startActivity(intent)
                }else{
                    checkAllPermission()
                }
            }

            override fun onMessageClick(contactHelper: Contact) {
                val bundle = Bundle()
                bundle.putSerializable("contact",contactHelper)
                findNavController().navigate(R.id.sendMessageFragment,bundle)
            }


        })

        binding.contactsRv.adapter = contactHelperAdapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}