package com.udacity.asteroidradar

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

private const val TAG = "BindingAdapter"

//Creating Picture of the Day
@BindingAdapter("picOfTheDay")
fun bindPictureOfTheDayImage(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if(it.mediaType.equals("image")) {
            Picasso.get()
                .load(it.url)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.loading_img)
                .into(imageView)
        } else {
            Toast.makeText(imageView.context, "NASA does not have image for this ${it.title}", Toast.LENGTH_SHORT).show()
            Log.w(TAG, "NASA does not have image for this ${it.title}")
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
