package ru.nik.logparser.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import ru.nik.logparser.model.EventType
import ru.nik.logparser.model.ParsedEvent
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FileParser {
    fun parse(inFolder: String, outFolder: String, fileName: String, eventTypes: List<EventType>): Boolean {
        try {
            FileInputStream(File("$inFolder/$fileName"))
                    .use { inStream -> FileOutputStream(File("$outFolder/$fileName"))
                            .use { outStream -> return parse(inStream, outStream, eventTypes, null) } }
        } catch (ex: IOException) {
            return false
        }
    }

    private fun parse(inFile: InputStream, outFile: OutputStream, eventTypes: List<EventType>, sameDate: Date?): Boolean {
        try {
            BufferedReader(InputStreamReader(inFile)).use { br ->
                BufferedWriter(OutputStreamWriter(outFile)).use { bw ->
                    val mapper = ObjectMapper()
                    val df = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    var st: String?
                    while (br.readLine().also { st = it } != null) {
                        val events: List<ParsedEvent> = LineParser().parse(st, eventTypes, sameDate)
                        for (event in events) {
                            val node = mapper.createObjectNode()
                            node.put("eventTypeId", event.eventTypeId)
                            node.put("timestamp", df.format(event.eventTimestamp))
                            val data = mapper.createObjectNode()
                            for ((key, value) in event.data) {
                                data.put(key, value)
                            }
                            node.set<JsonNode>("data", data)
                            val jsonString = node.toString()
                            bw.write(jsonString)
                            bw.newLine()
                        }
                    }
                    bw.flush()
                }
            }
        } catch (ex: IOException) {
            return false
        }
        return true
    }
}