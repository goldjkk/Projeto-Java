# FEIFood (Swing + JDBC + PostgreSQL)

Aplicativo desktop estilo “cardápio + pedido” com **Java Swing**, **JDBC** e **PostgreSQL**, seguindo **MVC**.

## 🧰 Tecnologias
- Java 17 (ou 11+)
- Swing (NetBeans)
- JDBC (driver `postgresql-42.x.x.jar`)
- PostgreSQL 14+  
- Padrão **MVC**: `model/`, `dao/`, `controller/`, `view/`, `config/`

## 🚀 Como rodar
1. **Banco de dados**
   - Crie um banco (ex.: `feifood_db`) e rode o script `schema.sql` (este README traz o SQL).
   - Confirme que o `search_path` aponta para `feifood`.

2. **Configurar conexão**
   - Em `config/ConnectionFactory.java` ajuste:
     ```java
     private static final String URL  = "jdbc:postgresql://localhost:5432/feifood_db";
     private static final String USER = "postgres";
     private static final String PASS = "camisa2013"; // troque se necessário
     ```
   - Garanta que o driver `postgresql-42.x.x.jar` está em *Libraries* do projeto.

3. **Rodar**
   - `view.LoginFrame` → **Run** no NetBeans.

## 👤 Fluxo do usuário
- **Registrar** (nome, e-mail, senha) → persiste em `feifood.usuario`.
- **Login** → abre **MainFrame**.
- **Buscar alimentos** → lista do PostgreSQL (join com estabelecimento).
- **Meu Pedido**:
  - **Novo Pedido** → cria `feifood.pedido`.
  - **Adicionar/Remover** itens → tabela local.
  - **Salvar Itens** → `INSERT ... ON CONFLICT` em `feifood.pedido_item`.
  - **Excluir Pedido** → apaga itens, avaliação e o pedido.
  - **Avaliar (0–5)** → upsert em `feifood.avaliacao`.
- **Estatísticas**:
  - **Top 5 bem/mal avaliados** (média de `avaliacao` por alimento).
  - **Totais**: usuários, estabelecimentos e alimentos ativos.

## 🗂 Estrutura (resumo)
- config/ConnectionFactory.java
- controller/ (AuthController, PedidoController, EstatisticasController, ...)
- dao/ (interfaces)
- dao/impl (UsuarioDAOImpl, AlimentoDAOImpl, ...)
- model/ (Usuario, Estabelecimento, Alimento, Pedido, PedidoItem, Avaliacao)
- view/ (LoginFrame, MainFrame, PanelBuscaAlimentos, PanelPedido, PanelEstatisticas)


## 🧪 Testes manuais (checklist)
- Registrar e logar (senha errada → mensagem).
- Buscar com termo e vazio (lista tudo).
- Novo pedido → adicionar 2 itens → **Salvar** → verificar `pedido_item`.
- Avaliar (0–5) → conferir `avaliacao`.
- Estatísticas → atualizar ao abrir aba (Top5/Totais).

## 📝 Javadoc
No NetBeans: **Run → Generate Javadoc**. A pasta `dist/javadoc/` será criada.

## 📦 JAR
NetBeans: **Build → Clean and Build** → `dist/FEIFood.jar`.

