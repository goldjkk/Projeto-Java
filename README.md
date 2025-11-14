# FEIFood (Swing + JDBC + PostgreSQL)

Aplicativo desktop estilo **“cardápio + pedido”** feito em **Java Swing**, com **persistência no PostgreSQL** e organização simples por camadas.

## 🧰 Tecnologias
- Java 17 (ou 11+)
- NetBeans (projeto Swing)
- JDBC (driver `postgresql-42.x.x.jar`)
- PostgreSQL 14+
- Padrão “MVC básico”: `model/`, `dao/` + `dao.impl/`, `controller/`, `view/`, `config/`

## 🎯 O que o app faz
- **Cadastro e Login** (nome, e-mail, senha).
- **Buscar Alimentos** por nome (tabela somente leitura).
- **Meu Pedido**: novo pedido, adicionar/remover itens, **salvar** (upsert), **avaliar** (0–5) e **excluir**.
- **Estatísticas**: Top 5 **bem/mal avaliados** e **totais do sistema** (Usuários, Estabelecimentos, Alimentos). Atualiza ao entrar na aba e depois de salvar/avaliar.

## 🚀 Como rodar
- Ajuste `config/ConnectionFactory.java` com host, banco, usuário e senha do PostgreSQL.
- Tenha o schema **`feifood`** criado (ou use `SET search_path TO feifood;`).
- Abra no NetBeans e **execute `LoginFrame`**.
- **Registre** (nome, e-mail, senha) e depois **faça login**.

## 🗃️ Esquema (visão rápida)
- `usuario(id, nome, email, senha, criado_em)`
- `estabelecimento(id, nome, cnpj, cidade, ativo)`
- `alimento(id, estabelecimento_id, nome, descricao, preco, tipo, ativo)`
- `pedido(id, usuario_id, criado_em, status)`
- `pedido_item(pedido_id, alimento_id, quantidade, preco_unitario)` **PK composta**
- `avaliacao(pedido_id, estrelas, comentario, criado_em)` **1 por pedido (upsert)**

## 🧭 Organização do projeto
- `config/ -> ConnectionFactory (JDBC)`
-`controller/ -> Auth, Estatísticas`
-`dao/ + dao.impl/ -> Interfaces + SQL (JDBC “puro”)`
-`model/ -> Usuario, Alimento, Pedido, etc.`
-`view/ -> LoginFrame, MainFrame, Panel* (Swing)`


## 🧪 Roteiro de teste rápido
1. Criar conta e logar.  
2. Aba **Meu Pedido** → **Novo Pedido**, buscar um item e **Adicionar**.  
3. **Salvar Itens** e depois **Avaliar (0–5)**.  
4. Aba **Estatísticas** para conferir Top 5 e Totais.

## 📚 Problemas e soluções (minha experiência)
- **Login/cadastro**: no começo eu só salvava o nome e a comparação da senha falhava. Simplifiquei o fluxo e passei a registrar **nome, e-mail e senha** de uma vez, com validação e mensagens mais claras.
- **Chave duplicada ao salvar itens**: a PK composta de `pedido_item` estourava quando eu salvava de novo. Corrigi usando **`ON CONFLICT (pedido_id, alimento_id) DO UPDATE`** (upsert), somando as quantidades.
- **Avaliação duplicada**: cada pedido deve ter **uma** avaliação. Resolvi com **`ON CONFLICT (pedido_id) DO UPDATE`** para regravar a nota quando eu altero.
- **Estatísticas não atualizavam**: a aba ficava vazia mesmo após avaliar. Passei a **recarregar** ao entrar na aba e também disparei um **callback** depois de salvar/avaliar.
- **SQL x nível do curso**: optei por **JDBC simples**, consultas diretas e poucas camadas, para ficar didático e fácil de manter por mim.
- **UI Swing**: foquei em **tabelas simples** e botões diretos (Novo, Adicionar, Remover, Salvar, Avaliar, Excluir). Evitei componentes complicados para manter a interface previsível.


## 🔧 Troubleshooting
- **Login falhou**: confira o registro em `feifood.usuario` (e-mail/senha).
- **Busca vazia**: verifique se existem alimentos **ativos** (`alimento.ativo = TRUE`).
- **Top 5 vazio**: é preciso ter **pelo menos um pedido avaliado**.
- **Erro de conexão**: valide URL/usuário/senha no `ConnectionFactory`.

## 📄 Autor
Matheus Concon de Oliveira 22.124.089-8

