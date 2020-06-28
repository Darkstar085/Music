/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package io.github.muntashirakon.music.mvp.presenter

import io.github.muntashirakon.music.model.Playlist
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.mvp.BaseView
import io.github.muntashirakon.music.mvp.Presenter
import io.github.muntashirakon.music.mvp.PresenterImpl
import io.github.muntashirakon.music.providers.interfaces.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by hemanths on 20/08/17.
 */
interface PlaylistSongsView : BaseView {

    fun songs(songs: List<Song>)
}

interface PlaylistSongsPresenter : Presenter<PlaylistSongsView> {
    fun loadPlaylistSongs(playlist: Playlist)

    class PlaylistSongsPresenterImpl @Inject constructor(
        private val repository: Repository
    ) : PresenterImpl<PlaylistSongsView>(), PlaylistSongsPresenter, CoroutineScope {

        private var job: Job = Job()

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        override fun loadPlaylistSongs(playlist: Playlist) {
            launch {
                val songs = repository.getPlaylistSongs(playlist)
                view?.songs(songs)
            }
        }

        override fun detachView() {
            super.detachView()
            job.cancel()
        }
    }
}