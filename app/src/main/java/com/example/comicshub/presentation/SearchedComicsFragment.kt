package com.example.comicshub

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.databinding.FragmentComicsBinding
import com.example.comicshub.databinding.FragmentSavedComicsBinding
import com.example.comicshub.databinding.FragmentSearchedComicsBinding
import com.example.comicshub.presentation.adapter.SavedComicsAdapter
import com.example.comicshub.presentation.viewmodel.ComicsViewModel

const val SEARCHED_COMIC_DATA = "SEARCHED_COMIC_DATA"

class SearchedComicsFragment : Fragment(), SavedComicsAdapter.AdapterOnItemClickListener {
    private lateinit var viewModel : ComicsViewModel
    private lateinit var binding : FragmentSearchedComicsBinding
    private lateinit var listOfSearchedComics : ArrayList<APIResponse>
    private lateinit var savedComicsAdapter: SavedComicsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searched_comics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchedComicsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel


        arguments?.let {
            arguments?.getSerializable(SEARCHED_LIST)?.let { arg1->
                listOfSearchedComics= arg1 as ArrayList<APIResponse>
            }

        }

//        viewSavedComicsList()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        savedComicsAdapter = SavedComicsAdapter(listOfSearchedComics, this)
        binding.apply {
            searchedComicsRecyclerView.adapter = savedComicsAdapter
            searchedComicsRecyclerView.layoutManager = LinearLayoutManager(activity)
        }

    }

    override fun onItemClicked(apiResponse: APIResponse, position: Int, view: View) {
        view.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(SEARCHED_COMIC_DATA, apiResponse)
            }
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putBoolean(FIRST_TIME_IN, false)
                this?.apply()
            }
            findNavController().navigate(R.id.action_searchedComicsFragment_to_comicsFragment,bundle)
        }
    }

//    private fun viewSavedComicsList() {
//        viewModel.getNewestComic()
//        viewModel.newestComicPublic.observe(viewLifecycleOwner, Observer {
//            when(it){
//                is Resource.Success -> {
//                    hideProgressBar()
//                    it.data?.let {
//                        n
//                    }
//                }
//                is Resource.Error -> {
//
//                }
//                is Resource.Loading -> {
//
//                }
//            }
//        })
//    }


}