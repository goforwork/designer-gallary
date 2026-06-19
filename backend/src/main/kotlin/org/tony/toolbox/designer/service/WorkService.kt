package org.tony.toolbox.designer.service

import org.springframework.stereotype.Service
import org.tony.toolbox.designer.bean.WorkBean
import org.tony.toolbox.designer.bean.param.CreateWorkParam
import org.tony.toolbox.designer.bean.param.UpdateWorkParam
import org.tony.toolbox.designer.dao.WorkFileDao
import org.tony.toolbox.utils.errorUtil.ErrorCode
import org.tony.toolbox.utils.exceptionUtil.CheckException
import java.time.Instant
import java.util.UUID


/**
 * 作品 - 业务服务
 */
@Service
class WorkService(
    private val workFileDao: WorkFileDao,
) {

    /**
     * 列表 - 作品（公开）
     */
    fun listPublicWorks(categoryId: String? = null): List<WorkBean> {
        var works = workFileDao.readVisible()
        if (!categoryId.isNullOrBlank()) {
            works = works.filter { it.categoryId == categoryId }
        }
        return works.sortedWith(compareBy({ it.sortOrder }, { it.createdAt }))
    }


    /**
     * 详情 - 作品（按 slug）
     */
    fun getPublicWork(slug: String): WorkBean {
        return workFileDao.readBySlug(slug)
            ?: throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "作品不存在")
    }


    /**
     * 列表 - 作品（管理后台）
     */
    fun listAllWorks(): List<WorkBean> {
        return workFileDao.readAll().sortedWith(compareByDescending<WorkBean> { it.isVisible }.thenBy { it.sortOrder }.thenBy { it.createdAt })
    }


    /**
     * 详情 - 作品（管理后台）
     */
    fun getWork(id: String): WorkBean {
        return workFileDao.readById(id)
            ?: throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "作品不存在")
    }


    /**
     * 创建 - 作品
     */
    fun createWork(param: CreateWorkParam): WorkBean {
        val works = workFileDao.readAll().toMutableList()
        if (works.any { it.slug == param.slug }) {
            throw CheckException(ErrorCode.DATA_ALREADY_EXISTS.ec, "作品标识已存在")
        }
        val now = Instant.now().toString()
        val work = WorkBean(
            id = UUID.randomUUID().toString(),
            title = param.title,
            slug = param.slug,
            categoryId = param.categoryId,
            coverImage = param.coverImage,
            images = param.images ?: emptyList(),
            description = param.description ?: "",
            tags = param.tags ?: emptyList(),
            sortOrder = param.sortOrder ?: 0,
            isVisible = param.isVisible ?: true,
            createdAt = now,
            updatedAt = now,
        )
        works.add(work)
        workFileDao.saveAll(works)
        return work
    }


    /**
     * 更新 - 作品
     */
    fun updateWork(id: String, param: UpdateWorkParam): WorkBean {
        val works = workFileDao.readAll().toMutableList()
        val index = works.indexOfFirst { it.id == id }
        if (index < 0) {
            throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "作品不存在")
        }
        val old = works[index]
        if (!param.slug.isNullOrBlank() && param.slug != old.slug && works.any { it.slug == param.slug }) {
            throw CheckException(ErrorCode.DATA_ALREADY_EXISTS.ec, "作品标识已存在")
        }
        val updated = old.copy(
            title = param.title ?: old.title,
            slug = param.slug ?: old.slug,
            categoryId = param.categoryId ?: old.categoryId,
            coverImage = param.coverImage ?: old.coverImage,
            images = param.images ?: old.images,
            description = param.description ?: old.description,
            tags = param.tags ?: old.tags,
            sortOrder = param.sortOrder ?: old.sortOrder,
            isVisible = param.isVisible ?: old.isVisible,
            updatedAt = Instant.now().toString(),
        )
        works[index] = updated
        workFileDao.saveAll(works)
        return updated
    }


    /**
     * 删除 - 作品
     */
    fun deleteWork(id: String) {
        val works = workFileDao.readAll().toMutableList()
        if (!works.removeIf { it.id == id }) {
            throw CheckException(ErrorCode.DATA_NOT_FOUND.ec, "作品不存在")
        }
        workFileDao.saveAll(works)
    }
}
