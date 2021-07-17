package com.demo.eshop.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@Profile("default")
open class SwaggerConfig(private val swaggerEnvConfiguration: SwaggerEnvConfiguration) {

    @Bean
    open fun api(): Docket {
        val error500 = ResponseMessageBuilder().code(500).message("Internal Server Error")
            .responseModel(null).build()
        val error403 = ResponseMessageBuilder().code(403).message("Forbidden Request").build()
        val errorCodeDescriptions: MutableList<ResponseMessage> = ArrayList()
        errorCodeDescriptions.add(error500)
        errorCodeDescriptions.add(error403)
        return Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, errorCodeDescriptions)
            .globalResponseMessage(RequestMethod.POST, errorCodeDescriptions)
            .globalResponseMessage(RequestMethod.PUT, errorCodeDescriptions)
            .globalResponseMessage(RequestMethod.DELETE, errorCodeDescriptions).select()
            .apis(RequestHandlerSelectors.basePackage("com.demo.eshop.controller")).paths(PathSelectors.any()).build()
            .apiInfo(metaData())
    }

    private fun metaData(): ApiInfo {
        val title = swaggerEnvConfiguration.title
        val description = swaggerEnvConfiguration.description
        val version = swaggerEnvConfiguration.version
        val licenseTitle = swaggerEnvConfiguration.licenseTitle
        val licenseUrl = swaggerEnvConfiguration.licenseUrl
        val contactName = swaggerEnvConfiguration.contactName
        val contactUrl = swaggerEnvConfiguration.contactUrl
        val contactEmail = swaggerEnvConfiguration.contactEmail
        val contact = Contact(contactName, contactUrl, contactEmail)
        return ApiInfoBuilder().title(title).description(description).version(version).license(licenseTitle)
            .licenseUrl(licenseUrl).contact(contact).build()
    }
}
