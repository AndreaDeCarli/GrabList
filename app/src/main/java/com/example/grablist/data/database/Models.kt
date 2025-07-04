package com.example.grablist.data.database

import androidx.compose.ui.res.stringResource
import com.example.grablist.R

enum class Theme(val label: Int) {
    Light (R.string.theme_light),
    Dark (R.string.theme_dark),
    System (R.string.theme_system)
}

enum class Tier(val requirement: Int, val max: Int, val iconId: Int, val label: Int) {
    None(0,3, -1, R.string.tier_none),
    Check(3,6, R.drawable.check, R.string.tier_check),
    Bronze(6, 9, R.drawable.bronze, R.string.tier_bronze),
    Silver(9, 12, R.drawable.silver, R.string.tier_silver),
    Gold(12, 15, R.drawable.gold, R.string.tier_gold),
    Star(15, 18,R.drawable.star, R.string.tier_star),
    Diamond(18, 21, R.drawable.diamond, R.string.tier_diamond );
}


fun evaluateTier(progress: Int): Tier{
    var chosen: Tier = Tier.None
    for (tier in Tier.entries){
        if (progress >= tier.requirement && progress < tier.max){
            chosen = tier
            break
        }
    }
    return chosen
}

fun evaluateNextTier(progress: Int): Tier{
    return evaluateTier(progress + 3)
}