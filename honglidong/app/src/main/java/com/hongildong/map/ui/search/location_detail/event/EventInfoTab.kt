package com.hongildong.map.ui.search.location_detail.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.EventDetailInfo
import com.hongildong.map.ui.search.location_detail.facility.IconWithText
import com.hongildong.map.ui.theme.PrimaryLight
import com.hongildong.map.ui.util.PhoneNumberText
import com.hongildong.map.ui.util.WebsiteText
import com.hongildong.map.ui.util.formatDate

@Composable
fun EventInfoTab(
    eventInfo: EventDetailInfo
) {
    Column(
        modifier = Modifier.fillMaxSize(1f)
    ) {
        IconWithText(icon = R.drawable.ic_calendar, text = "${formatDate(eventInfo.eventInfo.eventStart)} ~ ${formatDate(eventInfo.eventInfo.eventEnd)}")
        IconWithText(icon = R.drawable.ic_location_info, text = eventInfo.location)
        IconWithText(icon = R.drawable.ic_location_open, text = if (eventInfo.locationInfo.isEventOpen) "영업중" else "영업종료")
        if (eventInfo.eventInfo.callNumber.isNotEmpty()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_phone),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 5.dp),
                    tint = PrimaryLight
                )
                PhoneNumberText(
                    phoneNumber = eventInfo.eventInfo.callNumber,
                )
            }
        }
        if (eventInfo.eventInfo.homepage.isNotEmpty()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_link),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 5.dp),
                    tint = PrimaryLight
                )
                WebsiteText(
                    url = eventInfo.eventInfo.homepage,
                )
            }
        }
    }
}