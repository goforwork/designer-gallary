package org.tony.toolbox.designer.bean.param


/**
 * 更新 - 分类参数
 */
data class UpdateCategoryParam(
    val name: String? = null,
    val slug: String? = null,
    val description: String? = null,
    val sortOrder: Int? = null,
    val isVisible: Boolean? = null,
)
