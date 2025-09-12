package com.happyhope.bubbletea.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Design tokens for BubbleTea app following Material 3 guidelines
 * These tokens provide consistent spacing, shapes, and component styles
 */
object BubbleTeaTokens {
    
    // Spacing tokens
    object Spacing {
        val extraSmall = 4.dp
        val small = 8.dp
        val medium = 16.dp
        val large = 24.dp
        val extraLarge = 32.dp
        val huge = 48.dp
    }
    
    // Shape tokens
    object Shapes {
        val extraSmall = RoundedCornerShape(4.dp)
        val small = RoundedCornerShape(8.dp)
        val medium = RoundedCornerShape(12.dp)
        val large = RoundedCornerShape(16.dp)
        val extraLarge = RoundedCornerShape(24.dp)
        val circular = RoundedCornerShape(50)
    }
    
    // Elevation tokens
    object Elevation {
        val none = 0.dp
        val small = 2.dp
        val medium = 4.dp
        val large = 8.dp
        val extraLarge = 16.dp
    }
    
    // Component-specific tokens
    object Cards {
        @Composable
        fun defaultElevation() = CardDefaults.cardElevation(
            defaultElevation = Elevation.small
        )
        
        @Composable
        fun elevatedCardElevation() = CardDefaults.cardElevation(
            defaultElevation = Elevation.medium
        )
    }
    
    object Buttons {
        @Composable
        fun primaryButtonElevation() = ButtonDefaults.buttonElevation(
            defaultElevation = Elevation.small,
            pressedElevation = Elevation.medium
        )
    }
    
    // Icon sizes
    object IconSize {
        val small = 16.dp
        val medium = 24.dp
        val large = 32.dp
        val extraLarge = 48.dp
    }
    
    // Border widths
    object BorderWidth {
        val thin = 1.dp
        val medium = 2.dp
        val thick = 4.dp
    }
}