package com.freak.quiz_app

enum class QuestionType{
    Text,
    Radio,
    Checkbox
}



data class Question(val id:Int,val type:QuestionType, val qText:String, val options: List<String>?,val answers: List<String>)