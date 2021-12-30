package uz.artikov.modul_6_lesson_4_1

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionActivity : AppCompatActivity() {

    var requestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        checkAudioPermission()

    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Contacts permission granted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
        } else {
            requestAudioPermission()
        }
    }

    private fun requestAudioPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_CONTACTS
            )
        ) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Permission to access the contacts is required")
                .setTitle("Permission required")
            builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    requestCode
                )
            }
            val dialog = builder.create()
            dialog.show()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


}