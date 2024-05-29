package ua.kpi.its.lab.rest.svc

import ua.kpi.its.lab.rest.dto.JournalRequest
import ua.kpi.its.lab.rest.dto.JournalResponse

interface JournalService {
    fun create(journal: JournalRequest): JournalResponse
    fun read(): List<JournalResponse>
    fun readById(id: Long): JournalResponse
    fun updateById(id: Long, journal: JournalRequest): JournalResponse
    fun deleteById(id: Long): JournalResponse
}
