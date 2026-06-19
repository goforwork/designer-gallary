#!/bin/bash
set -e

# 服务器端重启脚本

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="${SCRIPT_DIR}"
JAR_FILE="${APP_DIR}/toolbox-0.0.1-SNAPSHOT.jar"

if [[ ! -f "${JAR_FILE}" ]]; then
    echo "错误：未找到 JAR 文件 ${JAR_FILE}"
    exit 1
fi

# 数据目录与日志目录默认与应用同级
export DESIGNER_DATA_DIR="${APP_DIR}/data"
export DESIGNER_LOG_DIR="${APP_DIR}/logs"

mkdir -p "${DESIGNER_DATA_DIR}" "${DESIGNER_LOG_DIR}"

# 使用与 JAR 同名的 pid 文件
PID_FILE="${APP_DIR}/app.pid"

if [[ -f "${PID_FILE}" ]]; then
    OLD_PID="$(cat "${PID_FILE}")"
    if kill -0 "${OLD_PID}" 2>/dev/null; then
        echo "停止旧进程 ${OLD_PID}..."
        kill "${OLD_PID}"
        sleep 2
    fi
    rm -f "${PID_FILE}"
fi

echo "启动应用..."
echo "  应用目录: ${APP_DIR}"
echo "  数据目录: ${DESIGNER_DATA_DIR}"
echo "  日志目录: ${DESIGNER_LOG_DIR}"
nohup java -jar "${JAR_FILE}" --spring.profiles.active=online >"${APP_DIR}/app.log" 2>&1 &
echo $! >"${PID_FILE}"
echo "应用已启动，PID: $(cat "${PID_FILE}")"
