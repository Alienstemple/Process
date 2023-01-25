package com.example.process

import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.process.databinding.FragmentHandlerCallbackBinding
import java.util.concurrent.TimeUnit

class HandlerCallbackFragment : Fragment() {

    private var _binding: FragmentHandlerCallbackBinding? = null
    private val binding get() = _binding!!

    private val backgroundHandler: Handler
    private val uiHandler =
        Handler(Looper.getMainLooper())  // Основной поток уже запущен. Возьмем от него looper

    private val updateUi = Runnable {
        updateUi()
    }
    private var timerValue: Int = 0

    // Executes after default constructor
    init {
        val backgroundHandlerThread = HandlerThread("background")
        backgroundHandlerThread.start()    // Запустим фоновый поток
        // В конструктор передадим looper запущенного фонового потока и экземпляр TimerCallback
        backgroundHandler =
            Handler(backgroundHandlerThread.looper, TimerCallback())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHandlerCallbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startBtn.setOnClickListener {
            timerValue = binding.enterTime.text.toString().toInt()
            Log.d(TAG, "Before send empty message")
            backgroundHandler.sendEmptyMessage(CALC)  // TODO TimerCallback.calc
        }
    }

    private fun updateUi() {
        Log.d(TAG, "Current thread = ${Thread.currentThread().name}")
        Log.d(TAG, "In UI: $timerValue")
        binding.resultTv.text = timerValue.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backgroundHandler.removeCallbacksAndMessages(null)  // очистим очередь сообщений
        uiHandler.removeCallbacksAndMessages(null)
        _binding = null
    }

    inner class TimerCallback : Handler.Callback {

        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                CALC -> {
                    Log.d(TAG, "Current thread = ${Thread.currentThread().name}")
                    if (timerValue > 0) {
                        Log.d(IncorrectFragment.TAG, "Timer updated: $timerValue")
                        timerValue--   // In UI - 9
                        uiHandler.post(updateUi)  // Отрисуем интерфейс
                        backgroundHandler.sendEmptyMessageDelayed(CALC,
                            TimeUnit.SECONDS.toMillis(1))
                    } else {
//            Looper.myLooper()?.quitSafely()   // Выход из looper, timer заново не запустим, dead thread
                    }
                    return true
                }
            }
            return false
        }
    }

    companion object {
        const val TAG = "HandlerFragLog"
        const val CALC: Int = 0

        @JvmStatic
        fun newInstance() =
            HandlerFragment()
    }
}