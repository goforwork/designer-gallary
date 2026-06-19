package org.tony.toolbox.designer.bean.param


/**
 * 创建 - 作品参数
 */
data class CreateWorkParam(
    val title: String,
    val slug: String,
    val categoryId: String? = null,
    val coverImage: String? = null,
    val images: List<String>? = null,
    val description: String? = null,
    val tags: List<String>? = null,
    val sortOrder: Int? = null,
    val isVisible: Boolean? = null,
)
