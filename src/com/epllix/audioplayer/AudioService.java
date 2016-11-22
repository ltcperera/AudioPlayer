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
import com.epllix.audioplayer.library.*;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import android.media.MediaPlayer;

/**
 * Local bound service that handles the audio playback functionality. The
 * service is not exposed to other applications. It handles requests for play,
 * stop, pause, skip and seek.
 */
public class AudioService extends Service
{

	// Static constant definitions
	private static final int REPLAY_THRESHOLD = 3000;	// Duration in milliseconds over which the player will allow replay of the current track

	private MediaPlayer m_mediaPlayer = null;
	private AudioLibrary m_library = null;
	private final IBinder m_audioServiceBinder = new AudioServiceBinder();

    /**
     * Local binder for the audio service.
     * No other applications can bind to this service.
     */
    public class AudioServiceBinder extends Binder
    {
    	/**
    	 * Returns a reference to the AudioService
    	 */
    	AudioService getService()
    	{
    		return AudioService.this;
    	}
    }

    /**
     * Called by the android system when the service is created
     */
    @Override
	public void onCreate()
	{
		m_library = new AudioLibrary(getContentResolver());
		m_library.initialize();
		LibraryItem item = m_library.getFirstItem();
		//String artPath = m_library.getAlbumArt();

        m_mediaPlayer = new MediaPlayer();


        try
        {
            playUri(item.getUri());
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
	}

	/**
	 * Called by the android system to notify that the service is
	 * no longer in use and is being removed
	 */
    @Override
	public void onDestroy()
	{
    	// Release resources held by the media player
    	m_mediaPlayer.release();
    	m_mediaPlayer = null;
	}

    /**
     * Called by the android system when a client binds to this
     * service. The function returns an IBinder that can be used
     * for communication between this service and client.
     *
     * @return - IBinder object that will be used to communicate with the service
     */
	@Override
	public IBinder onBind(Intent intent)
	{
		// Return the IBinder to the client for communication with the service
		return m_audioServiceBinder;
	}

	/**
	 * Start audio playback
	 */
	public void playAction()
	{
		// Request audio focus for playback (add this functionality for API level 8 and later)
		//AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

		m_mediaPlayer.start();
	}

	/**
	 * Pause audio playback
	 */
	public void pauseAction()
	{
		m_mediaPlayer.pause();
	}

	/**
	 * Starts playing the specified media item
	 * @param uri
	 */
	public void playUri(String uri)
	{
		try
		{
			// Reset and start playing the specified media item
			m_mediaPlayer.reset();
			m_mediaPlayer.setDataSource(uri);
			m_mediaPlayer.prepare();
			playAction();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Play next audio track
	 * Shuffle back to the first track if the user skips past the last track.
	 */
	public void skipForwardAction()
	{
		// Get the next item
		LibraryItem item = m_library.getNextItem();
		if (item == null)
		{
			// Get the first item if the call failed
			item = m_library.getFirstItem();
		}

		// Setup the media player for playback if item was retrieved
		if (item != null)
		{
			playUri(item.getUri());
		}
	}

	/**
	 * Play previous audio track.
	 * Shuffle back to the last track if the user skips past the beginning.
	 */
	public void skipBackwardAction()
	{
		int currentPosition = m_mediaPlayer.getCurrentPosition();

		// Allow the user to replay the current track if the seek back
		//   button is clicked after the REPLAY_THRESHOLD seconds have passed.
		// If not the previous track is played back.
		if (currentPosition < REPLAY_THRESHOLD)
		{
			// Get the previous item
			LibraryItem item = m_library.getPreviousItem();
			if (item == null)
			{
				// Get the last item if the call failed
				item = m_library.getLastItem();
			}

			// Setup the media player for playback if item was retrieved
			if (item != null)
			{
				playUri(item.getUri());
			}
		}
		else
		{
			// Start playback of the current track from the beginning
			try
			{
				m_mediaPlayer.seekTo(0);
				return;
			}
			catch(IllegalStateException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
