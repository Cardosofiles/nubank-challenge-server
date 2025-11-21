# 🏦 Nubank Challenge Server

## 📋 Sumário Executivo

Aplicação backend desenvolvida em **Java 17** com **Spring Boot 3.x**, implementando uma **API RESTful** completa para gerenciamento de transações financeiras. O projeto utiliza **PostgreSQL** como banco de dados relacional e está preparado para execução em containers **Docker**.

### Características Técnicas Principais

- ✅ Arquitetura REST seguindo princípios Richardson Maturity Model (Level 2)
- ✅ Persistência JPA/Hibernate com PostgreSQL
- ✅ Validação de dados com Bean Validation (JSR 380)
- ✅ Tratamento centralizado de exceções
- ✅ Configuração externalizada (12-factor app)
- ✅ Containerização com Docker
- ✅ Logs estruturados

---

## 🎯 Objetivo do Projeto

Este projeto foi desenvolvido como resposta a um desafio técnico, simulando um sistema de autorização de transações financeiras. A API expõe endpoints para:

1. **Criar contas** de usuários
2. **Realizar transações** entre contas
3. **Consultar saldos** e histórico
4. **Validar regras de negócio** (saldo suficiente, limites, etc.)

---

## 🏗️ Arquitetura da Aplicação

### Camadas da Aplicação (Layered Architecture)

```
┌─────────────────────────────────────┐
│   Controllers (REST Endpoints)      │  ← Camada de Apresentação
├─────────────────────────────────────┤
│   Services (Regras de Negócio)      │  ← Camada de Aplicação
├─────────────────────────────────────┤
│   Repositories (Spring Data JPA)    │  ← Camada de Persistência
├─────────────────────────────────────┤
│   Entities (Modelo de Domínio)      │  ← Camada de Domínio
├─────────────────────────────────────┤
│   PostgreSQL Database                │  ← Camada de Dados
└─────────────────────────────────────┘
```

### Padrões de Design Utilizados

- **DTO Pattern**: Separação entre entidades de domínio e objetos de transferência
- **Repository Pattern**: Abstração do acesso a dados via Spring Data JPA
- **Service Layer**: Encapsulamento da lógica de negócio
- **Dependency Injection**: Inversão de controle via Spring IoC Container

---

## 🔧 Pré-requisitos

### Ambiente de Desenvolvimento

- **JDK 17+** (OpenJDK ou Oracle JDK)
- **Maven 3.8+** (gerenciador de dependências)
- **Docker 20+** e **Docker Compose 2+** (opcional, mas recomendado)
- **PostgreSQL 14+** (caso não use Docker)
- **IDE recomendada**: IntelliJ IDEA, Eclipse ou VS Code com extensões Java

### Verificando Instalações

```bash
java -version    # Deve exibir versão 17+
mvn -version     # Deve exibir versão 3.8+
docker --version # Deve exibir versão 20+
```

---

## 🚀 Como Executar Localmente

### Opção 1: Usando Docker (Recomendado)

#### Passo 1: Subir o Banco de Dados PostgreSQL

```bash
docker run --name nubank-postgres \
  -e POSTGRES_DB=nubankdb \
  -e POSTGRES_USER=nubank_user \
  -e POSTGRES_PASSWORD=nubank_pass \
  -p 5432:5432 \
  -d postgres:15-alpine
```

**Explicação dos parâmetros:**

- `--name`: Nome do container para fácil referência
- `-e`: Variáveis de ambiente (credenciais e nome do banco)
- `-p`: Mapeamento de porta (host:container)
- `-d`: Execução em background (detached mode)
- `postgres:15-alpine`: Imagem otimizada (menor tamanho)

#### Passo 2: Construir a Aplicação

```bash
# Limpar builds anteriores e compilar
mvn clean package -DskipTests

# Ou com testes
mvn clean package
```

#### Passo 3: Executar a Aplicação

```bash
# Via Maven (desenvolvimento)
mvn spring-boot:run

# Ou via JAR (produção-like)
java -jar target/nubank-challenge-server-0.0.1-SNAPSHOT.jar
```

### Opção 2: Docker Compose (Mais Simples)

Crie um arquivo `docker-compose.yml` na raiz:

```yaml
version: "3.8"

services:
  postgres:
    image: postgres:15-alpine
    container_name: nubank-postgres
    environment:
      POSTGRES_DB: nubankdb
      POSTGRES_USER: nubank_user
      POSTGRES_PASSWORD: nubank_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - nubank-network

  app:
    build: .
    container_name: nubank-app
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/nubankdb
      SPRING_DATASOURCE_USERNAME: nubank_user
      SPRING_DATASOURCE_PASSWORD: nubank_pass
    ports:
      - "8080:8080"
    networks:
      - nubank-network

volumes:
  postgres_data:

networks:
  nubank-network:
    driver: bridge
```

**Executar:**

```bash
docker-compose up -d
```

---

## 📡 Testando a API

### Endpoints Disponíveis (Exemplos)

```bash
# Health Check
curl http://localhost:8080/actuator/health

# Criar Conta
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "document": "12345678900",
    "initialBalance": 1000.00
  }'

# Realizar Transação
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 150.00,
    "description": "Pagamento"
  }'

# Consultar Saldo
curl http://localhost:8080/api/accounts/1/balance
```

---

## 📚 Explicação Didática: `application.properties`

O arquivo `application.properties` é o **coração das configurações** da aplicação Spring Boot. Ele define como a aplicação se conecta ao banco, se comporta e expõe recursos.

### Localização

```
src/main/resources/application.properties
```

### Configurações Detalhadas

#### 1️⃣ Configuração do Banco de Dados

```properties
# URL de conexão JDBC (protocolo + host + porta + database)
spring.datasource.url=jdbc:postgresql://localhost:5432/nubankdb

# Credenciais de acesso
spring.datasource.username=nubank_user
spring.datasource.password=nubank_pass

# Driver JDBC (autodetectado pelo Spring Boot)
spring.datasource.driver-class-name=org.postgresql.Driver
```

**📖 Explicação:**

- `jdbc:postgresql://`: Protocolo JDBC para PostgreSQL
- `localhost:5432`: Host e porta onde o Postgres está rodando
- `/nubankdb`: Nome do banco de dados
- Credenciais devem ser **externalizadas** em produção (variáveis de ambiente)

#### 2️⃣ Configuração JPA/Hibernate

```properties
# Gerenciamento automático do schema
spring.jpa.hibernate.ddl-auto=update

# Dialeto SQL específico do PostgreSQL
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Exibir SQL no console (apenas desenvolvimento)
spring.jpa.show-sql=true

# Formatar SQL para melhor legibilidade
spring.jpa.properties.hibernate.format_sql=true

# Estratégia de nomeação de tabelas/colunas
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

**📖 Explicação do `ddl-auto`:**

- `none`: Não faz nada (produção)
- `validate`: Apenas valida o schema
- `update`: Atualiza schema sem perder dados (desenvolvimento)
- `create`: Recria schema toda vez (testes)
- `create-drop`: Recria e deleta ao finalizar (testes integração)

**⚠️ IMPORTANTE:** Em **produção**, sempre use `validate` ou `none` e gerencie migrations com **Flyway** ou **Liquibase**.

#### 3️⃣ Configuração do Servidor

```properties
# Porta HTTP
server.port=8080

# Context path da aplicação
server.servlet.context-path=/

# Timeout de sessão
server.servlet.session.timeout=30m
```

#### 4️⃣ Configuração de Logs

```properties
# Nível de log da aplicação
logging.level.root=INFO
logging.level.com.nubank=DEBUG

# Nível de log do Hibernate
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Arquivo de log
logging.file.name=logs/application.log
```

#### 5️⃣ Profiles (Ambientes)

```properties
# Profile ativo (dev, test, prod)
spring.profiles.active=dev
```

**📖 Como usar:**

- Crie `application-dev.properties`, `application-prod.properties`
- Sobrescreve configurações base
- Ative via variável: `SPRING_PROFILES_ACTIVE=prod`

#### 6️⃣ Variáveis de Ambiente (12-Factor App)

**Melhor prática:**

```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/nubankdb}
spring.datasource.username=${DATABASE_USERNAME:nubank_user}
spring.datasource.password=${DATABASE_PASSWORD:nubank_pass}
```

**Executar com variáveis:**

```bash
export DATABASE_URL=jdbc:postgresql://prod-db:5432/nubank
export DATABASE_USERNAME=prod_user
export DATABASE_PASSWORD=super_secret_pass
java -jar app.jar
```

---

## 📦 Explicação Didática: `pom.xml`

O `pom.xml` (Project Object Model) é o arquivo de configuração do **Maven**, gerenciador de dependências e build do Java.

### Estrutura Hierárquica

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <!-- Metadados do Projeto -->
    <!-- Parent (Herança) -->
    <!-- Propriedades -->
    <!-- Dependências -->
    <!-- Build (Plugins) -->
</project>
```

### 1️⃣ Parent (Herança Spring Boot)

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
    <relativePath/>
</parent>
```

**📖 Explicação:**

- **Parent POM**: Herda configurações padrão do Spring Boot
- **Benefícios**:
  - Versões de dependências pré-testadas
  - Plugins Maven pré-configurados
  - Propriedades padrão (encoding UTF-8, Java version)

### 2️⃣ Propriedades do Projeto

```xml
<properties>
    <!-- Versão do Java -->
    <java.version>17</java.version>

    <!-- Encoding padrão -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

    <!-- Versões de dependências customizadas -->
    <lombok.version>1.18.30</lombok.version>
</properties>
```

### 3️⃣ Dependências Essenciais

#### Spring Boot Starters

```xml
<dependencies>
    <!-- 1. Web - API REST -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <!-- Inclui: Tomcat, Jackson, Spring MVC, Validation -->
    </dependency>

    <!-- 2. Data JPA - Persistência -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <!-- Inclui: Hibernate, Spring Data JPA, JDBC -->
    </dependency>

    <!-- 3. Validation - Bean Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
        <!-- Inclui: Hibernate Validator, JSR 380 -->
    </dependency>

    <!-- 4. Actuator - Monitoramento -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
        <!-- Endpoints: /health, /metrics, /info -->
    </dependency>
</dependencies>
```

**📖 O que são Starters?**

- Coleções de dependências agrupadas por funcionalidade
- Reduzem boilerplate de configuração
- Garantem compatibilidade entre versões

#### Driver PostgreSQL

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
    <!-- JDBC Driver específico do PostgreSQL -->
</dependency>
```

**📖 Scope `runtime`:**

- Necessário apenas em tempo de execução
- Não é incluído no classpath de compilação

#### Ferramentas de Desenvolvimento

```xml
<!-- Lombok - Reduz Boilerplate -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
    <!-- Gera getters, setters, constructors via annotations -->
</dependency>

<!-- DevTools - Hot Reload -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

#### Dependências de Teste

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <!-- Inclui: JUnit 5, Mockito, AssertJ, Hamcrest -->
</dependency>

<!-- H2 Database - Testes em memória -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### 4️⃣ Build e Plugins

```xml
<build>
    <finalName>nubank-challenge-server</finalName>

    <plugins>
        <!-- Plugin principal Spring Boot -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>

        <!-- Compilador Java -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**📖 O que faz o `spring-boot-maven-plugin`?**

- Empacota aplicação em **fat JAR** (todas dependências inclusas)
- Permite executar via `mvn spring-boot:run`
- Configura manifest para execução standalone

---

## 🐳 Dockerfile (Exemplo Multi-Stage)

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**📖 Por que Multi-Stage?**

- Reduz tamanho final da imagem (JRE vs JDK)
- Separação entre ambiente de build e runtime
- Imagem final: ~200MB vs ~800MB

---

## 🔒 Boas Práticas e Segurança

### ✅ Configurações de Produção

1. **Nunca versione credenciais:**

```bash
# .gitignore
application-prod.properties
.env
```

2. **Use Secrets Manager:**

```properties
# AWS Secrets Manager, Vault, etc.
spring.cloud.aws.secretsmanager.enabled=true
```

3. **Habilite HTTPS:**

```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${KEYSTORE_PASSWORD}
```

4. **Configure CORS corretamente:**

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://frontend.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

### ✅ Monitoramento e Observabilidade

```properties
# Actuator endpoints (produção)
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized

# Métricas customizadas
management.metrics.export.prometheus.enabled=true
```

### ✅ Migrations com Flyway

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.jpa.hibernate.ddl-auto=validate
```

**Estrutura:**

```
src/main/resources/
└── db/migration/
    ├── V1__create_accounts_table.sql
    ├── V2__create_transactions_table.sql
    └── V3__add_indexes.sql
```

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/
├── unit/           # Testes unitários (Mockito)
├── integration/    # Testes de integração (Testcontainers)
└── e2e/            # Testes end-to-end (MockMvc)
```

### Exemplo de Teste de Integração

```java
@SpringBootTest
@Testcontainers
class AccountServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldCreateAccount() {
        // test implementation
    }
}
```

---

## 📊 Comandos Maven Úteis

```bash
# Limpar builds anteriores
mvn clean

# Compilar código
mvn compile

# Executar testes
mvn test

# Empacotar JAR (com testes)
mvn package

# Empacotar sem testes
mvn package -DskipTests

# Instalar no repositório local
mvn install

# Executar aplicação
mvn spring-boot:run

# Executar com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Análise de dependências
mvn dependency:tree

# Atualizar dependências
mvn versions:display-dependency-updates
```

---

## 🎓 Recursos de Aprendizado

### Documentação Oficial

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

### Livros Recomendados

- "Spring in Action" - Craig Walls
- "Spring Boot: Up and Running" - Mark Heckler
- "Java Persistence with Hibernate" - Christian Bauer

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch: `git checkout -b feature/nova-funcionalidade`
3. Commit suas mudanças: `git commit -m 'Adiciona nova funcionalidade'`
4. Push para branch: `git push origin feature/nova-funcionalidade`
5. Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 👨‍💻 Autor

Desenvolvido como parte do Nubank Challenge

**Contato:** [seu-email@exemplo.com](mailto:seu-email@exemplo.com)

---

## 🆘 Troubleshooting

### Problema: Porta 8080 já está em uso

```bash
# Descobrir processo usando a porta
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Matar processo
kill -9 <PID>

# Ou mudar porta da aplicação
server.port=8081
```

### Problema: Conexão recusada ao PostgreSQL

```bash
# Verificar se container está rodando
docker ps

# Verificar logs do Postgres
docker logs nubank-postgres

# Testar conexão manual
psql -h localhost -U nubank_user -d nubankdb
```

### Problema: OutOfMemoryError

```bash
# Aumentar heap da JVM
java -Xmx512m -Xms256m -jar app.jar
```

---

**🎉 Fim do README - Happy Coding!**
