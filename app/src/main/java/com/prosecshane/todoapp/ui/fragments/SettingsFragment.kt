package com.prosecshane.todoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.button.MaterialButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants.SETTINGS_THEME
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil
import com.prosecshane.todoapp.ui.dialogs.NotificationsDialog
import com.prosecshane.todoapp.util.notificationsEnabled
import com.prosecshane.todoapp.util.updateTheme

// Fragment, that contains settings
class SettingsFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var sharedPrefs: SharedPreferencesUtil

    // Navigate to the previous Fragment
    private fun navigateTo(id: Int) {
        val navController = findNavController()
        navController.navigate(id)
    }

    // Setup Transition
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    // Setup sharedPrefs and get the rootView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        sharedPrefs = SharedPreferencesUtil(requireContext())
        return rootView
    }

    // Setup on Resume
    override fun onResume() {
        super.onResume()
        setup()
    }

    // Setup all Intractable Elements
    private fun setup() {
        setupBackButton()
        setupAppThemeGroup()
        setupNotificationsState()
        setupNotificationsAsker()
    }

    // Setup the Back Button
    private fun setupBackButton() {
        val button = rootView.findViewById<ImageButton>(R.id.settings_exit)
        button.setOnClickListener { navigateTo(R.id.action_fragset_to_fraglist) }
    }

    // Setup App Theme Radio Group of Radio Buttons
    private fun setupAppThemeGroup() {
        val currentTheme = sharedPrefs.get(SETTINGS_THEME, 2)
        val themeGroup = rootView.findViewById<RadioGroup>(R.id.settings_theme_group)
        for ((index, child) in themeGroup.children.withIndex()) {
            if (child is RadioButton) {
                child.isChecked = (currentTheme == index)
                child.setOnClickListener {
                    sharedPrefs.set(SETTINGS_THEME, index)
                    updateTheme(requireContext())
                }
            }
        }
    }

    // Setup Notification State Text
    private fun setupNotificationsState() {
        val notifState = rootView.findViewById<TextView>(R.id.settings_notif_state)
        notifState.text = getString(
            R.string.notif_state,
            if (notificationsEnabled(requireContext())) "Allowed" else "Denied"
        )
    }

    // Setup Notification Asker
    private fun setupNotificationsAsker() {
        val button = rootView.findViewById<MaterialButton>(R.id.settings_notif_ask)
        button.setOnClickListener { NotificationsDialog(this) {
            setupNotificationsState()
        }.show() }
    }
}
