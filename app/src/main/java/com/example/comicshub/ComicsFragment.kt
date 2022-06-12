package com.example.comicshub

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.databinding.FragmentComicsBinding
import com.example.comicshub.presentation.viewmodel.ComicsViewModel
import com.example.comicshub.presentation.viewmodel.NEWEST_COMIC_NUMBER
import com.example.comicshub.workmanager.NotificationWorker
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


const val TAG = "ComicsFragment"
const val COMIC_DATA = "COMIC_DATA"
const val SEARCHED_LIST = "SEARCHED_LIST"

class ComicsFragment : Fragment() {

    private lateinit var viewModel: ComicsViewModel
    private lateinit var binding: FragmentComicsBinding
    private lateinit var comicData: APIResponse
    private var selectedComicNumber: Int? = 1
    private lateinit var listOfSearchedComics: ArrayList<APIResponse>


    private val comicListAfterSearchWithText = ArrayList<APIResponse>()
    private lateinit var sharedPref: SharedPreferences
    private var firstTimeIn by Delegates.notNull<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comics, container, false)
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            searchView.setQuery("", false)
            searchView.clearFocus()
        }
        firstTimeIn = sharedPref.getBoolean(FIRST_TIME_IN, true)
        if (!firstTimeIn) {
            arguments?.let {
                when {
                    requireArguments().containsKey(SEARCHED_COMIC_DATA) -> {
                        arguments?.getSerializable(SEARCHED_COMIC_DATA)?.let { arg1 ->
                            val comic = arg1 as APIResponse
                            comicData = comic
                            viewComicData(comic.num)
                        }
                    }
                    requireArguments().containsKey(SAVED_COMIC_DATA) -> {
                        arguments?.getSerializable(SAVED_COMIC_DATA)?.let { arg1 ->
                            val comic = arg1 as APIResponse
                            comicData = comic
                            viewComicData(comic.num)
                        }
                    }
                    else -> return
                }
            }
        } else {
            viewComicData(200)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(sharedPref.edit()) {
            putBoolean(FIRST_TIME_IN, true)
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComicsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        firstTimeIn = sharedPref.getBoolean(FIRST_TIME_IN, false)

        viewModel.getRandomListOfComics()

        getNewestComicNumberAndSaveIt()


        Handler().postDelayed({
            setPeriodicWorkRequest()
        }, 1000)



        if (!firstTimeIn) {
            arguments?.let {
                when {
                    requireArguments().containsKey(SEARCHED_COMIC_DATA) -> {
                        arguments?.getSerializable(SEARCHED_COMIC_DATA)?.let { arg1 ->
                            val comic = arg1 as APIResponse
                            comicData = comic
                            viewComicData(comic.num)
                        }
                    }
                    requireArguments().containsKey(SAVED_COMIC_DATA) -> {
                        arguments?.getSerializable(SAVED_COMIC_DATA)?.let { arg1 ->
                            val comic = arg1 as APIResponse
                            comicData = comic
                            viewComicData(comic.num)
                        }
                    }
                    else -> return
                }
            }
        } else {
            viewComicData(200)
        }


        setSearchView()

        binding.apply {
            fab.setOnClickListener {
                viewModel.saveComicDataToDatabase(comicData)
                Snackbar.make(view, "Comic saved successfully", Snackbar.LENGTH_LONG).show()
            }
            btnGetExplanation.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable(COMIC_DATA, comicData)
                }
                findNavController().navigate(
                    R.id.action_comicsFragment_to_explainationFragment,
                    bundle
                )
            }
            btnRandom.setOnClickListener {
                selectedComicNumber = (1..2500).random()
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



    //creating the periodic workmanager
    private fun setPeriodicWorkRequest() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lastCheckedNewestNumber = sharedPref.getInt(NEWEST_COMIC_NUMBER,0)

        val data = Data.Builder()
            .putInt(NEWEST_COMIC_NUMBER,lastCheckedNewestNumber)
            .build()
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workManager = WorkManager.getInstance(requireContext())
        val periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java,16, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .setInputData(data)
            .build()
        workManager.enqueue(periodicWorkRequest)
    }


    private fun getNewestComicNumberAndSaveIt() {
        viewModel.saveNewestComicNumber(requireActivity())
    }

    private fun viewComicData(comicNumber: Int?) {
        if (comicNumber == null) {
//            Log.d("List of", "${viewModel.incomingComicsList}")
            viewModel.getNewestComic()
            viewModel.comicDataPublic.observe(viewLifecycleOwner, Observer { response ->
                selectedComicNumber = response.data?.num
//                Log.d(TAG, "${response.data}")
                when (response) {
                    is Resource.Success -> {
//                     hideProgressBar()
                        binding.apply {
                            response.data?.let { apiResponse ->
                                comicData = apiResponse
                                comicTitle.text = apiResponse.title
                                comicTranscript.text = apiResponse.transcript
                            }
                            Glide.with(comicImgView.context)
                                .load(response.data?.img)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(comicImgView)
                        }

                    }
                    is Resource.Error -> {
//                    hideProgressBar()
                        response.message?.let {
                            Toast.makeText(activity, "Unexpected error : $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    is Resource.Loading -> {
//                    showProgressBar()
                    }
                }
            })
        } else {
            viewModel.getSelectedComic(comicNumber)
            viewModel.comicDataPublic.observe(viewLifecycleOwner, Observer { response ->
//                Log.d(TAG, "${response.data}")
                when (response) {
                    is Resource.Success -> {
//                     hideProgressBar()
                        binding.apply {
                            response.data?.let { apiResponse ->
                                comicData = apiResponse
                                comicTitle.text = apiResponse.title
                                comicTranscript.text = apiResponse.transcript
                            }
                            Glide.with(comicImgView.context)
                                .load(response.data?.img)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        progressBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(comicImgView)
                        }

                    }
                    is Resource.Error -> {
//                    hideProgressBar()
                        response.message?.let {
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


    private fun setSearchView() {
        binding.apply {
            searchView.queryHint = "Search for your favorite comic"
        }
        comicListAfterSearchWithText.clear()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val bundle = Bundle().apply {
                    val list = viewModel.incomingComicsList
                    Log.d(TAG, list.toString())

                    putSerializable(
                        SEARCHED_LIST,
                        getSearchedComicsListByText(query.toString(), list)
                    )
                }
                findNavController().navigate(
                    R.id.action_comicsFragment_to_searchedComicsFragment,
                    bundle
                )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        binding.searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                return false
            }

        })
    }


    private fun getSearchedComicsListByText(
        searchQuery: String,
        list: ArrayList<APIResponse>
    ): ArrayList<APIResponse> {
        val distinct = list?.distinctBy {
            it?.title
        }
        if (distinct != null) {
            for (comic in distinct) {
                if (comic.title?.lowercase(Locale.getDefault())?.contains(searchQuery) == true) {
                    comicListAfterSearchWithText.add(comic)
                }
            }
        }

        return comicListAfterSearchWithText
    }


//    private fun showProgressBar(){
//        binding.progressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar(){
//        binding.progressBar.visibility = View.INVISIBLE
//    }
}