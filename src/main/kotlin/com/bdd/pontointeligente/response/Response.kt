package com.bdd.pontointeligente.response

data class Response<T> (
        val erros: ArrayList<String> = arrayListOf(),
        var data: T? = null /*pode tiver erro, entao retornará nulo*/
)