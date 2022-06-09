package com.example.comicshub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comicshub.data.util.Resource
import com.example.comicshub.databinding.FragmentComicsBinding
import com.example.comicshub.databinding.FragmentSavedComicsBinding
import com.example.comicshub.presentation.adapter.SavedComicsAdapter
import com.example.comicshub.presentation.viewmodel.ComicsViewModel


class SavedComicsFragment : Fragment() {
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

//        viewSavedComicsList()
        initRecyclerView()

    }

    private fun initRecyclerView() {
//        savedComicsAdapter = SavedComicsAdapter()
//        binding.apply {
//            savedComicsRecyclerView.adapter = savedComicsAdapter
//            savedComicsRecyclerView.layoutManager = LinearLayoutManager(activity)
//        }

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