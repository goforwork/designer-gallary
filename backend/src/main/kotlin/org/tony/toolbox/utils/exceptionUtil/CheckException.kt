package org.tony.toolbox.utils.exceptionUtil


/**
 * 检查异常
 */
class CheckException(
    val ec: Int,
    val em: String,
    val data: Any? = null
) : Exception(em)