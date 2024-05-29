package ua.kpi.its.lab.rest.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import ua.kpi.its.lab.rest.dto.JournalRequest
import ua.kpi.its.lab.rest.svc.JournalService
import java.text.SimpleDateFormat

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val builder = Jackson2ObjectMapperBuilder()
            .indentOutput(true)
            .dateFormat(SimpleDateFormat("yyyy-MM-dd"))
            .modulesToInstall(KotlinModule.Builder().build())

        converters.add(MappingJackson2HttpMessageConverter(builder.build()))
    }

    @Bean
    fun functionalRoutes(journalService: JournalService): RouterFunction<*> = router {
        fun wrapNotFoundError(call: () -> Any): ServerResponse {
            return try {
                val result = call()
                ServerResponse.ok().body(result)
            } catch (e: IllegalArgumentException) {
                ServerResponse.notFound().build()
            }
        }

        "/fn".nest {
            "/journals".nest {
                GET("") {
                    ServerResponse.ok().body(journalService.read())
                }
                GET("/{id}") { req ->
                    val id = req.pathVariable("id").toLong()
                    wrapNotFoundError { journalService.readById(id) }
                }
                POST("") { req ->
                    val journal = req.body<JournalRequest>()
                    ServerResponse.ok().body(journalService.create(journal))
                }
                PUT("/{id}") { req ->
                    val id = req.pathVariable("id").toLong()
                    val journal = req.body<JournalRequest>()
                    wrapNotFoundError { journalService.updateById(id, journal) }
                }
                DELETE("/{id}") { req ->
                    val id = req.pathVariable("id").toLong()
                    wrapNotFoundError { journalService.deleteById(id) }
                }
            }
        }
    }
}
