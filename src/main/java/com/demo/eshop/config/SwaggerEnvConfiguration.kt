package com.demo.eshop.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("swagger")
class SwaggerEnvConfiguration {

    lateinit var version: String
    lateinit var title: String
    lateinit var description: String
    lateinit var license: Map<String, String>
    lateinit var contact: Map<String, String>

    val licenseTitle: String?
        get() = license["title"]
    val licenseUrl: String?
        get() = license["url"]
    val contactName: String?
        get() = contact["name"]
    val contactUrl: String?
        get() = contact["url"]
    val contactEmail: String?
        get() = contact["email"]
}
