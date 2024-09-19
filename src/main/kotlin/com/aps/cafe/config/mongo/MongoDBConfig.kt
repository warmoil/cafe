package com.aps.cafe.config.mongo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Configuration
class MongoDBConfig {
    @Value("\${spring.data.mongodb.host}")
    private val host = ""

    @Value("\${spring.data.mongodb.port}")
    private val port = 0

    @Value("\${spring.data.mongodb.database}")
    private val db = ""

    private fun getConnectString() = "mongodb://$host:$port/$db"

    @Bean
    fun mongoDatabase(): MongoDatabaseFactory = SimpleMongoClientDatabaseFactory(getConnectString())

    @Bean
    fun mongoTemplate(): MongoTemplate {
        val mappingContext = MongoMappingContext()
        val converter = MappingMongoConverter(DefaultDbRefResolver(mongoDatabase()), mappingContext)
        converter.customConversions = customConversions()
        mappingContext.setSimpleTypeHolder(converter.customConversions.simpleTypeHolder)
        mappingContext.afterPropertiesSet()
        converter.afterPropertiesSet()

        return MongoTemplate(mongoDatabase(), converter)
    }

    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters: MutableList<Converter<*, *>> = ArrayList()
        converters.add(DateToZonedDateTimeConverter.INSTANCE)
        converters.add(ZonedDateTimeToDateConverter.INSTANCE)
        return MongoCustomConversions(converters)
    }

    enum class DateToZonedDateTimeConverter : Converter<Date, ZonedDateTime> {
        INSTANCE;

        override fun convert(source: Date): ZonedDateTime {
            return source.toInstant().atZone(ZoneId.of("Asia/Seoul"))
        }
    }

    enum class ZonedDateTimeToDateConverter : Converter<ZonedDateTime, Date> {
        INSTANCE;

        override fun convert(source: ZonedDateTime): Date {
            return Date.from(source.toInstant())
        }
    }
}