package ru.yeahub.detail_question.impl.data.local

import ru.yeahub.network_api.models.NestedSpecializationResponse

private val gurus: List<Guru> =
    listOf(
        Guru(
            name = "Ruslan Kuyanets",
            title = "Frontend Guru",
            specializationId = 11,
            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                    "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
            youtubeUrl = "https://youtube.com/@reactify-it",
            telegramUrl = "https://t.me/reactify_IT"
        ),
        Guru(
            name = "Anton Gulyaev",
            title = "Android Guru",
            specializationId = 27,
            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus" +
                    "/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
            youtubeUrl = "https://youtube.com/@gulyaev_it",
            telegramUrl = "https://t.me/gulyaev_it"
        ),
        Guru(
            name = "Roman Isakov",
            title = "iOS Guru",
            specializationId = 26,
            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus/рома.jpg",
            youtubeUrl = "https://www.youtube.com/watch?v=VdN4PKgnzRg&ab/_channel=RomanIsakov",
            telegramUrl = "https://t.me/isakov/_ios"
        )
    )

fun getGuru(specializations: List<NestedSpecializationResponse>): Guru {
    val specializationIds = specializations.map { it.id }
    return gurus.first { guru ->
        specializationIds.contains(guru.specializationId)
    }
}
