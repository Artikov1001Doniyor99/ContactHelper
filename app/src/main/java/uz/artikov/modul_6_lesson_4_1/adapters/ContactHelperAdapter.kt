package uz.artikov.modul_6_lesson_4_1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.artikov.modul_6_lesson_4_1.databinding.ItemContactHelperBinding
import uz.artikov.modul_6_lesson_4_1.models.Contact

class ContactHelperAdapter(var list: ArrayList<Contact>, var onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ContactHelperAdapter.Vh>() {

    inner class Vh(var itemContactHelperBinding: ItemContactHelperBinding) :
        RecyclerView.ViewHolder(itemContactHelperBinding.root) {
        fun onBind(contact: Contact, position: Int) {

            itemContactHelperBinding.phone.text = contact.number
            itemContactHelperBinding.name.text = contact.name

            itemContactHelperBinding.callBtn.setOnClickListener {
                onItemClickListener.onPhoneClick(contact)
            }

            itemContactHelperBinding.messageBtn.setOnClickListener {
                onItemClickListener.onMessageClick(contact)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemContactHelperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onPhoneClick(contactHelper: Contact)
        fun onMessageClick(contactHelper: Contact)
    }


}