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
package com.epllix.audioplayer.library;

import android.provider.MediaStore.Audio;

public class LibraryItem {
	private String m_ID;
	private String m_title;
	private String m_uri;
	private String m_album;
	private String m_artist;
	private String m_composer;

	public void setID(String id)
	{
		m_ID = id;
	}

	public String getUri()
	{
		return m_uri;
	}

	public void setTitle(String title)
	{
		m_title = title;
	}

	public void setUri(String uri)
	{
		m_uri = uri;
	}

	public void setAlbum(String album)
	{
		m_album = album;
	}

	public void setArtist(String artist)
	{
		m_artist = artist;
	}

	public void setComposer(String composer)
	{
		m_composer = composer;
	}
}
