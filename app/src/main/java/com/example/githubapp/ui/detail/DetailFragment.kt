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
import com.example.githubapp.data.Result
import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.databinding.FragmentDetailBinding
import com.example.githubapp.ui.adapter.SectionsPagerAdapter
import com.example.githubapp.ui.favorite.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        detailViewModel.detailUser.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result) {
                    is Result.Loading -> {
                        showProgressBar(true)
                    }

                    is Result.Success -> {
                        showProgressBar(false)
                        setData(result.data)
                    }

                    is Result.Error -> {
                        showProgressBar(false)
                    }
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setData(data: FavoriteEntity) {
        binding.tvUserGithub.text = data.username
        binding.tvUserId.text = data.fullName
        binding.tvTotalFollower.text = getString(R.string.total_followers, data.totalFollower)
        binding.tvTotalFollowing.text = getString(R.string.total_following, data.totalFollowing)
        setIconFavoriteState(data.isFavorited)

        Glide.with(requireContext())
            .load(data.avatarUrl)
            .into(binding.imageView)

        binding.floatingActionButton.setOnClickListener {
            data.isFavorited = !data.isFavorited
            setFavoriteState(data, data.isFavorited)
            setIconFavoriteState(data.isFavorited)
        }
    }

    private fun setFavoriteState(favoriteEntity: FavoriteEntity, status: Boolean) {
        detailViewModel.saveFav(favoriteEntity, status)
    }

    private fun setIconFavoriteState(isFavorite: Boolean) {
        binding.apply {
            val icon =
                if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
            val contentDescription = if (isFavorite) R.string.unfavorite else R.string.favorite
            floatingActionButton.setImageResource(icon)
            floatingActionButton.contentDescription = resources.getString(contentDescription)
        }
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