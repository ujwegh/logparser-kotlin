package ru.nik.logparser.service

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.nik.logparser.model.EventType
import java.io.File
import java.io.IOException
import java.util.concurrent.locks.ReentrantLock
import java.util.logging.Logger

/**
 * Сервис управления событиями
 * сохраняем/удаляем типы событий в rules.json
 */
class EventService {
    companion object {
        val log: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    var events: List<EventType> = loadEvents()
    private val locker: ReentrantLock = ReentrantLock()

    private fun loadEvents(): List<EventType> {
        return try {
            val mapper = jacksonObjectMapper()
            mapper.registerKotlinModule()
            mapper.registerModule(JavaTimeModule())
            val json: String = File("rules.json").readText(Charsets.UTF_8)
            mapper.readValue<List<EventType>>(json)
        } catch (ex: Exception) {
            emptyList()
        }
    }

    fun listEventTypes(): List<EventType> {
        return events
    }

    fun addEventType(event: EventType) {
        locker.lock()
        try {
            val newEvents: MutableList<EventType> = events.toMutableList()
            newEvents += event
            events = newEvents
            writeEvents()
        } catch (ex: java.lang.Exception) {
            log.info("Failed to add new event type: $event")
        } finally {
            locker.unlock()
        }
    }

    fun deleteEventType(id: Int) {
        locker.lock()
        try {
            val eventTypes: MutableList<EventType> = events.toMutableList()
            eventTypes.removeIf { et: EventType -> et.id == id }
            events = eventTypes
            writeEvents()
        } catch (ex: java.lang.Exception) {
            log.info("Failed to delete event type with id: $id")
        } finally {
            locker.unlock()
        }
    }

    @Throws(IOException::class)
    private fun writeEvents() {
        val mapper = ObjectMapper()
        val writer = mapper.writer(DefaultPrettyPrinter())
        writer.writeValue(File("rules.json"), events)
    }
}

