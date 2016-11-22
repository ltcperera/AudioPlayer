/*
 * Copyright (C) 2012 Leander Perera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epllix.audioplayer;

import com.epllix.audioplayer.AudioService.AudioServiceBinder;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.database.Cursor;
import java.io.IOException;
import android.content.Intent;
import android.util.Log;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.content.BroadcastReceiver;

/**
 * Activity class that provides the main audio player screen from where the
 * user can interact with the application.
 */
public class AudioPlayerActivity extends Activity implements OnPreparedListener, OnClickListener
{
    private AudioService m_audioService;
    boolean m_audioServiceBound = false;

    Button m_ejectButton;
    Button m_rewindButton;
    Button m_playPauseButton;
    Button m_forwardButton;
    Button m_loopButton;

    ImageView m_albumArtImage;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer);

        // Find and assign all the button views
        m_ejectButton = (Button) findViewById(R.id.btnEject);
        m_rewindButton = (Button) findViewById(R.id.btnRewind);
        m_playPauseButton = (Button) findViewById(R.id.btnPlayPause);
        m_forwardButton = (Button) findViewById(R.id.btnForward);
        m_loopButton = (Button) findViewById(R.id.btnLoop);

        m_ejectButton.setOnClickListener(this);
        m_rewindButton.setOnClickListener(this);
        m_playPauseButton.setOnClickListener(this);
        m_forwardButton.setOnClickListener(this);
        m_loopButton.setOnClickListener(this);

        // Find and setup image view for album art
        //m_albumArtImage = (ImageView) findViewById(R.id.imgAlbumArt);
        //Bitmap albumBitmap = BitmapFactory.decodeFile("/sdcard/albumthumbs/1334802432151");
        //m_albumArtImage.setImageBitmap(albumBitmap);

        // Start the music service
        //startService(new Intent(AudioService.ACTION_PLAY));
    }

    @Override
    public void onStart()
    {
    	super.onStart();

		//Specify music as the audio stream the application will use
		setVolumeControlStream(android.media.AudioManager.STREAM_MUSIC);

    	// Bind to the AudioService
    	Intent intent = new Intent(this, AudioService.class);
    	bindService(intent, m_serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause()
    {
    	super.onPause();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer)
    {
    	//m_mediaPlayer.start();
    }

    public void playPauseAction(View v)
    {
    	ToggleButton btnPlayPause = (ToggleButton) v;
    	if (btnPlayPause.isChecked())
    	{
    		// Pause action selected
    		m_audioService.pauseAction();
    	}
    	else
    	{
    		// Play action selected
    		m_audioService.playAction();
    	}
    }


    public void ejectAction(View v)
    {
    	Intent intent = new Intent(this, com.epllix.audioplayer.LibraryActivity.class);
    	startActivity(intent);
    }

    public void onClick(View target)
    {
    	if (target == m_ejectButton)
    		ejectAction(target);
    	else if ((target == m_playPauseButton) && (m_audioServiceBound == true))
    		playPauseAction(target);
    	else if ((target == m_rewindButton) && (m_audioServiceBound == true))
    		m_audioService.skipBackwardAction();
    	else if ((target == m_forwardButton) && (m_audioServiceBound == true))
    		m_audioService.skipForwardAction();
    }

    private ServiceConnection m_serviceConnection = new ServiceConnection()
    {

    	@Override
    	public void onServiceConnected(ComponentName className, IBinder service)
    	{
    		AudioServiceBinder binder = (AudioServiceBinder) service;
    		m_audioService = binder.getService();
    		m_audioServiceBound = true;
    	}

    	@Override
    	public void onServiceDisconnected(ComponentName className)
    	{
    		m_audioServiceBound = false;
    	}
    };
}
