package uz.artikov.modul_6_lesson_4_1.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.artikov.modul_6_lesson_4_1.models.ContactHelper
import uz.artikov.modul_6_lesson_4_1.utils.Constant

class MyDbHelper(context: Context) :SQLiteOpenHelper(context,Constant.DB_NAME,null,Constant.DB_VERSION),DatabaseService{
    override fun onCreate(p0: SQLiteDatabase?) {
        val query =
            "create table ${Constant.TABLE_NAME} (${Constant.ID} integer not null primary key autoincrement unique,${Constant.NAME} Text not null,${Constant.NUMBER} Text not null,${Constant.TEXT} Text not null)"

        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        p0?.execSQL("drop table if exists ${Constant.TABLE_NAME}")
        onCreate(p0)

    }

    override fun addContactHelper(contactHelper: ContactHelper) {
        val database = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constant.NAME, contactHelper.name)
        contentValues.put(Constant.NUMBER, contactHelper.number)
        contentValues.put(Constant.TEXT, contactHelper.text)

        database.insert(Constant.TABLE_NAME, null, contentValues)
        database.close()
    }



    override fun getContactById(id: Int): ContactHelper {

        val database = this.readableDatabase


        val cursor = database.query(
            Constant.TABLE_NAME,
            arrayOf(Constant.ID, Constant.NAME, Constant.NUMBER, Constant.TEXT),
            "${Constant.ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        cursor?.moveToFirst()


        return ContactHelper(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3)
        )

    }

    override fun getAllContact(): ArrayList<ContactHelper> {
        val list = ArrayList<ContactHelper>()
        var query = "select * from ${Constant.TABLE_NAME}"

        val database = this.readableDatabase

        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)
                val text = cursor.getString(3)

                val contactHelper = ContactHelper(id, name, number, text)
                list.add(contactHelper)


            } while (cursor.moveToNext())
        }

        return list

    }
}