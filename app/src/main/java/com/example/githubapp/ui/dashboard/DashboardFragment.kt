package com.example.githubapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.base.BaseFragment
import com.example.githubapp.data.Result
import com.example.githubapp.data.remote.response.ItemsItem
import com.example.githubapp.databinding.FragmentDashboardBinding
import com.example.githubapp.ui.adapter.AccountAdapter
import com.example.githubapp.ui.favorite.ViewModelFactory

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(),
    AccountAdapter.OnItemClickCallback {
    private val dashboardViewModel by viewModels<DashboardViewModel>() {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBarSetting()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvGithub.layoutManager = layoutManager

        dashboardViewModel.findUser.observe(viewLifecycleOwner) { result ->
            result.let {
                when (result) {
                    is Result.Loading -> {
                        showProgressBar(true)
                    }

                    is Result.Success -> {
                        showProgressBar(false)
                        listAccountDataService(result.data.items)
                    }

                    is Result.Error -> {
                        showProgressBar(false)
                    }
                }
            }
        }

        searchBarSetting()
    }

    private fun searchBarSetting() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val searchText = textView.text.toString()
                    searchBar.setText(searchText)

                    val textSearch = searchText.ifEmpty { DashboardViewModel.USERNAME_GITHUB }
                    dashboardViewModel.findAccount(textSearch)

                    searchView.hide()
                    false
                }

            searchBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.favActionButton -> {
                        findNavController().navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToFavoriteFragment()
                        )
                        true
                    }

                    R.id.themeActionButton -> {
                        findNavController().navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToSettingFragment()
                        )
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.pbDashboard.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun listAccountDataService(listAccount: List<ItemsItem>) {
        val adapter = AccountAdapter()
        adapter.setUserData(listAccount)

        binding.rvGithub.adapter = adapter

        adapter.setOnItemClickCallback(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onItemClicked(username: String) {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToDetailFragment(
                username
            )
        )
    }

}