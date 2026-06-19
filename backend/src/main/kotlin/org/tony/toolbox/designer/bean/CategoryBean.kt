package org.tony.toolbox.designer.bean


/**
 * 分类 - 领域对象
 */
data class CategoryBean(
    /** 分类 ID */
    val id: String,

    /** 分类名称 */
    val name: String,

    /** URL 友好标识 */
    val slug: String,

    /** 分类描述 */
    val description: String = "",

    /** 排序权重，数值越小越靠前 */
    val sortOrder: Int = 0,

    /** 是否可见 */
    val isVisible: Boolean = true,
)
