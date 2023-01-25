package com.example.process

import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.process.databinding.FragmentCustomHandlerBinding
import com.example.process.databinding.FragmentExecutorBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ExecutorFragment : Fragment() {
    private var _binding: FragmentExecutorBinding? = null
    private val binding get() = _binding!!

    private val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    private val updateUi = Runnable {
        updateUi()
    }
    private var timerValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentExecutorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,
            "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")

        timerValue = binding.enterTime.text.toString().toInt()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        executorService.scheduleAtFixedRate({
            if (timerValue > 0) {
                Log.d(TAG, "Launched! ")
                binding.resultTv.post(updateUi)
                timerValue--
            } else {
                executorService.shutdownNow()
            }
        }, 0, 1, TimeUnit.SECONDS)

    }

    private fun updateUi() {
        Log.d(TAG, "Current thread = ${Thread.currentThread().name}")
        Log.d(TAG, "In UI: $timerValue")
        binding.resultTv.text = timerValue.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        executorService.shutdownNow()
        _binding = null
    }

    companion object {
        const val TAG = "HandlerFragLog"

        @JvmStatic
        fun newInstance() =
            HandlerFragment()
    }
}