package com.xyoye.common_component.source.media

import com.xyoye.common_component.extension.formatFileName
import com.xyoye.common_component.source.base.BaseVideoSource
import com.xyoye.common_component.source.base.VideoSourceFactory
import com.xyoye.common_component.utils.getFileName
import com.xyoye.data_component.enums.MediaType
import org.apache.commons.net.ftp.FTPFile

/**
 * Created by xyoye on 2021/11/21.
 */

class FTPMediaSource(
    private val index: Int,
    private val videoSources: List<FTPFile>,
    private val extSources: List<FTPFile>,
    private val rootPath: String,
    private val proxyUrl: String,
    private val currentPosition: Long,
    private var danmuPath: String?,
    private var episodeId: Int,
    private var subtitlePath: String?
) : BaseVideoSource(index, videoSources) {

    override fun getDanmuPath(): String? {
        return danmuPath
    }

    override fun setDanmuPath(path: String) {
        danmuPath = path
    }

    override fun getEpisodeId(): Int {
        return episodeId
    }

    override fun setEpisodeId(id: Int) {
        episodeId = id
    }

    override fun getSubtitlePath(): String? {
        return subtitlePath
    }

    override fun setSubtitlePath(path: String) {
        subtitlePath = path
    }

    override fun indexTitle(index: Int): String {
        return videoSources.getOrNull(index)?.name?.formatFileName() ?: ""
    }

    override suspend fun indexSource(index: Int): BaseVideoSource? {
        if (index in videoSources.indices) {
            val source = VideoSourceFactory.Builder()
                .setVideoSources(videoSources)
                .setExtraSource(extSources)
                .setRootPath(rootPath)
                .setIndex(index)
                .create(getMediaType())
                ?: return null
            return source as FTPMediaSource
        }
        return null
    }

    override fun getVideoUrl(): String {
        return proxyUrl
    }

    override fun getVideoTitle(): String {
        return getFileName(videoSources[index].name).formatFileName()
    }

    override fun getCurrentPosition(): Long {
        return currentPosition
    }

    override fun getMediaType(): MediaType {
        return MediaType.FTP_SERVER
    }

    override fun getHttpHeader(): Map<String, String>? {
        return null
    }

    override fun getUniqueKey(): String {
        return rootPath + "/" + getVideoTitle()
    }

}