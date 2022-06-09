package com.example.comicshub

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.comicshub.data.util.Resource
import com.example.comicshub.databinding.FragmentComicsBinding
import com.example.comicshub.presentation.viewmodel.ComicsViewModel


const val TAG = "ComicsFragment"
class ComicsFragment : Fragment() {

    private lateinit var viewModel : ComicsViewModel
    private lateinit var binding : FragmentComicsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComicsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        viewComicData(200)
    }

    private fun viewComicData(comicNumber : Int) {
        viewModel.getNewestComic(comicNumber)
        viewModel.newestComicPublic.observe(viewLifecycleOwner, Observer {response->
            Log.d(TAG, "${response.data}")
            when(response){
                 is Resource.Success -> {
//                     hideProgressBar()
                     binding.apply {
                         response.data?.let {apiResponse ->
                             comicTitle.text = apiResponse.title
                             comicTranscript.text = apiResponse.transcript
                         }
                         Glide.with(comicImgView.context)
                             .load(response.data?.img)
                             .into(comicImgView)
                     }

                 }
                is Resource.Error -> {
//                    hideProgressBar()
                    response.message?.let{
                        Toast.makeText(activity, "Unexpected error : $it", Toast.LENGTH_SHORT)
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        })
    }

//    private fun showProgressBar(){
//        binding.progressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar(){
//        binding.progressBar.visibility = View.INVISIBLE
//    }
}