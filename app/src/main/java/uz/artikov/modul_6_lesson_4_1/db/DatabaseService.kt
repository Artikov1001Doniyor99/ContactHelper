package uz.artikov.modul_6_lesson_4_1.db

import uz.artikov.modul_6_lesson_4_1.models.ContactHelper

interface DatabaseService {

    fun addContactHelper(contactHelper: ContactHelper)
    fun getContactById(id:Int):ContactHelper
    fun getAllContact():List<ContactHelper>

}