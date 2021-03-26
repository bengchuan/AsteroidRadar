package com.udacity.asteroidradar

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants.SUPPORTED_MEDIA_TYPE
import com.udacity.asteroidradar.main.AsteroidListAdapter

private const val TAG = "BindingAdapter"

//Creating Picture of the Day
@BindingAdapter("picOfTheDay")
fun bindPictureOfTheDayImage(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (it.mediaType.toLowerCase().trim().equals(SUPPORTED_MEDIA_TYPE)) {
            Picasso.get()
                .load(it.url)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.loading_img)
                .into(imageView)
        } else {
            Log.w(
                TAG,
                "NASA does not have image for this '${it.title}'. Media Type is '${it.mediaType}'"
            )
        }
    }
}

// List of Asteriods
@BindingAdapter("asteroidList")
fun bindAsteriodList(recyclerView: RecyclerView, data: List<Asteroid?>?) {
    val adapter = recyclerView.adapter as AsteroidListAdapter
    adapter.submitList(data) {
        recyclerView.scrollToPosition(0)
    }
}

@BindingAdapter("showLoadingAsteriodError")
fun bindShowLoadingAsteriodError(textView: TextView, data: List<Asteroid?>?) {
    if (data.isNullOrEmpty()) {
        textView.setText(R.string.error_unable_to_load_list)
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.GONE
    }
}

@BindingAdapter("showFavoriteIconStatus")
fun bindAsteriodFavoriteIconStatus(button: FloatingActionButton, isFavorite: Boolean) {
    val context = button.context
    if (isFavorite) {
        button.setImageResource(R.drawable.ic_heart_on)
        button.contentDescription = context.getString(R.string.favorite_asteriod)
    } else {
        button.setImageResource(R.drawable.ic_heart_off)
        button.contentDescription = context.getString(R.string.not_favorite_asteriod)
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_icon)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_icon)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
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

