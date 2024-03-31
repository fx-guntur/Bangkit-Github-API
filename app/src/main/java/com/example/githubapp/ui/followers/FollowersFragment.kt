package com.example.githubapp.ui.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.base.BaseFragment
import com.example.githubapp.data.Result
import com.example.githubapp.data.remote.response.ItemsItem
import com.example.githubapp.databinding.FragmentFollowersBinding
import com.example.githubapp.ui.adapter.AccountAdapter
import com.example.githubapp.ui.favorite.ViewModelFactory

class FollowersFragment : BaseFragment<FragmentFollowersBinding>() {
    private val followersViewModel by viewModels<FollowersViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var position: Int? = null
    private var username: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowers.layoutManager = layoutManager


        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            setFollower(savedInstanceState)
        } else {
            setFollowing(savedInstanceState)
        }
    }

    private fun setFollower(savedInstanceState: Bundle?) {
        username.takeIf { savedInstanceState == null }?.let { followersViewModel.findFollower(it) }
        followersViewModel.listFollower.observe(viewLifecycleOwner) { result ->
            result.let {
                when (result) {
                    is Result.Loading -> {
                        showProgressBar(true)
                    }

                    is Result.Success -> {
                        showProgressBar(false)
                        setAdapter(result.data)
                    }

                    is Result.Error -> {
                        showProgressBar(false)
                    }
                }
            }
        }
    }

    private fun setFollowing(savedInstanceState: Bundle?) {
        username.takeIf { savedInstanceState == null }?.let { followersViewModel.findFollowing(it) }
        followersViewModel.listFollowing.observe(viewLifecycleOwner) { result ->
            result.let {
                when (result) {
                    is Result.Loading -> {
                        showProgressBar(true)
                    }

                    is Result.Success -> {
                        showProgressBar(false)
                        setAdapter(result.data)
                    }

                    is Result.Error -> {
                        showProgressBar(false)
                    }
                }
            }
        }
    }

    private fun setAdapter(listAccount: List<ItemsItem>) {
        val adapter = AccountAdapter(false)
        adapter.setUserData(listAccount)
        binding.rvFollowers.adapter = adapter
    }

    private fun setNoFollowers(tabStatus: String) {
        binding.apply {
            tvNoFollower.apply {
                visibility = View.VISIBLE
                text = getString(R.string.noFollower, tabStatus)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFollowersBinding = FragmentFollowersBinding.inflate(inflater, container, false)

    private fun showProgressBar(isLoading: Boolean) {
        binding.pbDashboard.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

}