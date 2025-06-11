package com.example.network_impl

import kotlinx.coroutines.runBlocking

val api = ApiFactory.apiService

fun main() {
    runBlocking {
        getQuestion()
        getQuestionById()
        getSkills()
        getAllSpecializations()
        getSpecializationById()
    }
    kotlin.system.exitProcess(0)
}


suspend fun getQuestion() {
    val result = api.getQuestions()
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
    val result = api.getSkills()
    println("result for: fun getSkills()")
    println(result)
    println()
}

suspend fun getAllSpecializations() {
    val result = api.getAllSpecializations()
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