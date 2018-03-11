package com.tripletres.android.gcvvr.googlecardboardvrexample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.google.vr.sdk.widgets.video.VrVideoEventListener
import com.google.vr.sdk.widgets.video.VrVideoView


/**
 * Video VR fragment
 *
 * A simple [Fragment] subclass.
 */
class VideoFragment : Fragment() {

    private val VIDEO_NAME = "congo_2048.mp4"

    private var seekBar: SeekBar? = null
    private var videoView: VrVideoView? = null
    private var statusText: TextView? = null

    /**
     * Preserve the video's state and duration when rotating the phone. This improves
     * performance when rotating or reloading the video.
     */
    private val STATE_IS_PAUSED = "isPaused"
    private val STATE_VIDEO_DURATION = "videoDuration"
    private val STATE_PROGRESS_TIME = "progressTime"

    /**
     * By default, the video will start playing as soon as it is loaded.
     */
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        seekBar = view.findViewById(R.id.seek_bar)
        videoView = view.findViewById(R.id.video_view)
        statusText = view.findViewById(R.id.status_text)

        //Restore if needed
        if (savedInstanceState != null) {
            val progressTime = savedInstanceState.getLong(STATE_PROGRESS_TIME)
            videoView?.seekTo(progressTime)
            seekBar?.max = savedInstanceState.getLong(STATE_VIDEO_DURATION).toInt()
            seekBar?.progress = progressTime.toInt()

            isPaused = savedInstanceState.getBoolean(STATE_IS_PAUSED)
            if (isPaused) {
                videoView?.pauseVideo()
            }
        } else {
            seekBar?.isEnabled = false
        }

        //Initialize SeekBar
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoView?.seekTo(progress.toLong())
                    updateStatusText()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        //Initialize VrVideoView
        videoView?.setEventListener(object : VrVideoEventListener() {
            override fun onLoadSuccess() {
                Log.d("VideoVr", "onLoadSuccess")
                seekBar?.max = videoView?.duration!!.toInt()
                seekBar?.isEnabled = true
                updateStatusText()
            }

            override fun onLoadError(errorMessage: String?) {
                Toast.makeText(context, "Error: #{errorMessage}", Toast.LENGTH_SHORT).show()
                Log.d("VideoVr", "onLoadError: #{errorMessage}")
            }

            override fun onClick() {
                if (isPaused)
                    videoView?.playVideo()
                else
                    videoView?.pauseVideo()
            }

            /**
             * Update the progress bar for the video
             */
            override fun onNewFrame() {
                updateStatusText()
                seekBar?.progress = videoView?.currentPosition!!.toInt()
            }

            override fun onCompletion() {
                videoView?.seekTo(0)
            }
        })

        return view
    }

    /**
     * Start video when the view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            if (videoView?.duration!! <= 0) {
                val options = VrVideoView.Options()
                videoView?.loadVideoFromAsset(VIDEO_NAME, options)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error loading video", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateStatusText() {
        statusText?.text = if (isPaused) "paused" else "playing"
    }


    override fun onPause() {
        super.onPause()
        videoView?.pauseRendering()
        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        videoView?.resumeRendering()
        updateStatusText()
    }

    override fun onDestroy() {
        videoView?.shutdown()
        super.onDestroy()
    }

}
