package com.example.githubapp.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.githubapp.base.BaseFragment
import com.example.githubapp.databinding.FragmentSettingBinding
import com.example.githubapp.ui.favorite.ViewModelFactory

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private val settingViewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        settingViewModel.getThemeSettings()
            .observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                binding.switchTheme.isChecked = isDarkModeActive
            }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                title = "Setting"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false)

}