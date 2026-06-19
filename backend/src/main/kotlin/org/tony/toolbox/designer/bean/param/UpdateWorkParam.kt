package org.tony.toolbox.designer.bean.param


/**
 * 更新 - 作品参数
 */
data class UpdateWorkParam(
    val title: String? = null,
    val slug: String? = null,
    val categoryId: String? = null,
    val coverImage: String? = null,
    val images: List<String>? = null,
    val description: String? = null,
    val tags: List<String>? = null,
    val sortOrder: Int? = null,
    val isVisible: Boolean? = null,
)
