package com.example.jumbotest.ui.image

import android.widget.ImageView
import com.example.jumbotest.R
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PicassoImageHandler @Inject constructor() : ImageHandler {

    override fun loadPicture(imageView: ImageView, url: String) {
        Picasso.get().load(url).placeholder(R.drawable.ic_image_placeholder)
            .into(imageView)
    }
}
