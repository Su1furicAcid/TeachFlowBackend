# TeachflowBackend

## 介绍 (Introduction)

TeachflowBackend 是 Teachflow 项目的后端服务，一个旨在简化教育工作流程的平台。该服务提供了管理用户、课程和内容的 API，并利用 AI 技术增强功能。

API 文档：[http://60.205.57.52:8080/swagger-ui/index.html#/](http://60.205.57.52:8080/swagger-ui/index.html#/)

## 功能特性 (Features)

*   基于 JWT 的用户认证与授权
*   教育资源的增删改查 (CRUD) 操作
*   AI 驱动的内容搜索与生成 (集成 OpenAI)
*   文件上传与管理
*   集成 Google 自定义搜索以查找外部资源

## 技术栈 (Technologies Used)

*   **框架**: Spring Boot 3
*   **语言**: Java 17
*   **数据库**: PostgreSQL
*   **数据访问**: Spring Data JPA (Hibernate)
*   **安全**: Spring Security, JSON Web Tokens (JWT)
*   **AI**: Spring AI (OpenAI)
*   **API 文档**: SpringDoc (Swagger UI)
*   **容器化**: Docker, Docker Compose

## 快速开始 (Getting Started)

### 环境准备

*   安装 Docker 和 Docker Compose
*   安装 Java 17 或更高版本
*   拥有一个 DockerHub 账号

### 项目设置

1.  **克隆仓库:**
    ```bash
    git clone <your-repository-url>
    cd TeachflowBackend
    ```

2.  **创建 `.env` 环境文件:**

    在项目根目录下创建一个名为 `.env` 的文件，并填入以下环境变量。此文件已被 `.gitignore` 忽略，不会提交到版本库。

    ```env
    # DockerHub 用户名，用于拉取镜像
    DOCKERHUB_USERNAME=your-dockerhub-username

    # PostgreSQL 数据库凭据
    POSTGRES_DB=teachflow_db
    POSTGRES_USER=postgres
    POSTGRES_PASSWORD=password

    # Google 自定义搜索 API 凭据
    GOOGLE_API_KEY=your_google_api_key
    GOOGLE_SEARCH_ENGINE_ID=your_google_search_engine_id

    # OpenAI API 密钥
    SPRING_AI_OPENAI_API_KEY=your_openai_api_key
    ```

3.  **构建并运行应用:**

    使用 Docker Compose 构建并启动所有服务。

    ```bash
    docker-compose up -d --build
    ```

## API 文档 (API Documentation)

应用成功运行后，可以通过以下地址访问 API 文档：

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 环境变量 (Environment Variables)

本项目通过环境变量进行配置，以下是所需变量的详细说明：

| 变量名                      | 描述                                       | 示例值 / 默认值                |
| --------------------------- | ------------------------------------------ | ------------------------------ |
| `DOCKERHUB_USERNAME`        | 你的 DockerHub 用户名                      | `your-dockerhub-username`      |
| `POSTGRES_DB`               | PostgreSQL 数据库名称                      | `teachflow_db`                 |
| `POSTGRES_USER`             | PostgreSQL 用户名                          | `postgres`                     |
| `POSTGRES_PASSWORD`         | PostgreSQL 密码                            | `password`                     |
| `GOOGLE_API_KEY`            | Google 自定义搜索 API 密钥                 | `your_google_api_key`          |
| `GOOGLE_SEARCH_ENGINE_ID`   | Google 自定义搜索引擎 ID                   | `your_google_search_engine_id` |
| `SPRING_AI_OPENAI_API_KEY`  | OpenAI API 密钥                            | `your_openai_api_key`          |

## 项目结构 (Project Structure)

```
.
├── build.gradle                # Gradle 构建脚本
├── compose.yaml                # Docker Compose 生产环境配置
├── compose-local.yaml          # Docker Compose 本地开发配置
├── Dockerfile                  # 应用的 Dockerfile
├── gradlew                     # Gradle Wrapper
├── http-tests/                 # HTTP 测试文件
├── README.md                   # 项目说明文档
└── src/
    ├── main/
    │   ├── java/               # Java 源代码
    │   └── resources/          # 资源文件 (application.properties, data.sql)
    └── test/
        └── java/               # 测试代码
```
