package com.example.grablist.data.database

import com.example.grablist.R

enum class Theme {
    Light,
    Dark,
    System
}

enum class Tier(val requirement: Int, val max: Int, val iconId: Int) {
    None(0,3, -1),
    Bronze(3, 6, R.drawable.bronze),
    Silver(6, 9, R.drawable.silver),
    Gold(9, 12, R.drawable.gold),
    Star(12, 999,R.drawable.star);
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