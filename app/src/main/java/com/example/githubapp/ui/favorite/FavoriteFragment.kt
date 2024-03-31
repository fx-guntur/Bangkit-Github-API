package com.example.githubapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.base.BaseFragment
import com.example.githubapp.data.remote.response.ItemsItem
import com.example.githubapp.databinding.FragmentFavoriteBinding
import com.example.githubapp.ui.adapter.AccountAdapter

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(),
    AccountAdapter.OnItemClickCallback {
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.layoutManager = layoutManager

        favoriteViewModel.getFavoriteAccount()
        favoriteViewModel.favoriteUser.observe(viewLifecycleOwner) { listFavoriteUser ->
            val listUsers = listFavoriteUser.map {
                ItemsItem(
                    login = it.username,
                    avatarUrl = it.avatarUrl
                )
            }
            listAccountDataService(listUsers)
        }
    }

    private fun listAccountDataService(listAccount: List<ItemsItem>) {
        val adapter = AccountAdapter()
        adapter.setUserData(listAccount)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(this)
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                title = "Favorite List"
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
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun onItemClicked(username: String) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                username
            )
        )
    }


}