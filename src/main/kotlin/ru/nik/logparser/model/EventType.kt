package ru.nik.logparser.model

class EventType() {

    var id: Int = 0
    var pattern = ""
    var data: Map<String, String> = HashMap()

    constructor(id: Int, pattern: String, data: Map<String, String>) : this() {
        this.id = id
        this.pattern = pattern
        this.data = data
    }
}