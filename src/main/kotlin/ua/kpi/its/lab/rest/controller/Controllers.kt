package ua.kpi.its.lab.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.rest.dto.JournalRequest
import ua.kpi.its.lab.rest.dto.JournalResponse
import ua.kpi.its.lab.rest.svc.JournalService

@RestController
@RequestMapping("/journals")
class JournalController @Autowired constructor(
    private val journalService: JournalService
) {
    @GetMapping(path = ["", "/"])
    fun journals(): List<JournalResponse> = journalService.read()

    @GetMapping("{id}")
    fun readJournal(@PathVariable("id") id: Long): ResponseEntity<JournalResponse> {
        return wrapNotFound { journalService.readById(id) }
    }

    @PostMapping(path = ["", "/"])
    fun createJournal(@RequestBody journal: JournalRequest): JournalResponse {
        return journalService.create(journal)
    }

    @PutMapping("{id}")
    fun updateJournal(
        @PathVariable("id") id: Long,
        @RequestBody journal: JournalRequest
    ): ResponseEntity<JournalResponse> {
        return wrapNotFound { journalService.updateById(id, journal) }
    }

    @DeleteMapping("{id}")
    fun deleteJournal(
        @PathVariable("id") id: Long
    ): ResponseEntity<JournalResponse> {
        return wrapNotFound { journalService.deleteById(id) }
    }

    fun <T> wrapNotFound(call: () -> T): ResponseEntity<T> {
        return try {
            val result = call()
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
}
