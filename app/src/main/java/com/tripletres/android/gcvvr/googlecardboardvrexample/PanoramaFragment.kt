package com.tripletres.android.gcvvr.googlecardboardvrexample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import kotlinx.android.synthetic.main.fragment_panorama.*


/**
 * Panorama view fragment
 */
class PanoramaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panorama, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeImageLoader()
        loadPanoramicImage()
    }

    private fun initializeImageLoader() {
        val config = ImageLoaderConfiguration.Builder(context).build()
        ImageLoader.getInstance().init(config)
    }

    private fun loadPanoramicImage() {

        //Load image
        val imageLoader = ImageLoader.getInstance()
        imageLoader.loadImage(IMAGE_PATH, object : SimpleImageLoadingListener() {
            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                pano_view.visibility = View.VISIBLE
                val viewOptions = VrPanoramaView.Options()
                viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER
                pano_view.loadImageFromBitmap(loadedImage, viewOptions)
            }
        })
    }

    override fun onPause() {
        pano_view.pauseRendering()
        super.onPause()
    }

    override fun onResume() {
        pano_view.resumeRendering()
        super.onResume()
    }

    override fun onDestroy() {
        pano_view.shutdown()
        super.onDestroy()
    }

    companion object {

        const val ASSETS_URI = "assets://"
        val IMAGE_PATH = ASSETS_URI + "sample_converted.jpg"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PanoramaFragment.
         */
        fun newInstance(): PanoramaFragment {
            val fragment = PanoramaFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
