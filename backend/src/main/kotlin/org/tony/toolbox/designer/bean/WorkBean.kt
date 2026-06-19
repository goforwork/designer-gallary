package org.tony.toolbox.designer.bean


/**
 * 作品 - 领域对象
 */
data class WorkBean(
    /** 作品 ID */
    val id: String,

    /** 作品标题 */
    val title: String,

    /** URL 友好标识 */
    val slug: String,

    /** 分类 ID */
    val categoryId: String? = null,

    /** 封面图 URL */
    val coverImage: String? = null,

    /** 详情页图片列表 */
    val images: List<String> = emptyList(),

    /** 作品描述 */
    val description: String = "",

    /** 标签列表 */
    val tags: List<String> = emptyList(),

    /** 排序权重，数值越小越靠前 */
    val sortOrder: Int = 0,

    /** 是否可见 */
    val isVisible: Boolean = true,

    /** 创建时间（ISO-8601） */
    val createdAt: String,

    /** 更新时间（ISO-8601） */
    val updatedAt: String,
)
