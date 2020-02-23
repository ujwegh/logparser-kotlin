package ru.nik.logparser.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.nik.logparser.util.FileParser
import java.io.File
import java.util.logging.Logger

@Service
class BackgroundFileProcessor {
    companion object {
        val log: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    @Value("\${in_folder}")
    val inFolder: String = ""

    @Value("\${out_folder}")
    val outFolder: String = ""

    var processedFiles: List<String> = emptyList()

    @Scheduled(fixedDelay = 1000)
    private fun processFiles() {
        val inFolderFile = File(inFolder)
        var count = 0
        val start: Long = System.currentTimeMillis()
        inFolderFile.listFiles()?.forEach { file: File ->
            if (processedFiles.contains(file.name)) return@forEach
            if (FileParser().parse(inFolder, outFolder, file.name, EventService().listEventTypes())) {
                processedFiles = processedFiles + file.name;
                count++
            }
        }
        val end: Long = System.currentTimeMillis()
        val mSec: Long = end - start

        if (count > 0) log.info("Processed $count files in $mSec mSec")
    }
}