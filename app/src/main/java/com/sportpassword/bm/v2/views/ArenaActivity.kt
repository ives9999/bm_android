package com.sportpassword.bm.v2.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sportpassword.bm.databinding.ActivityArenaBinding

class ArenaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArenaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}