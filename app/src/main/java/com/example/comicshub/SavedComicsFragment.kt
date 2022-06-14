package com.example.comicshub

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.databinding.FragmentComicsBinding
import com.example.comicshub.databinding.FragmentSavedComicsBinding
import com.example.comicshub.presentation.adapter.SavedComicsAdapter
import com.example.comicshub.presentation.viewmodel.ComicsViewModel

const val SAVED_COMIC_DATA = "SAVED_COMIC_DATA"

class SavedComicsFragment : Fragment(), SavedComicsAdapter.AdapterOnItemClickListener {
    private lateinit var viewModel : ComicsViewModel
    private lateinit var binding : FragmentSavedComicsBinding
    private lateinit var savedComicsAdapter: SavedComicsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_comics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedComicsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        viewModel.getAllSavedComics().observe(viewLifecycleOwner, Observer {

            initRecyclerView(it)

        })

    }

    private fun initRecyclerView(savedComics: List<APIResponse>) {
        savedComicsAdapter = SavedComicsAdapter(savedComics,this)
        binding.apply {
            savedComicsRecyclerView.adapter = savedComicsAdapter
            savedComicsRecyclerView.layoutManager = LinearLayoutManager(activity)
            savedComicsRecyclerView.setHasFixedSize(true)
        }

    }

    override fun onItemClicked(apiResponse: APIResponse, position: Int, view: View) {
        view.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(SAVED_COMIC_DATA, apiResponse)
            }
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putBoolean(FIRST_TIME_IN, false)
                this?.apply()
            }
            findNavController().navigate(R.id.action_savedComicsFragment_to_comicsFragment,bundle)
        }
    }

}