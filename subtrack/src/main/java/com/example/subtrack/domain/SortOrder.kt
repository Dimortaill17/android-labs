package com.example.subtrack.domain

import androidx.annotation.StringRes
import com.example.subtrack.R

enum class SortOrder(@param:StringRes val title: Int) {
    PRICE_DESC(R.string.sort_price_desc),
    PRICE_ASC(R.string.sort_price_asc),
    DATE_NEAR(R.string.sort_date_near),
    DATE_FAR(R.string.sort_date_far),
    NAME(R.string.sort_name),
    CATEGORY(R.string.sort_category)
}
