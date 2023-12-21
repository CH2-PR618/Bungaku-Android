package com.nccdms.bloomiqpro.utils

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment:Fragment() {
    fun showToast(message:String?){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    fun permissionGranted(permission: Permission): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission.value
        ) == PackageManager.PERMISSION_GRANTED

}