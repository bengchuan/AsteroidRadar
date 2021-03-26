package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteriodFilter

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        // Adding adapter for recyclerview
        val adapter = AsteroidListAdapter(
            AsteriodOnClickListener {
                //On click, we will navigate to Asteriod Details
                Log.i("MAIN", "Navigating to Asteriod Details for ${it.codename}")
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            })
        binding.asteroidRecycler.adapter = adapter
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.getAsteriodsFilteredBy(
            when (item.itemId) {
                R.id.show_all_menu -> AsteriodFilter.ALL
                R.id.show_favorite -> AsteriodFilter.FAVORITE
                R.id.show_today -> AsteriodFilter.TODAY
                else -> AsteriodFilter.ALL
            }
        )
        return true
    }
}
