package com.example.githubapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.base.BaseFragment
import com.example.githubapp.data.response.DetailUserResponse
import com.example.githubapp.databinding.FragmentDetailBinding
import com.example.githubapp.ui.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private val detailViewModel by viewModels<DetailViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        val safeArgs = arguments?.let { DetailFragmentArgs.fromBundle(it) }
        val username: String = safeArgs?.githubUsername ?: ""

        showData(username, savedInstanceState)
        setViewPager(username)
    }

    private fun showData(username: String, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) detailViewModel.findDetail(username)
        detailViewModel.detailUser.observe(viewLifecycleOwner, ::setData)
        detailViewModel.isLoading.observe(viewLifecycleOwner, ::showProgressBar)
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setData(data: DetailUserResponse) {
        binding.tvUserGithub.text = data.name
        binding.tvUserId.text = data.login
        binding.tvTotalFollower.text = getString(R.string.total_followers, data.followers)
        binding.tvTotalFollowing.text = getString(R.string.total_following, data.following)

        Glide.with(requireContext())
            .load(data.avatarUrl)
            .into(binding.imageView)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false)

    private fun setViewPager(username: String) {
        binding.apply {
            val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
            sectionsPagerAdapter.username = username
            vpFollow.adapter = sectionsPagerAdapter
            TabLayoutMediator(tlFollower, vpFollow) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                title = "Account Detail"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}