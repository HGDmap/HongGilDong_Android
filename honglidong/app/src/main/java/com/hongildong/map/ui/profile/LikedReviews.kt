package com.hongildong.map.ui.profile

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.ui.search.location_detail.facility.review.FacilityReviewItem

@Composable
fun LikedReviews(
    reviews: List<ReviewInfo>,
    onDeleteItem: (Int) -> Unit = {},
    onEditItem: (ReviewInfo) -> Unit = {},
    onLikeItem: (Int) -> Unit = {}
) {
    LazyColumn {
        items(reviews) { review ->
            FacilityReviewItem(
                reviewItem = review,
                onDeleteItem = {
                    onDeleteItem(review.id)
                },
                onEditItem = {
                    onEditItem(review)
                },
                onLikeItem = {
                    onLikeItem(review.id)
                }
            )
        }
    }
}