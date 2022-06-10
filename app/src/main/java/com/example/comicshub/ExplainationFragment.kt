package com.example.comicshub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.databinding.FragmentExplainationBinding

class ExplainationFragment : Fragment() {

    private lateinit var binding : FragmentExplainationBinding
    private lateinit var comic : APIResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explaination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExplainationBinding.bind(view)

//        val args : ExplainationFragmentArgs by navArgs()
//        val comic = args.comicExplanation

        arguments?.let {
            arguments?.getSerializable(COMIC_DATA)?.let { arg1->
                comic= arg1 as APIResponse
            }

        }
        binding.webview.apply {
            webViewClient = WebViewClient()
            loadUrl("https://www.explainxkcd.com/wiki/index.php/${comic.num}")
        }
    }
}