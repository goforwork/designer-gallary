package org.tony.toolbox.utils.errorUtil

/**
 * 错误码
 */
enum class ErrorCode (
    val ec: Int,
    val em: String,
    val elegantMsg: String? = null
) {
    SUCCESS(0, "success", "成功"),
    SYSTEM_ERROR(1000, "system error", "系统错误"),
    PARAMETER_ERROR(1001, "parameter error", "参数错误"),
    DATA_NOT_FOUND(1002, "data not found", "数据不存在"),
    DATA_ALREADY_EXISTS(1003, "data already exists", "数据已存在"),
    DATA_INVALID(1004, "data invalid", "数据无效"),
    DATA_EXPIRED(1005, "data expired", "数据过期"),
    DATA_UPDATED(1006, "data updated", "数据更新"),
    DATA_DELETED(1007, "data deleted", "数据删除"),
    UNKNOWN_ERROR(1008, "unknown error", "未知错误"),
    UNAUTHORIZED(1009, "unauthorized", "未登录或登录已过期"),
    FORBIDDEN(1010, "forbidden", "无权限访问"),
}