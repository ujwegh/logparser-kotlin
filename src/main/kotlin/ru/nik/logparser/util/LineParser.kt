package ru.nik.logparser.util

import ru.nik.logparser.model.EventType
import ru.nik.logparser.model.ParsedEvent
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import kotlin.collections.HashMap

class LineParser {
    private var compiledPatterns: Map<String, Pattern> = Collections.synchronizedMap(HashMap())

    private fun compile(pattern: String): Pattern {
        var p: Pattern? = compiledPatterns[pattern]
        if (p != null) return p
        p = Pattern.compile(pattern)
        compiledPatterns = compiledPatterns + Pair(pattern, p)
        return p
    }

    fun parse(line: String?, eventTypes: List<EventType>, sameDate: Date?): List<ParsedEvent> {
        val events: MutableList<ParsedEvent> = mutableListOf()
        val timestamp = sameDate ?: Date()
        eventTypes.forEach(Consumer { eventType: EventType ->
            val pattern: Pattern = compile(eventType.pattern)
            val matcher = pattern.matcher(line ?: "")
            val params: MutableMap<String, String>
            if (matcher.find()) {
                params = HashMap()
                eventType.data.forEach { (key: String, value1: String?) ->
                    val parameterPattern: Pattern = compile(value1)
                    val parameterMatcher = parameterPattern.matcher(line ?: "")
                    var value = ""
                    if (parameterMatcher.find()) {
                        value = if (parameterMatcher.groupCount() > 0) {
                            parameterMatcher.group(1)
                        } else {
                            parameterMatcher.group(0)
                        }
                    }
                    if (value.isNotEmpty()) {
                        params[key] = value
                    }
                }
                events += (ParsedEvent(eventType.id, timestamp, params))
            }
        })
        return events
    }

}