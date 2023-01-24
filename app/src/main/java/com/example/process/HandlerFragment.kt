package com.example.process

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.process.databinding.FragmentHandlerBinding
import com.example.process.databinding.FragmentIncorrectBinding
import java.util.concurrent.TimeUnit

class HandlerFragment : Fragment() {

    private var _binding: FragmentHandlerBinding? = null
    private val binding get() = _binding!!

    private val backgroundHandler: Handler
    private val uiHandler = Handler(Looper.getMainLooper())  // Основной поток уже запущен. Возьмем от него looper

    private val calculateTimer = Runnable {
        calculateTimer()
    }
    private val updateUi = Runnable {
        updateUi()
    }
    private var timerValue: Int = 0

    // Executes after default constructor
    init {
        val backgroundHandlerThread = HandlerThread("background")
        backgroundHandlerThread.start()    // Запустим фоновый поток
        backgroundHandler =
            Handler(backgroundHandlerThread.looper)   // В конструктор передадим looper запущенного фонового потока
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHandlerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startBtn.setOnClickListener {
            timerValue = binding.enterTime.text.toString().toInt()
            backgroundHandler.post(calculateTimer)
        }
    }

    private fun calculateTimer() {
        if (timerValue > 0) {
            Log.d(IncorrectFragment.TAG, "Timer updated: $timerValue")
            timerValue--
            uiHandler.post(updateUi)  // Отрисуем интерфейс
            backgroundHandler.postDelayed(calculateTimer, TimeUnit.SECONDS.toMillis(1))
        }
    }

    private fun updateUi() {
        Log.d(TAG, "In UI: $timerValue")
        binding.resultTv.text = timerValue.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HandlerFragLog"

        @JvmStatic
        fun newInstance() =
            HandlerFragment()
    }
}