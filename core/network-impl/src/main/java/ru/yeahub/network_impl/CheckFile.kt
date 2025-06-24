package ru.yeahub.network_impl

import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext.startKoin
import ru.yeahub.network_api.ApiService

val page = 1
val limit = 10
lateinit var api: ApiService

fun main() {

    //только для тестов и желательно удалить, запускать отдельно от приложения
    val koin = startKoin {
        modules(networkModule)
    }.koin

    api = koin.get<ApiService>()

    runBlocking {

        getQuestion()
        getQuestionById()
        getSkills()
        getSpecializations()
        getSpecializationById()
    }
    kotlin.system.exitProcess(0)
}


suspend fun getQuestion() {
    val result = api.getQuestions(page, limit)
    println("result for: fun getQuestion()")
    println(result)
    println()

}

suspend fun getQuestionById() {
    val result = api.getQuestionById(103)
    println("result for: fun getQuestionById()")
    println(result)
    println()
}

suspend fun getSkills() {
    val result = api.getSkills(page, limit)
    println("result for: fun getSkills()")
    println(result)
    println()
}

suspend fun getSpecializations() {
    val result = api.getSpecializations(page, limit)
    println("result for: fun getAllSpecializations()")
    println(result)
    println()
}

suspend fun getSpecializationById() {
    val result = api.getSpecializationById(27)
    println("result for: fun getSpecializationById()")
    println(result)
    println()
}
