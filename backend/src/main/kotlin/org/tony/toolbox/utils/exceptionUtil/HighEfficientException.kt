package org.tony.toolbox.utils.exceptionUtil


/**
 * 高效影响异常
 */
class HighEfficientException(
    val ec: Int,
    val em: String,
    val data: Any? = null
) : Exception(em)