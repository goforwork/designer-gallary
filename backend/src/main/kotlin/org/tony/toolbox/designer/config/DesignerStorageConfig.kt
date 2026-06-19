package org.tony.toolbox.designer.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


/**
 * Designer - 文件存储路径配置
 */
@Configuration
class DesignerStorageConfig(
    @param:Value("\${designer.storage.data-dir}")
    private val dataDir: String,
) {
    /** 数据根目录 */
    lateinit var rootPath: Path

    /** 作品数据文件路径 */
    lateinit var worksFilePath: Path

    /** 分类数据文件路径 */
    lateinit var categoriesFilePath: Path

    /** 站点配置文件路径 */
    lateinit var siteConfigFilePath: Path

    /** 管理员用户文件路径 */
    lateinit var adminUserFilePath: Path

    /** 上传文件根目录 */
    lateinit var uploadsPath: Path

    /** 作品图片上传目录 */
    lateinit var workUploadsPath: Path


    @PostConstruct
    fun init() {
        rootPath = Paths.get(dataDir).toAbsolutePath().normalize()
        worksFilePath = rootPath.resolve("works.json")
        categoriesFilePath = rootPath.resolve("categories.json")
        siteConfigFilePath = rootPath.resolve("site-config.json")
        adminUserFilePath = rootPath.resolve("admin-user.json")
        uploadsPath = rootPath.resolve("uploads")
        workUploadsPath = uploadsPath.resolve("works")

        listOf(rootPath, uploadsPath, workUploadsPath).forEach { Files.createDirectories(it) }

        log.info(
            "DesignerStorageConfig.init | 数据目录初始化完成 | rootPath: {}",
            rootPath,
        )
    }


    companion object {
        private val log = LoggerFactory.getLogger(DesignerStorageConfig::class.java)
    }
}
