package org.tony.toolbox.utils.exceptionUtil


/**
 * 无影响异常
 */
class NoEfficientException(
    val ec: Int,
    val em: String,
    val data: Any? = null
) : Exception(em)