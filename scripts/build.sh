#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

echo "=== 构建设计师作品网站 ==="

# 构建用户端
echo "[1/4] 构建用户端..."
cd "${ROOT_DIR}/web/designer-web"
npm install
npm run build

# 构建管理后台
echo "[2/4] 构建管理后台..."
cd "${ROOT_DIR}/web/designer-admin"
npm install --legacy-peer-deps
npm run build

# 打包后端
echo "[3/4] 打包后端..."
cd "${ROOT_DIR}/backend"
./mvnw clean package -DskipTests

echo "[4/4] 构建完成"
echo "产物位置："
echo "  - 后端 JAR: ${ROOT_DIR}/backend/target/toolbox-0.0.1-SNAPSHOT.jar"
echo "  - 用户端静态资源: ${ROOT_DIR}/backend/designerWebDist/"
echo "  - 管理后台静态资源: ${ROOT_DIR}/backend/designerAdminDist/"
