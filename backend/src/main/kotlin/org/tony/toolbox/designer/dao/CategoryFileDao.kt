package org.tony.toolbox.designer.dao

import com.alibaba.fastjson2.JSON
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Repository
import org.tony.toolbox.designer.bean.CategoryBean
import org.tony.toolbox.designer.config.DesignerStorageConfig
import org.tony.toolbox.utils.log.Log
import java.nio.charset.StandardCharsets
import java.nio.file.Files


/**
 * 分类 - 文件存储 DAO
 */
@Repository
class CategoryFileDao(
    private val designerStorageConfig: DesignerStorageConfig,
) : JsonFileDao<List<CategoryBean>>(
    filePath = { designerStorageConfig.categoriesFilePath },
    type = typeRef<List<CategoryBean>>().type,
) {

    @PostConstruct
    fun init() {
        val path = designerStorageConfig.categoriesFilePath
        if (!Files.exists(path)) {
            writeAll(loadSeedCategories())
        }
    }


    /**
     * 读取 - 可见分类列表
     */
    @Log
    fun readVisible(): List<CategoryBean> = readAll().filter { it.isVisible }


    /**
     * 读取 - 单条分类（按 ID）
     */
    @Log
    fun readById(id: String): CategoryBean? = readAll().find { it.id == id }


    /**
     * 读取 - 单条分类（按 slug）
     */
    @Log
    fun readBySlug(slug: String): CategoryBean? = readAll().find { it.slug == slug && it.isVisible }


    /**
     * 写入 - 全部分类
     */
    @Log
    fun saveAll(categories: List<CategoryBean>) = writeAll(categories)


    override fun defaultValue(): List<CategoryBean> = emptyList()


    private fun loadSeedCategories(): List<CategoryBean> {
        return try {
            val resource = ClassPathResource("designer/categories.seed.json")
            val json = resource.inputStream.use { it.readBytes().toString(StandardCharsets.UTF_8) }
            JSON.parseObject(json, typeRef<List<CategoryBean>>().type)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
