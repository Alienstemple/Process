package com.example.process

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.process.databinding.FragmentIncorrectBinding


class IncorrectFragment : Fragment() {

    private var _binding: FragmentIncorrectBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentIncorrectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startBtn.setOnClickListener {
            val time = binding.enterTime.text.toString().toInt()

            for (i in time downTo 0) {
                try {
                    Log.d(TAG, "Timer updated: $i")
                    binding.resultTv.text = i.toString()
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "$e.stackTrace")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "IncorFragLog"
        @JvmStatic
        fun newInstance() =
            IncorrectFragment()
    }
}