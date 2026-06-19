#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
ENV_FILE="${ROOT_DIR}/.env"

if [[ ! -f "${ENV_FILE}" ]]; then
    echo "错误：未找到 ${ENV_FILE}"
    echo "请复制 .env.example 为 .env 并填写服务器配置："
    echo "  cp .env.example .env"
    echo "然后编辑 .env，填写服务器 IP、用户名、密码。"
    exit 1
fi

# 安全读取 .env：按 key=value 解析，支持单引号包裹的值
read_env() {
    local key="$1"
    local value
    value="$(grep -E "^${key}=" "${ENV_FILE}" | head -1 | sed -E "s/^${key}=//")"
    # 去除首尾单引号
    value="${value#\'}"
    value="${value%\'}"
    echo "${value}"
}

SERVER_HOST="$(read_env SERVER_HOST)"
SERVER_USER="$(read_env SERVER_USER)"
SERVER_PASSWORD="$(read_env SERVER_PASSWORD)"
SERVER_PORT="$(read_env SERVER_PORT)"

# 默认值
SERVER_USER="${SERVER_USER:-ubuntu}"
SERVER_PORT="${SERVER_PORT:-22}"

# 固定默认路径，使用用户 home 目录避免权限问题
SERVER_BASE_DIR="/home/${SERVER_USER}/designer-gallery"
SERVER_APP_DIR="${SERVER_BASE_DIR}/app"
SERVER_DATA_DIR="${SERVER_BASE_DIR}/data"
SERVER_LOG_DIR="${SERVER_BASE_DIR}/logs"

if [[ -z "${SERVER_HOST}" ]]; then
    echo "错误：.env 中未设置 SERVER_HOST（服务器 IP）"
    exit 1
fi

if [[ -z "${SERVER_PASSWORD}" ]]; then
    echo "错误：.env 中未设置 SERVER_PASSWORD（服务器登录密码）"
    exit 1
fi

SSH_TARGET="${SERVER_USER}@${SERVER_HOST}"
SSH_OPTS="-p ${SERVER_PORT} -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null"
SCP_OPTS="-P ${SERVER_PORT} -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null"

# expect 通过环境变量读取密码，避免密码中的特殊字符被 bash 解释
export SERVER_PASSWORD

# 检测可用的密码登录工具
if command -v sshpass &> /dev/null; then
    USE_TOOL="sshpass"
elif command -v expect &> /dev/null; then
    USE_TOOL="expect"
else
    echo "错误：当前系统未安装 sshpass 或 expect，无法使用密码自动登录服务器。"
    echo ""
    echo "推荐安装 sshpass（最简单）："
    echo "  macOS:    brew install sshpass"
    echo "  Ubuntu:   sudo apt-get install sshpass"
    echo "  CentOS:   sudo yum install sshpass"
    echo ""
    echo "安装完成后重新运行 ./scripts/deploy.sh"
    exit 1
fi

# 执行远程命令
remote_exec() {
    local cmd="$1"
    if [[ "${USE_TOOL}" == "sshpass" ]]; then
        # shellcheck disable=SC2086
        sshpass -p "${SERVER_PASSWORD}" ssh ${SSH_OPTS} "${SSH_TARGET}" "${cmd}"
    else
        expect -c "
            set timeout 60
            set ssh_args [list -p ${SERVER_PORT} -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ${SSH_TARGET} ${cmd}]
            eval spawn ssh \$ssh_args
            expect {
                -nocase \"password:\" { send \"\$env(SERVER_PASSWORD)\r\"; exp_continue }
                eof
            }
        "
    fi
}

# 上传文件
remote_scp() {
    local src="$1"
    local dst="$2"
    if [[ "${USE_TOOL}" == "sshpass" ]]; then
        # shellcheck disable=SC2086
        sshpass -p "${SERVER_PASSWORD}" scp ${SCP_OPTS} "${src}" "${dst}"
    else
        expect -c "
            set timeout 120
            set scp_args [list -P ${SERVER_PORT} -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null \"${src}\" \"${dst}\"]
            eval spawn scp \$scp_args
            expect {
                -nocase \"password:\" { send \"\$env(SERVER_PASSWORD)\r\"; exp_continue }
                eof
            }
        "
    fi
}

# 上传目录（递归）
remote_scp_r() {
    local src="$1"
    local dst="$2"
    if [[ "${USE_TOOL}" == "sshpass" ]]; then
        # shellcheck disable=SC2086
        sshpass -p "${SERVER_PASSWORD}" scp ${SCP_OPTS} -r "${src}" "${dst}"
    else
        expect -c "
            set timeout 180
            set scp_args [list -P ${SERVER_PORT} -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r \"${src}\" \"${dst}\"]
            eval spawn scp \$scp_args
            expect {
                -nocase \"password:\" { send \"\$env(SERVER_PASSWORD)\r\"; exp_continue }
                eof
            }
        "
    fi
}

echo "=== 部署到 ${SSH_TARGET} ==="
echo "使用登录方式：${USE_TOOL}"

# 1. 本地构建
echo "[1/3] 本地构建..."
cd "${ROOT_DIR}"
"${ROOT_DIR}/scripts/build.sh"

# 2. 上传到服务器
echo "[2/3] 上传构建产物..."
remote_exec "mkdir -p ${SERVER_APP_DIR} ${SERVER_DATA_DIR} ${SERVER_LOG_DIR}"
remote_scp "${ROOT_DIR}/backend/target/toolbox-0.0.1-SNAPSHOT.jar" "${SSH_TARGET}:${SERVER_APP_DIR}/"
remote_scp "${ROOT_DIR}/backend/scripts/restart.sh" "${SSH_TARGET}:${SERVER_APP_DIR}/"
remote_scp_r "${ROOT_DIR}/backend/designerWebDist" "${SSH_TARGET}:${SERVER_APP_DIR}/"
remote_scp_r "${ROOT_DIR}/backend/designerAdminDist" "${SSH_TARGET}:${SERVER_APP_DIR}/"

# 3. 重启服务
echo "[3/3] 重启服务..."
remote_exec "cd ${SERVER_APP_DIR} && chmod +x restart.sh && bash ./restart.sh"

echo "=== 部署完成 ==="
echo "访问地址："
echo "  用户端：http://${SERVER_HOST}/"
echo "  管理后台：http://${SERVER_HOST}/admin/"
