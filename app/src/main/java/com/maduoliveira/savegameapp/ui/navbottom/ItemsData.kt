package com.maduoliveira.savegameapp.ui.navbottom

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.ButtonBackground
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.ReportsAnimation
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.ColorButtonAnimation
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.GearColorButton
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.PlusColorButton
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.TransactionColorButton


@Stable
data class WiggleButtonItem(
    @DrawableRes val backgroundIcon: Int,
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    @StringRes val description: Int,
    val animationType: ColorButtonAnimation = TransactionColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.plus)
    ),
)

@Stable
data class Item(
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    @StringRes val description: Int,
    val animationType: ColorButtonAnimation = TransactionColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.plus)
    ),
)

val wiggleButtonItems = listOf(
    WiggleButtonItem(
        icon = R.drawable.outline_favorite,
        backgroundIcon = R.drawable.favorite,
        isSelected = false,
        description = R.string.Heart,
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_energy_leaf,
        backgroundIcon = R.drawable.energy_savings_leaf,
        isSelected = false,
        description = R.string.Leaf
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_water_drop,
        backgroundIcon = R.drawable.water_drop,
        isSelected = false,
        description = R.string.Drop
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_circle,
        backgroundIcon = R.drawable.circle,
        isSelected = false,
        description = R.string.Circle
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_laptop,
        backgroundIcon = R.drawable.laptop,
        isSelected = false,
        description = R.string.Laptop
    ),
)

val dropletButtons = listOf(
    Item(
        icon = R.drawable.home,
        isSelected = false,
        description = R.string.Home
    ),
    Item(
        icon = R.drawable.bell,
        isSelected = false,
        description = R.string.Bell
    ),
    Item(
        icon = R.drawable.chat_bubble,
        isSelected = false,
        description = R.string.Message
    ),
    Item(
        icon = R.drawable.heart,
        isSelected = false,
        description = R.string.Heart
    ),
    Item(
        icon = R.drawable.person,
        isSelected = false,
        description = R.string.Person
    ),
)

val colorButtons = listOf(
    Item(
        icon = R.drawable.outline_home,
        isSelected = true,
        description = R.string.Home,
        animationType = TransactionColorButton(
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 20f),
            background = ButtonBackground(
                icon = R.drawable.circle,
                offset = DpOffset(2.5.dp, 3.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.sync,
        isSelected = false,
        description = R.string.Bell,
        animationType = TransactionColorButton(
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 20f),
            background = ButtonBackground(
                icon = R.drawable.square,
                offset = DpOffset(1.dp, 2.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.add_box,
        isSelected = false,
        description = R.string.Plus,
        animationType = PlusColorButton(
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = Spring.StiffnessVeryLow
            ),
            background = ButtonBackground(
                icon = R.drawable.hexagon,
                offset = DpOffset(1.6.dp, 2.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.report,
        isSelected = false,
        description = R.string.Calendar,
        animationType = ReportsAnimation(
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = Spring.StiffnessVeryLow
            ),
            background = ButtonBackground(
                icon = R.drawable.heart,
                offset = DpOffset(1.dp, 1.5.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.gear,
        isSelected = false,
        description = R.string.Settings,
        animationType = GearColorButton(
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = Spring.StiffnessVeryLow
            ),
            background = ButtonBackground(
                icon = R.drawable.settings,
                offset = DpOffset(2.5.dp, 3.dp)
            ),
        )
    )
)