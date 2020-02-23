package ru.nik.logparser.controller

import org.springframework.web.bind.annotation.*
import ru.nik.logparser.model.EventType
import ru.nik.logparser.service.EventService
import java.util.logging.Logger


@RestController
@RequestMapping(path = ["/events"])
class ApiController {
    companion object {
        val log: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    val eventService: EventService = EventService()

    @GetMapping
    fun list(): List<EventType> {
        log.info("Get all event types")
        return eventService.listEventTypes()
    }

    @PostMapping
    fun add(@RequestBody event: EventType) {
        log.info("Add new event type: $event")
        eventService.addEventType(event)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Int) {
        log.info("Delete event type with id: $id")
        eventService.deleteEventType(id)
    }

}