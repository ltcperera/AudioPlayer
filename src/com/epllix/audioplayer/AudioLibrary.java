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
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;

/**
 * This class provides access to audio files stored
 * on external storage of the device.
 */
public class AudioLibrary
{
	private ContentResolver m_resolver = null;
	private Cursor m_cursor = null;

	public AudioLibrary(ContentResolver resolver)
	{
		m_resolver = resolver;
	}

	public void initialize()
	{
		m_cursor = m_resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + "= 1", null, null);
		m_cursor.moveToFirst();
	}

	public LibraryItem getItem()
	{
		LibraryItem item = new LibraryItem();

		int idIndex = m_cursor.getColumnIndex(Audio.Media._ID);
		int titleIndex = m_cursor.getColumnIndex(Audio.Media.TITLE);
		int dataIndex = m_cursor.getColumnIndex(Audio.Media.DATA);
		int albumIndex = m_cursor.getColumnIndex(Audio.Media.ALBUM);
		int artistIndex = m_cursor.getColumnIndex(Audio.Media.ARTIST);
		int composerIndex = m_cursor.getColumnIndex(Audio.Media.COMPOSER);

		item.setID(m_cursor.getString(idIndex));
		item.setTitle(m_cursor.getString(titleIndex));
		item.setUri(m_cursor.getString(dataIndex));
		item.setAlbum(m_cursor.getString(albumIndex));
		item.setArtist(m_cursor.getString(artistIndex));
		item.setComposer(m_cursor.getString(composerIndex));

		return item;
	}

	public String getAlbumArt()
	{
		//m_cursor.moveToPosition(0);
		//int albumId = m_cursor.getColumnIndex(Audio.Media.ALBUM_ID);
		/*Cursor artCursor = m_resolver.query(
				ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, albumId),
				new String[] { MediaStore.Audio.AlbumColumns.ALBUM_ART },
				null, null, null);*/
		Cursor artCursor = m_resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null, null, null);
		artCursor.moveToFirst();
		//int columnIndex = m_cursor.getColumnIndex(Audio.Media.ALBUM_ART);
		int columnIndex = artCursor.getColumnIndex(Audio.AlbumColumns.ALBUM_ART);
		return artCursor.getString(columnIndex);
	}

	public LibraryItem getFirstItem()
	{
		m_cursor.moveToFirst();
		return getItem();
	}

	public LibraryItem getLastItem()
	{
		m_cursor.moveToLast();
		return getItem();
	}

	/**
	 * Get the next item from the audio library
	 *
	 * @return libraryItem	- The next library item if found
	 *                        null if the item is not found
	 *
	 */
	public LibraryItem getNextItem()
	{
		// Initialize the library item
		LibraryItem item = null;

		// Retrieve the next library item
		if (m_cursor.moveToNext())
		{
			item = getItem();
		}

		return item;
	}

	/**
	 * Returns the previous item from the audio library
	 *
	 * @return - the previous item in the library if found
	 * 			 null if the item is not found
	 */
	public LibraryItem getPreviousItem()
	{
		LibraryItem item = null;

		if (m_cursor.moveToPrevious())
		{
			item = getItem();
		}

		return item;
	}

	public int getItemCount()
	{
		return m_cursor.getCount();
	}
}
