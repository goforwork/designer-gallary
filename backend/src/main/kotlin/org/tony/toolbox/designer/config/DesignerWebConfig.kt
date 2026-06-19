package org.tony.toolbox.designer.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import java.nio.file.Files
import java.nio.file.Paths


/**
 * Designer - 用户端 / 管理后台静态资源与 SPA 回退
 */
@Configuration
class DesignerWebConfig(
    @param:Value("\${designer.web-dist.user-dir}")
    private val userWebDistDir: String,
    @param:Value("\${designer.web-dist.admin-dir}")
    private val adminWebDistDir: String,
    private val designerStorageConfig: DesignerStorageConfig,
) : WebMvcConfigurer {
    private lateinit var userWebDistLocation: String
    private lateinit var adminWebDistLocation: String
    private lateinit var uploadsLocation: String


    @PostConstruct
    fun init() {
        val userPath = resolveAndEnsure(userWebDistDir)
        val adminPath = resolveAndEnsure(adminWebDistDir)
        val uploadsPath = designerStorageConfig.uploadsPath.toAbsolutePath().normalize()

        userWebDistLocation = toLocation(userPath)
        adminWebDistLocation = toLocation(adminPath)
        uploadsLocation = toLocation(uploadsPath)

        log.info("DesignerWebConfig.init | 用户端静态资源目录 | path: {}", userPath)
        log.info("DesignerWebConfig.init | 管理后台静态资源目录 | path: {}", adminPath)
        log.info("DesignerWebConfig.init | 上传资源目录 | path: {}", uploadsPath)

        warnIfMissingIndex(userPath, "用户端")
        warnIfMissingIndex(adminPath, "管理后台")
    }


    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("forward:/index.html")
        registry.addViewController("/admin").setViewName("forward:/admin/index.html")
        registry.addViewController("/admin/").setViewName("forward:/admin/index.html")
    }


    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 上传资源：/uploads/**
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(uploadsLocation)

        // 管理后台：/admin/**
        registry.addResourceHandler("/admin/**")
            .addResourceLocations(adminWebDistLocation)
            .resourceChain(true)
            .addResolver(SpaFallbackResolver())

        // 用户端：/**
        registry.addResourceHandler("/**")
            .addResourceLocations(userWebDistLocation)
            .resourceChain(true)
            .addResolver(SpaFallbackResolver())
    }


    private fun resolveAndEnsure(dir: String) = Paths.get(dir).toAbsolutePath().normalize()
        .also { Files.createDirectories(it) }


    private fun toLocation(path: java.nio.file.Path): String {
        var location = path.toUri().toString()
        if (!location.endsWith("/")) {
            location += "/"
        }
        return location
    }


    private fun warnIfMissingIndex(path: java.nio.file.Path, label: String) {
        if (!Files.exists(path.resolve("index.html"))) {
            log.warn(
                "DesignerWebConfig.init | {} index.html 不存在，请先执行前端构建：npm run build",
                label,
            )
        }
    }


    /**
     * SPA 回退解析器：找不到文件且非 API 请求时回退到 index.html
     */
    private class SpaFallbackResolver : PathResourceResolver() {
        override fun getResource(resourcePath: String, location: Resource): Resource? {
            if (resourcePath.isEmpty()) {
                return super.getResource("index.html", location)
            }
            if (resourcePath.startsWith("api/")) {
                return null
            }
            val requested = super.getResource(resourcePath, location)
            if (requested != null) {
                return requested
            }
            if (resourcePath.contains(".") && !resourcePath.endsWith(".html")) {
                return null
            }
            return super.getResource("index.html", location)
        }
    }


    companion object {
        private val log = LoggerFactory.getLogger(DesignerWebConfig::class.java)
    }
}
