package com.example.onlineradio

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentStreamUrl: String? = null

    private val hitFmUrl = "http://icecast.hitfm.ua/hitfm128.mp3"
    private val europaPlusUrl = "http://ep128-icecast.tavrmedia.ua/ep128.mp3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHitFm = findViewById<Button>(R.id.btnHitFm)
        val btnEuropaPlus = findViewById<Button>(R.id.btnEuropaPlus)
        val btnPlayPause = findViewById<Button>(R.id.btnPlayPause)

        btnHitFm.setOnClickListener {
            startStream(hitFmUrl)
        }

        btnEuropaPlus.setOnClickListener {
            startStream(europaPlusUrl)
        }

        btnPlayPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                btnPlayPause.text = "Play"
            } else {
                mediaPlayer?.start()
                btnPlayPause.text = "Pause"
            }
        }
    }

    private fun startStream(url: String) {
        if (currentStreamUrl == url && mediaPlayer?.isPlaying == true) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                start()
            }
            setOnErrorListener { _, _, _ ->
                // handle error if needed
                true
            }
        }
        currentStreamUrl = url

        val btnPlayPause = findViewById<Button>(R.id.btnPlayPause)
        btnPlayPause.text = "Pause"
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}