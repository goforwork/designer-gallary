# 设计师作品网站

一个简洁的设计师作品集网站，包含用户端（响应式）与管理后台。

## 项目结构

```
designer-gallary/
├── backend/                    Spring Boot 4 + Kotlin 后端
│   ├── src/main/kotlin/org/tony/toolbox/designer/
│   ├── src/main/resources/designer/   # 种子数据
│   ├── designerWebDist/        用户端构建产物
│   └── designerAdminDist/      管理后台构建产物
├── web/
│   ├── designer-web/           React + Vite + Tailwind 用户端
│   └── designer-admin/         Vue 3 + Vite + Tailwind 管理后台
└── scripts/                    构建与部署脚本
```

## 技术栈

- **后端**：Spring Boot 4、Kotlin 2.2、Java 21、Fastjson2
- **用户端**：React 19、TypeScript、Vite、Tailwind CSS、GSAP
- **管理后台**：Vue 3、TypeScript、Vite、Tailwind CSS、Pinia
- **数据存储**：JSON 文件系统（无数据库，便于维护）

## 功能

### 用户端

- 首页：Hero 展示区、作品网格、分类筛选、页脚联系方式
- 分类列表页：按分类浏览作品
- 作品详情页：大图展示、描述、相关作品推荐

### 管理后台

- 管理员登录/登出
- 作品增删改查、上架/下架、排序
- 分类管理
- 站点配置（品牌名、联系方式、社交媒体、Hero 文案、管理员账号）
- 图片上传

## 开发

```bash
# 启动后端（默认 test 配置，端口 8081）
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=test

# 启动用户端（端口 5173）
cd web/designer-web
npm install
npm run dev

# 启动管理后台（端口 5174）
cd web/designer-admin
npm install --legacy-peer-deps
npm run dev
```

## 管理后台初始账号

首次启动时，系统会从种子数据自动创建默认管理员账号：

- 用户名：`admin`
- 密码：`admin123`

登录后可在 **管理后台 → 站点设置 → 账号设置** 中修改用户名和密码。

## 生产构建

```bash
./scripts/build.sh
```

构建产物：

- 后端 JAR：`backend/target/toolbox-0.0.1-SNAPSHOT.jar`
- 用户端静态资源：`backend/designerWebDist/`
- 管理后台静态资源：`backend/designerAdminDist/`

## 部署到服务器

### 1. 准备服务器信息（首次部署前必须询问用户）

部署脚本只需要知道以下 3 项信息：

- 服务器公网 IP
- 服务器登录用户名（如 `ubuntu`、`root`）
- 服务器登录密码

> 其他路径（应用目录、数据目录、日志目录）和 SSH 端口都已使用默认配置，不需要询问。

### 2. 创建 .env 文件

```bash
cp .env.example .env
```

编辑 `.env`，填写服务器信息。示例：

```bash
SERVER_HOST=your-server-ip
SERVER_USER=ubuntu
SERVER_PASSWORD=your-server-password
```

> ⚠️ `.env` 文件包含敏感信息，**请勿提交到 Git**。

### 3. 安装 sshpass（仅首次）

部署脚本通过 `sshpass` 使用密码自动登录服务器。如果系统未安装，请根据系统选择以下命令安装：

```bash
# macOS
brew install sshpass

# Ubuntu
sudo apt-get install sshpass

# CentOS
sudo yum install sshpass
```

### 4. 一键部署

```bash
./scripts/deploy.sh
```

部署脚本会自动完成：本地构建 → 上传 JAR 与静态资源 → 远程重启应用。

### 5. 访问

部署完成后：

- 用户端：http://<SERVER_HOST>/
- 管理后台：http://<SERVER_HOST>/admin/
- 默认管理员账号：`admin / admin123`

首次登录后，建议立即修改管理员密码。

## 数据目录

默认数据存储在用户主目录：

- macOS/Linux：`${user.home}/.toolbox/designer-gallery/`
- 生产环境：可通过 `DESIGNER_DATA_DIR` 环境变量指定

目录内容：

```
${data-dir}/
├── works.json
├── categories.json
├── site-config.json
└── uploads/works/
```

## 注意事项

- 用户端使用 BrowserRouter，后端已配置 SPA 回退，支持直接刷新与深链接。
- 首次启动会自动生成种子数据（示例作品、分类、站点配置、默认管理员账号）。
- 图片上传限制：jpg/png/webp/gif，最大 5MB。

## 设计师用 AI 部署避坑指南

> 以下经验来自真实部署踩坑记录，供让 AI 帮你初始化项目时参考。

### 1. 服务器上必须先有 Java

后端基于 Java 21 运行。如果服务器是干净的 Ubuntu，需要先安装：

```bash
sudo apt-get update
sudo apt-get install -y openjdk-21-jre-headless
```

> 坑：部署脚本上传文件成功，但启动时报 `java: command not found`，导致网站无法访问。

### 2. 云服务器安全组必须放行 HTTP 端口

腾讯云、阿里云等云厂商默认安全组通常**只开放 22 端口**（SSH）。部署前或部署后，需要在控制台的安全组里放行 **TCP 80 端口**。

- 如果访问 `http://<IP>` 长时间无响应 → 大概率是安全组没开 80 端口
- 如果访问 `http://<IP>` 立即提示 "Connection refused" → 可能是服务没启动

> 坑：服务已在服务器本地 `localhost:8081` 正常运行，但公网访问超时，排查很久才发现是安全组没放行。

### 3. 部署目录不要用 `/opt`

普通用户（如 `ubuntu`）通常没有 `/opt` 写入权限。本项目默认使用用户 home 目录：

```
/home/ubuntu/designer-gallery/app
```

> 坑：第一次部署选 `/opt/designer-gallery/app`，导致 `mkdir` 报 `Permission denied`，文件上传失败。

### 4. 线上环境用 80 端口

本地开发用 `8081` 端口，但线上正式访问应该用 **80 端口**，这样用户访问时不需要手动输入 `:8081`。

本项目线上配置已默认使用 80 端口。如果服务器之前没开放 80，参考第 2 条在安全组放行。

> 坑：本地测试用 `8081` 没问题，但公网 8081 被安全组拦截；改成 80 后还需要给 Java 加 `cap_net_bind_service` 权限才能以普通用户绑定 80 端口。

### 5. `.env` 中的密码含特殊字符要加单引号

如果服务器密码包含反引号 `` ` ``、美元符号 `$` 等，建议用单引号包裹：

```bash
SERVER_PASSWORD='Jqgd7N-`9Y8%'
```

> 坑：密码中的反引号被 shell 解释为命令替换，导致登录失败。

### 6. macOS 用户可能需要安装 `sshpass`

`deploy.sh` 会优先尝试 `sshpass`，如果没有安装则自动使用系统自带的 `expect`。若你遇到提示，可按提示安装：

```bash
brew install sshpass
```

### 7. 部署后检查清单

如果部署脚本显示成功但网站打不开，按顺序检查：

1. 服务器上 Java 是否安装：`ssh 到服务器执行 java -version`
2. 服务是否在运行：`ssh 到服务器执行 ps aux | grep java`
3. 安全组是否放行 80 端口
4. 本地浏览器访问：`http://<SERVER_HOST>/`

### 8. 让 AI 帮你排查时的最佳提问方式

把以下信息直接丢给 AI：

- 服务器 IP、用户名、密码
- 运行 `./scripts/deploy.sh` 的完整输出
- 浏览器访问时的具体现象（超时 / 拒绝连接 / 404 / 空白页）
- 服务器上 `app.log` 的最后几行
