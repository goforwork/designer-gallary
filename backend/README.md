# Designer Gallery 后端

Spring Boot + Kotlin 实现的设计师作品网站后端。

## 数据存储

使用 JSON 文件系统，默认路径：`~/.toolbox/designer-gallery/`

可通过环境变量覆盖：

```bash
export DESIGNER_DATA_DIR=/opt/designer-gallery/data
```

## 运行

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

## 打包

```bash
./mvnw clean package -DskipTests
```

## 部署

请参考项目根目录 `README.md` 中的部署说明，并先在项目根目录创建 `.env` 配置文件。
