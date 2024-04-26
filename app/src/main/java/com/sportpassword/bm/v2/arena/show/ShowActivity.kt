package com.sportpassword.bm.v2.arena.show

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.ActivityArenaShowBinding

class ShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArenaShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArenaShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.top.apply {
            setTitle("球館")
            showPrev(true)
        }
    }
}