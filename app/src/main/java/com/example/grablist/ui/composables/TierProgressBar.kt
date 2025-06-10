package com.example.grablist.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.grablist.R
import com.example.grablist.data.database.Tier
import com.example.grablist.data.database.evaluateNextTier
import com.example.grablist.data.database.evaluateTier

@Composable
fun TierProgressBar(
    progress: Int
) {

    val tier: Tier = evaluateTier(progress = progress)
    val nextTier = evaluateNextTier(progress)

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier.padding(12.dp).fillMaxWidth().height(120.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.padding(horizontal = 10.dp).weight(0.5F).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .weight(0.8F)
                        .height(10.dp),
                    progress = { if (tier == Tier.Star) 1.0F else progress/tier.max.toFloat() },
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeCap = StrokeCap.Round,
                )
                Text(text = "${progress}/${if (tier == Tier.Star) "-" else tier.max}",
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.2F),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier.weight(0.5F).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("${stringResource(R.string.current_generic)}: ${stringResource(tier.label)}")
                if (tier != Tier.None){
                    Image(
                        painter = painterResource(tier.iconId),
                        contentDescription = "currentTier",
                        modifier = Modifier.size(40.dp))
                }
                if (tier != Tier.Star){
                    Text("${stringResource(R.string.next_generic)}: ${stringResource(nextTier.label)}")
                    Image(
                        painter = painterResource(nextTier.iconId),
                        contentDescription = "nextTier",
                        modifier = Modifier.size(40.dp))
                }

            }

        }

    }
}