package com.example.reproductor

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    //abrir fichero donde tenemos los archivo mp3
    val fd by lazy {
        assets.openFd(songActual);

    }

    //funcion declarada perezosa la cual llama la clase media player que reproducira el audio
    val mp by lazy {
        val m = MediaPlayer()
        m.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length

        )
        fd.close()
        m.prepare()
        m
    }


    val controllers by lazy {
        listOf(R.id.back , R.id.play,R.id.next).map {
            findViewById<MaterialButton>(it)
        }
    }


    object ci {
        val back = 0
        val play = 1
        val next = 2

    }

    val nameSong by lazy {
        findViewById<TextView>(R.id.namesong)
    }


    val listSong by lazy{
        val songs = assets.list("")?.toList() ?: listOf();
        songs.filter { it.contains(".mp3") }
    }

    var songActualIndex = 0
    set(value) {
        var v = if(value == -1){
            listSong.size-1
        }else{
            value%listSong.size
        }
            field = v
            songActual= listSong[v]
    }

    lateinit var songActual : String;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        controllers[ci.play].setOnClickListener(this::playclicked)
        controllers[ci.next].setOnClickListener(this::nextClicked)
        controllers[ci.back].setOnClickListener(this::backClicked)
        songActual = listSong[songActualIndex]

        //controllers[ci.stop].setOnClickListener(this::stopCliked)
        nameSong.text = songActual


    }

    fun backClicked(v:  View){
        songActualIndex--
        refreshSong()
    }
    fun nextClicked(v : View){
        songActualIndex++
        refreshSong()
    }
    fun playclicked(v: View) {
        if (!mp.isPlaying) {
            mp.start()
            controllers[ci.play].setIconResource(R.drawable.ic_baseline_pause_24)
            nameSong.visibility = View.VISIBLE
        } else {
            mp.pause()
            controllers[ci.play].setIconResource(R.drawable.ic_baseline_play_arrow_24)

        }

    }


    fun refreshSong(){
        mp.reset()
       val fd = assets.openFd(songActual)
        mp.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length

        )
        mp.prepare()
        playclicked(controllers[ci.play])
        nameSong.text= songActual


    }

}
    //fun stopCliked(v: View){
    //    if(mp.isPlaying){
           // mp.pause()
  //          controllers[ci.play].setIconResource(R.drawable.ic_baseline_play_arrow_24)
//
  //      }
//        mp.seekTo(0)

///    }
//}