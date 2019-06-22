package com.example.devenmistry.music;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

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
    String custURL = "", filepath, path;
    ImageView playIcon;
    ImageButton folder;
    AudioTrack audio;
    ImageButton ib;
    Uri audiouri;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

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
        } else if(audiouri == null && custURL != "") {
            audiouri = Uri.parse(custURL);
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
                Log.i("mediayolo2.5", audiouri.toString());

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

/*
        h = new Handler();
        seek = (SeekBar)findViewById(R.id.seekBar);
*/

/*
        seek.setMax(myPlayer.getDuration());

        myPlayer.seekTo(0);
*/


/*
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
*/


        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("Null", audiouri.toString());
//                seek.refreshDrawableState();
                if (audiouri != null) {
                    myPlayer = MediaPlayer.create(MainActivity.this, audiouri);
//                    myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    path = Environment.getExternalStorageDirectory()+"/"+getRealPathFromURI(audiouri);
                    Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
//                    if (path == null) {
//                        Log.i("NullPath", "path is null");
//                    } else {
//                        Log.i("IT IS NULL", "IT IS NULL");
//                    }

                    try {
                        myPlayer.reset();
                        myPlayer.setDataSource(path);
                        myPlayer.prepareAsync();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        Log.e("TAG", Log.getStackTraceString(e));
                    } catch (Exception e) {
                        System.out.println("Exception of type : " + e.toString());
                        e.printStackTrace();
                        Log.e("TAG", Log.getStackTraceString(e));
                    }

                    myPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            Log.i("MBC","Media Buffering Completed!");
                            myPlayer.start();
                        }
                    });

//                    myPlayer.start();
//                    playCycle();
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

    private String getRealPathFromURI(Uri contentUri) {
        filepath = "";

        File file = new File(audiouri.getPath());//create path from uri
        final String[] split = file.getPath().split(":");//split the path.
        filepath = split[1];//assign it to a string(your choice).

//        Toast.makeText(getApplicationContext(), filepath, Toast.LENGTH_LONG).show();

        return filepath;
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