package ru.nik.logparser.model

import java.util.*
import kotlin.collections.HashMap

class ParsedEvent() {
    var eventTypeId: Int = 0
    var eventTimestamp: Date = Date()
    var data: Map<String, String> = HashMap()

    constructor(eventTypeId: Int, eventTimestamp: Date, data: Map<String, String>) : this() {
        this.eventTypeId = eventTypeId
        this.eventTimestamp = eventTimestamp
        this.data = data
    }
}