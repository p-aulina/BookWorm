package com.example.bookworm.domain.model

enum class BookStatus(val label: String) {
    TBR("To Read"),
    READING("Reading"),
    FINISHED("Read"),
    DNF("Did Not Finished")
}