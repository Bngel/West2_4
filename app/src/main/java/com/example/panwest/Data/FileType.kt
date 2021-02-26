package com.example.panwest.Data

val musicFormat = listOf("mp3", "wma", "rm", "wav", "mid", "ape", "flac")

val movieFormat = listOf("mpg", "mpeg", "avi", "rm", "rmvb", "mov", "wmv", "asf", "dat", "asx", "wvx", "mpe", "mpa")

val photoFormat = listOf("bmp", "jpg", "jpeg", "png", "gif", "ico")

val rarFormat = listOf("zip", "rar", "7z")

fun getFileFormat(fileName: String): String {
    val parse = fileName.split('.')
    val fmt = parse[1]
    val MUSIC_STRING = "MUSIC"
    val MOVIE_STRING = "MOVIE"
    val PHOTO_STRING = "PHOTO"
    val FILE_STRING = "FILE"
    val RAR_STRING = "RAR"
    return when (fmt) {
        in musicFormat -> MUSIC_STRING
        in movieFormat -> MOVIE_STRING
        in photoFormat -> PHOTO_STRING
        in rarFormat -> RAR_STRING
        else -> FILE_STRING
    }
}