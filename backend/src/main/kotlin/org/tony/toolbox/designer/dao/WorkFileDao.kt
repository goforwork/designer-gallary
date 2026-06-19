package org.tony.toolbox.designer.dao

import com.alibaba.fastjson2.JSON
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Repository
import org.tony.toolbox.designer.bean.WorkBean
import org.tony.toolbox.designer.config.DesignerStorageConfig
import org.tony.toolbox.utils.log.Log
import java.nio.charset.StandardCharsets
import java.nio.file.Files


/**
 * 作品 - 文件存储 DAO
 */
@Repository
class WorkFileDao(
    private val designerStorageConfig: DesignerStorageConfig,
) : JsonFileDao<List<WorkBean>>(
    filePath = { designerStorageConfig.worksFilePath },
    type = typeRef<List<WorkBean>>().type,
) {

    @PostConstruct
    fun init() {
        val path = designerStorageConfig.worksFilePath
        if (!Files.exists(path)) {
            writeAll(loadSeedWorks())
        }
    }


    /**
     * 读取 - 可见作品列表
     */
    @Log
    fun readVisible(): List<WorkBean> = readAll().filter { it.isVisible }


    /**
     * 读取 - 单条作品（按 ID）
     */
    @Log
    fun readById(id: String): WorkBean? = readAll().find { it.id == id }


    /**
     * 读取 - 单条作品（按 slug）
     */
    @Log
    fun readBySlug(slug: String): WorkBean? = readAll().find { it.slug == slug && it.isVisible }


    /**
     * 写入 - 全部作品
     */
    @Log
    fun saveAll(works: List<WorkBean>) = writeAll(works)


    override fun defaultValue(): List<WorkBean> = emptyList()


    private fun loadSeedWorks(): List<WorkBean> {
        return try {
            val resource = ClassPathResource("designer/works.seed.json")
            val json = resource.inputStream.use { it.readBytes().toString(StandardCharsets.UTF_8) }
            JSON.parseObject(json, typeRef<List<WorkBean>>().type)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
