package org.tony.toolbox.utils.exceptionUtil


/**
 * 低效影响异常
 */
class LowEfficientException(
    val ec: Int,
    val em: String,
    val data: Any? = null
) : Exception(em)