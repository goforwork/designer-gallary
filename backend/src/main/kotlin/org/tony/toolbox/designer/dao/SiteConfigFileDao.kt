package org.tony.toolbox.designer.dao

import com.alibaba.fastjson2.JSON
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Repository
import org.tony.toolbox.designer.bean.SiteConfigBean
import org.tony.toolbox.designer.config.DesignerStorageConfig
import org.tony.toolbox.utils.log.Log
import java.nio.charset.StandardCharsets
import java.nio.file.Files


/**
 * 站点配置 - 文件存储 DAO
 */
@Repository
class SiteConfigFileDao(
    private val designerStorageConfig: DesignerStorageConfig,
) : JsonFileDao<SiteConfigBean>(
    filePath = { designerStorageConfig.siteConfigFilePath },
    type = SiteConfigBean::class.java,
) {

    @PostConstruct
    fun init() {
        val path = designerStorageConfig.siteConfigFilePath
        if (!Files.exists(path)) {
            writeAll(loadSeedConfig())
        }
    }


    /**
     * 读取 - 站点配置
     */
    @Log
    fun readConfig(): SiteConfigBean = readAll()


    /**
     * 写入 - 站点配置
     */
    @Log
    fun saveConfig(config: SiteConfigBean) = writeAll(config)


    override fun defaultValue(): SiteConfigBean = SiteConfigBean()


    private fun loadSeedConfig(): SiteConfigBean {
        return try {
            val resource = ClassPathResource("designer/site-config.seed.json")
            val json = resource.inputStream.use { it.readBytes().toString(StandardCharsets.UTF_8) }
            JSON.parseObject(json, SiteConfigBean::class.java)
        } catch (_: Exception) {
            SiteConfigBean()
        }
    }
}
