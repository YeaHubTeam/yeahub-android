package ru.yeahub.profile.impl.domain

import kotlinx.collections.immutable.PersistentList

data class DomainUserProfile(
    val id: String,
    val username: String,
    val avatarUrl: String?,
    val city: String?,
    val country: String?,
    val telegramUsername: String?,
    val aboutMe: String?,
    val roles: PersistentList<String>,
    val skills: PersistentList<String>,
    val specialization: String?,
    val socialNetworks: PersistentList<SocialNetwork>
)

data class SocialNetwork(
    val code: String,
    val url: String?
)