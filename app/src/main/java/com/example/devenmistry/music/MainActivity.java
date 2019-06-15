package com.example.devenmistry.music;

import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity
{
    MediaPlayer mysong;
    SeekBar seek;
    Handler h;
    Runnable rr;
    ImageView playIcon;
    AudioTrack audio;
    ImageButton ib;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mysong = MediaPlayer.create(MainActivity.this,R.raw.audio);
        mysong.setAudioStreamType(AudioManager.STREAM_MUSIC);



        h = new Handler();
        seek = (SeekBar)findViewById(R.id.seekBar);

        seek.setMax(mysong.getDuration());


        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mysong.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






    }
    public void ppz(View v){
        playIcon = findViewById(R.id.playIcon);
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mysong.isPlaying())
                {
                    mysong.pause();
                    playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    mysong.start();
                    playIcon.setImageResource(R.drawable.ic_pause_black_24dp);
                }

            }
        });
    }
    public void playCycle()
    {
        seek.setProgress(mysong.getCurrentPosition());

        if (mysong.isPlaying())
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
        if(mysong.isPlaying())
        {
            mysong.pause();
        } else {
//            playCycle();
            mysong.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mysong.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mysong.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mysong.release();
        h.removeCallbacks(rr);
    }

    public void ppause(View view)
    {
        mysong.pause();
    }

    public void previousTrack(View view)
    {

    }

    public void folderview(View v) {
    ib = findViewById(R.id.folder);
    ib.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = getIntent();
            

        }
    });
}

}
