package com.example.githubapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.base.BaseFragment
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.databinding.FragmentDashboardBinding
import com.example.githubapp.ui.adapter.AccountAdapter

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(),
    AccountAdapter.OnItemClickCallback {
    private val dashboardViewModel by viewModels<DashboardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBarSetting()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvGithub.layoutManager = layoutManager

        dashboardViewModel.findUser.observe(viewLifecycleOwner) {
            if (it.totalCount == 0) {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            }
        }

        dashboardViewModel.isLoading.observe(viewLifecycleOwner, ::showProgressBar)
        dashboardViewModel.listAccount.observe(viewLifecycleOwner, ::listAccountDataService)
        searchBarSetting()
    }

    fun searchBarSetting() {
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