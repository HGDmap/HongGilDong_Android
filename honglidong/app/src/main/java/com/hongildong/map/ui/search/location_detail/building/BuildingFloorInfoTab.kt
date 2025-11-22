package com.hongildong.map.ui.search.location_detail.building

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.Floor
import com.hongildong.map.data.entity.FloorFacility
import com.hongildong.map.data.entity.FloorInfo
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.PrimaryLight
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White

@Composable
fun BuildingFloorInfoTab(
    buildingInfo: FacilityInfo,
    onClickFacility: (FloorFacility) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp)
    ) {
        items(buildingInfo.floorFacilities!!) {
            FloorInfoItem(
                floorInfo = it,
                onClickFacility = { facilityInfo ->
                    onClickFacility(facilityInfo)
                }
            )
        }
    }
}

@Composable
fun FloorInfoItem(
    floorInfo: FloorInfo,
    onClickFacility: (FloorFacility) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        FloorItem(
            floor = floorInfo.floor,
            isExpanded = isExpanded,
            onClick = {
                isExpanded = !isExpanded
            }
        )
        if (isExpanded) {
            floorInfo.facilities.forEach { facility ->
                FacilityItem(
                    facility = facility,
                    onClickFacility = {
                        onClickFacility(facility)
                    }
                )
            }
        }
    }
}


@Composable
fun FloorItem(
    floor: String,
    isExpanded: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(3.dp)
            //.shadow(elevation = 3.dp, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .border(width = 1.dp, color = if (isExpanded) PrimaryMid else Gray300, shape = RoundedCornerShape(12.dp))
            .background(color = if (isExpanded) PrimaryLight.copy(alpha = 0.4f) else White, shape = RoundedCornerShape(12.dp))
            .padding(10.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        Text(
            text = Floor.fromApiName(floor)?.displayName ?: "?층",
            style = AppTypography.Medium_15.copy(color = if (isExpanded) PrimaryMid else Gray600),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = if (isExpanded) PrimaryMid else Gray600),
            modifier = Modifier
                .rotate(if (isExpanded) 90f else -90f)
                .clickable {
                    onClick()
                }
        )
    }
}

@Composable
fun FacilityItem(
    facility: FloorFacility,
    onClickFacility: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickFacility()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = facility.name,
                style = AppTypography.Regular_15.copy(color = Gray600),
                modifier = Modifier
                    .padding(3.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Gray600),
                modifier = Modifier.size(15.dp)
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Gray600)
    }
}