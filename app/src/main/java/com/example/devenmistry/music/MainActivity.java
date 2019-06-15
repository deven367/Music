package com.example.devenmistry.music;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

//        android.resource://com.example.devenmistry.music/2131427328 : The audio file in the raw folder

public class MainActivity extends AppCompatActivity
{
    MediaPlayer mysong, myPlayer;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor prefsEditor;
    SeekBar seek;
    Handler h;
    Runnable rr;
    String custURL = "";
    ImageView playIcon;
    ImageButton folder;
    AudioTrack audio;
    ImageButton ib;
    Uri audiouri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (custURL != "") {
            audiouri = Uri.parse(custURL);
            Log.i("mediayolo3", custURL);
        }


        sharedPrefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        custURL = sharedPrefs.getString("song", "");

//        mysong = MediaPlayer.create(MainActivity.this,R.raw.audio);
//        mysong.setAudioStreamType(AudioManager.STREAM_MUSIC);

//        myPlayer = new MediaPlayer();

        if (audiouri != null) {
            myPlayer = MediaPlayer.create(MainActivity.this, audiouri);
            myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        playIcon = findViewById(R.id.playIcon);
        folder = findViewById(R.id.folder);
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("audio/*");
                startActivityForResult(i, 1);

                Log.i("mediayolo2", custURL);

//                myPlayer.prepareAsync();
            }
        });



//        myPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
//            @Override
//            public void onPrepared(MediaPlayer playerM){
//                playerM.start();
//            }
//        });


/*
        myPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                myPlayer.start();
            }
        });

        myPlayer.prepareAsync();
*/

//        audiouri = Uri.parse("android.resource://"+getPackageName()+"/raw/audio.mp3");

        h = new Handler();
        seek = (SeekBar)findViewById(R.id.seekBar);

/*
        seek.setMax(myPlayer.getDuration());

        myPlayer.seekTo(0);
*/


        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
//                    myPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Null", audiouri.toString());
//                seek.refreshDrawableState();
                if (audiouri != null) {
                    myPlayer = MediaPlayer.create(MainActivity.this, audiouri);
                    myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    myPlayer.start();
                    playCycle();
                    ppz();
                }

            }
        });

    }
    public void ppz(){
//        playIcon = findViewById(R.id.playIcon);
//        playIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

                if(myPlayer.isPlaying())
                {
                    myPlayer.pause();
                    playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    myPlayer.start();
                    playIcon.setImageResource(R.drawable.ic_pause_black_24dp);
                }

//            }
//        });
    }
    public void playCycle()
    {
        seek.setProgress(myPlayer.getCurrentPosition());

        if (myPlayer.isPlaying())
        {
            rr = new Runnable(){
                @Override
                public void run() {
                    playCycle();
                }
            };
            h.postDelayed(rr,1000);
        }
    }

    public void playIT(View v)
    {
        if(myPlayer.isPlaying())
        {
            myPlayer.pause();
        } else {
            playCycle();
            myPlayer.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        myPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        myPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        myPlayer.release();
//        h.removeCallbacks(rr);
    }

    public void ppause(View view)
    {
        myPlayer.pause();
    }

    public void rewind30(View view)
    {

    }

    public void previousTrack(View view)
    {

    }

//    Folder button on Main Screen
    public void folderview( ) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)) {

                audiouri = data.getData(); //declared above Uri audio;
                Log.i("mediayolo", "onActivityResult: " + audiouri);

                custURL = audiouri.toString();

                prefsEditor = sharedPrefs.edit();
                prefsEditor.putString("song", custURL);
                prefsEditor.apply();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
