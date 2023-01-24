package com.example.process

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.process.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        launchFragment(IncorrectFragment())

        mainBinding.incorrectBtn.setOnClickListener {
            launchFragment(IncorrectFragment())
        }

        mainBinding.handlerBtn.setOnClickListener {
            launchFragment(HandlerFragment())
        }

        mainBinding.handlerCallbackBtn.setOnClickListener {
            launchFragment(HandlerCallbackFragment())
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.dialog_frag_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}