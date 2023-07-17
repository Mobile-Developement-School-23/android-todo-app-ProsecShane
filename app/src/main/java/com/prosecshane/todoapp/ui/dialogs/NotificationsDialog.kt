package com.prosecshane.todoapp.ui.dialogs

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants.SETTINGS_NOTIFICATIONS
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil

class NotificationsDialog(
    fragment: Fragment,
    onButtonPress: () -> Unit
) {
    private val layout = fragment.layoutInflater.inflate(R.layout.notifications_dialog, null)
    private val dialog = AlertDialog.Builder(fragment.requireContext()).setView(layout).create()
    private val sharedPrefs = SharedPreferencesUtil(fragment.requireContext())

    init {
        val agreeButton = layout.findViewById<MaterialButton>(R.id.notif_yes)
        val denyButton = layout.findViewById<MaterialButton>(R.id.notif_no)

        agreeButton.setOnClickListener {
            sharedPrefs.set(SETTINGS_NOTIFICATIONS, true)
            dialog.hide()
            onButtonPress()
        }
        denyButton.setOnClickListener {
            sharedPrefs.set(SETTINGS_NOTIFICATIONS, false)
            dialog.hide()
            onButtonPress()
        }
    }

    fun show() { dialog.show() }
}
