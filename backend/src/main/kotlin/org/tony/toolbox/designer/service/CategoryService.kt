package org.tony.toolbox.designer.service

import org.springframework.stereotype.Service
import org.tony.toolbox.designer.bean.CategoryBean
import org.tony.toolbox.designer.bean.param.CreateCategoryParam
import org.tony.toolbox.designer.bean.param.UpdateCategoryParam
import org.tony.toolbox.designer.dao.CategoryFileDao
import org.tony.toolbox.utils.errorUtil.ErrorCode
import org.tony.toolbox.utils.exceptionUtil.CheckException
import java.util.UUID


/**
 * 分类 - 业务服务
 */
@Service
class CategoryService(
    private val categoryFileDao: CategoryFileDao,
) {

    /**
     * 列表 - 分类（公开）
     */
    fun listPublicCategories(): List<CategoryBean> {
        return categoryFileDao.readVisible().sortedWith(compareBy({ it.sortOrder }, { it.name }))
    }


    /**
     * 详情 - 分类（按 slug，公开）
     */
    fun getPublicCategory(slug: String): CategoryBean {
        return categoryFileDao.readBySlug(slug)
            ?: throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "分类不存在")
    }


    /**
     * 列表 - 全部分类（管理后台）
     */
    fun listAllCategories(): List<CategoryBean> {
        return categoryFileDao.readAll().sortedWith(compareByDescending<CategoryBean> { it.isVisible }.thenBy { it.sortOrder }.thenBy { it.name })
    }


    /**
     * 详情 - 分类（管理后台）
     */
    fun getCategory(id: String): CategoryBean {
        return categoryFileDao.readById(id)
            ?: throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "分类不存在")
    }


    /**
     * 创建 - 分类
     */
    fun createCategory(param: CreateCategoryParam): CategoryBean {
        val categories = categoryFileDao.readAll().toMutableList()
        if (categories.any { it.slug == param.slug }) {
            throw CheckException(ErrorCode.DATA_ALREADY_EXISTS.ec, "分类标识已存在")
        }
        val category = CategoryBean(
            id = UUID.randomUUID().toString(),
            name = param.name,
            slug = param.slug,
            description = param.description ?: "",
            sortOrder = param.sortOrder ?: 0,
            isVisible = param.isVisible ?: true,
        )
        categories.add(category)
        categoryFileDao.saveAll(categories)
        return category
    }


    /**
     * 更新 - 分类
     */
    fun updateCategory(id: String, param: UpdateCategoryParam): CategoryBean {
        val categories = categoryFileDao.readAll().toMutableList()
        val index = categories.indexOfFirst { it.id == id }
        if (index < 0) {
            throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "分类不存在")
        }
        val old = categories[index]
        if (!param.slug.isNullOrBlank() && param.slug != old.slug && categories.any { it.slug == param.slug }) {
            throw CheckException(ErrorCode.DATA_ALREADY_EXISTS.ec, "分类标识已存在")
        }
        val updated = old.copy(
            name = param.name ?: old.name,
            slug = param.slug ?: old.slug,
            description = param.description ?: old.description,
            sortOrder = param.sortOrder ?: old.sortOrder,
            isVisible = param.isVisible ?: old.isVisible,
        )
        categories[index] = updated
        categoryFileDao.saveAll(categories)
        return updated
    }


    /**
     * 删除 - 分类
     */
    fun deleteCategory(id: String) {
        val categories = categoryFileDao.readAll().toMutableList()
        if (!categories.removeIf { it.id == id }) {
            throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "分类不存在")
        }
        categoryFileDao.saveAll(categories)
    }
}
