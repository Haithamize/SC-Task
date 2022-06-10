package com.example.comicshub

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.databinding.FragmentComicsBinding
import com.example.comicshub.presentation.adapter.SavedComicsAdapter
import com.example.comicshub.presentation.viewmodel.ComicsViewModel
import kotlin.properties.Delegates


const val TAG = "ComicsFragment"
const val COMIC_DATA = "COMIC_DATA"
class ComicsFragment : Fragment() {

    private lateinit var viewModel : ComicsViewModel
    private lateinit var binding : FragmentComicsBinding
    private lateinit var comicData : APIResponse
    private var selectedComicNumber : Int? = 1


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

        viewModel.getRandomListOfComics()

        viewComicData(200)

//        val args : ComicsFragmentArgs by navArgs()
//        val comic = args.savedComic

        binding.apply {
            btnGetExplanation.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable(COMIC_DATA,comicData)
                }
                findNavController().navigate(R.id.action_comicsFragment_to_explainationFragment, bundle)
            }
            btnRandom.setOnClickListener {
                selectedComicNumber = (1.. 2500). random()
                viewComicData(selectedComicNumber)
            }
            btnNext.setOnClickListener {
                selectedComicNumber = selectedComicNumber?.plus(1)
                viewComicData(selectedComicNumber)
            }
            btnPrev.setOnClickListener {
                selectedComicNumber = selectedComicNumber?.minus(1)
                viewComicData(selectedComicNumber)
            }
            btnFirst.setOnClickListener {
                selectedComicNumber = 1
                viewComicData(selectedComicNumber)
            }
            btnLast.setOnClickListener {
                selectedComicNumber = null
                viewComicData(selectedComicNumber)
            }
        }
    }

    private fun viewComicData(comicNumber : Int?) {
        if(comicNumber == null){
//            Log.d("List of", "${viewModel.incomingComicsList}")
            viewModel.getNewestComic()
            viewModel.comicDataPublic.observe(viewLifecycleOwner, Observer {response->
                selectedComicNumber = response.data?.num
                Log.d(TAG, "${response.data}")
                when(response){
                    is Resource.Success -> {
//                     hideProgressBar()
                        binding.apply {
                            response.data?.let {apiResponse ->
                                comicData = apiResponse
                                comicTitle.text = apiResponse.title
                                comicTranscript.text = apiResponse.transcript
                            }
                            Glide.with(comicImgView.context)
                                .load(response.data?.img)

                                .listener(object : RequestListener<Drawable> {
                                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(comicImgView)
                        }

                    }
                    is Resource.Error -> {
//                    hideProgressBar()
                        response.message?.let{
                            Toast.makeText(activity, "Unexpected error : $it", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading -> {
//                    showProgressBar()
                    }
                }
            })
        }else{
            viewModel.getSelectedComic(comicNumber)
            viewModel.comicDataPublic.observe(viewLifecycleOwner, Observer {response->
                Log.d(TAG, "${response.data}")
                when(response){
                    is Resource.Success -> {
//                     hideProgressBar()
                        binding.apply {
                            response.data?.let {apiResponse ->
                                comicData = apiResponse
                                comicTitle.text = apiResponse.title
                                comicTranscript.text = apiResponse.transcript
                            }
                            Glide.with(comicImgView.context)
                                .load(response.data?.img)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }
                                })
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
    }



//    private fun showProgressBar(){
//        binding.progressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar(){
//        binding.progressBar.visibility = View.INVISIBLE
//    }
}