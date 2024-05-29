package ua.kpi.its.lab.rest.repo

import org.springframework.data.jpa.repository.JpaRepository
import ua.kpi.its.lab.rest.entity.Article
import ua.kpi.its.lab.rest.entity.Journal

interface JournalRepository : JpaRepository<Journal, Long>

interface ArticleRepository : JpaRepository<Article, Long>
