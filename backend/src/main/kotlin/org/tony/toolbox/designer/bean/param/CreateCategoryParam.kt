package org.tony.toolbox.designer.bean.param


/**
 * 创建 - 分类参数
 */
data class CreateCategoryParam(
    val name: String,
    val slug: String,
    val description: String? = null,
    val sortOrder: Int? = null,
    val isVisible: Boolean? = null,
)
