package com.ccl3_id.wanderquest.handlers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * This class handles permissions in an Android application.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 *
 * @property activity The reference to the parent Activity.
 */
class PermissionHandler(private val activity: Activity) {

    /**
     * Constant representing the permission request code.
     */
    companion object {
        const val PERMISSION_REQUEST_CODE = 22101920
    }

    /**
     * Check if the given permissions are granted.
     *
     * @param permissions An array of permission strings to check.
     * @return `true` if all permissions are granted, `false` otherwise.
     */
    fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            // Check if the permission is granted
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Request the specified permissions from the user.
     *
     * @param permissions An array of permission strings to request.
     */
    fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)
    }

    /**
     * Handle the result of the permission request.
     *
     * @param requestCode The request code.
     * @param permissions The array of requested permissions.
     * @param grantResults The array of grant results for each requested permission.
     * @return `true` if all requested permissions are granted, `false` otherwise.
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all requested permissions are granted
            return grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        }
        return false
    }
}
