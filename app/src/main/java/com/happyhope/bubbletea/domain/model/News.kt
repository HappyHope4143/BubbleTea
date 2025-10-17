package com.happyhope.bubbletea.domain.model

data class News(
    val id: Long,
    val title: String,
    val summary: String,
    val url: String,
    val source: String,
    val createdAt: Long
)