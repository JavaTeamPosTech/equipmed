# EquipMed - Transparência e Eficiência na Gestão de Ativos de Alto Custo do SUS

O **EquipMed** é um ecossistema de microserviços desenvolvido como projeto de Hackathon para a conclusão da **Pós-Graduação em Arquitetura e Desenvolvimento Java**. A solução visa preencher uma lacuna crítica na gestão da saúde pública brasileira: a governança sobre o ciclo de vida, a manutenção e a produtividade de equipamentos médicos de alto valor.

## 1. O Problema e a Motivação

Atualmente, o Sistema Único de Saúde (SUS) gerencia um vasto parque tecnológico. No entanto, o acompanhamento desses ativos ocorre frequentemente em silos de dados isolados, focando apenas no cadastro burocrático ou na entrega da obra física, negligenciando a **operação contínua**.

### Desafios Identificados:

* **"Caixa Preta" na Manutenção:** Dificuldade em identificar quando o custo acumulado de manutenção de um equipamento supera o seu valor de aquisição (ponto de substituição econômica).
* **Equipamentos Ociosos:** Falta de visibilidade em tempo real sobre máquinas que estão cadastradas como "Ativas", mas não registram produtividade clínica real.
* **Ineficiência Licitatória:** Ausência de dados consolidados sobre a confiabilidade de fabricantes e prestadores de serviço, dificultando a tomada de decisão em futuras compras.
* **Desperdício de Recurso Público:** Verba destinada à saúde sendo drenada por contratos de manutenção desvantajosos ou equipamentos tecnicamente obsoletos.

## 2. Entendendo o Ecossistema SUS (Domínio)

Para um controle eficaz, o EquipMed integra e complementa os principais sistemas de dados da saúde pública brasileira:

* **SCNES (Cadastro Nacional de Estabelecimentos de Saúde):** Base oficial que registra onde os hospitais estão e quais recursos possuem. O EquipMed atua como a camada de monitoramento vivo sobre essa base estática.
* **SISMOB (Sistema de Monitoramento de Obras):** Focado no investimento inicial e entrega. O EquipMed inicia sua gestão onde o SISMOB termina: no dia a dia da operação.
* **SIGTAP (Tabela de Procedimentos, Medicamentos e OPM do SUS):** O "catálogo de serviços" do SUS. O EquipMed utiliza o SIGTAP como **validador de utilidade técnica**. Cada equipamento é vinculado a códigos específicos; se a máquina não gera registros SIGTAP, ela é sinalizada como ociosa.

## 3. A Solução: EquipMed

O projeto traz **Eficiência, Eficácia e Transparência** através de um monitoramento dinâmico baseado em três pilares estratégicos:

* **Registro de Ativos:** Cadastro detalhado incluindo marca, modelo, valor de aquisição, fornecedor e localização geográfica precisa.
* **Monitoramento Operacional (Uso e SIGTAP):** O uso do equipamento é validado por regras de domínio. O sistema impede, por exemplo, que um "Tomógrafo" registre um procedimento de "Radioterapia", garantindo a integridade da fiscalização.
* **Gestão de Manutenções:** Histórico completo de intervenções (preventivas e corretivas), com cálculo automático do percentual de custo sobre o valor original do ativo.
* **Dashboard de Transparência:** Interface pública com indicadores de disponibilidade, produtividade e alertas de custos excessivos, permitindo o controle social e auditoria governamental.

## 4. Matriz de Ativos e Escopo Inicial

O EquipMed foca em equipamentos de alto custo, onde o impacto financeiro de má gestão é mais severo:

| Equipamento | Valor Médio | Finalidade Principal | Família de Procedimentos SIGTAP |
| --- | --- | --- | --- |
| **Acelerador Linear** | R$ 4.500.000 | Tratamento Oncológico (Radioterapia) | **03.04.01.xxx** |
| **PET-CT** | R$ 8.000.000 | Diagnóstico por Imagem Avançado | **02.08.08.xxx** |
| **Ressonância Magnética** | R$ 3.500.000 | Diagnóstico por Imagem de Alta Definição | **02.08.09.xxx** |
| **Tomógrafo Computadorizado** | R$ 1.200.000 | Diagnóstico por Imagem Seccional | **02.06.01.xxx** |
| **Angiógrafo** | R$ 2.000.000 | Cardiologia Intervencionista | **02.11.02.xxx** |

## 5. Diferenciais Técnicos e Governança

* **Validação de Jurisdição via API Keys:** Cada Unidade de Saúde (Hospital/UBS) possui uma chave exclusiva. O **API Gateway** valida a chave e injeta a identidade da unidade, impedindo que dados de uma cidade sejam alterados por usuários de outra jurisdição.
* **Identificação de Padrões para Auditoria:** Sinalização automática de equipamentos "críticos" (gasto em manutenção > 50% do valor de compra) ou "fantasmas" (sem produção registrada).
* **Massa de Dados para IA:** A estrutura (Idade x Uso x Falha) cria o alicerce para **Manutenção Preditiva**, visando reduzir o tempo de máquina parada através de inteligência artificial.

## 6. Visão de Futuro

* **Integração Preditiva:** Antecipar quebras antes que ocorram com base no histórico de carga.
* **Ranking de Confiabilidade de Fornecedores:** Score público para balizar novas licitações.
* **Controle de Insumos:** Rastreabilidade de peças de alto valor trocadas durante o processo de manutenção.

## 7. Arquitetura do Sistema

O **EquipMed** adota uma arquitetura de microserviços descentralizada, priorizando a escalabilidade e o isolamento de domínios. A comunicação entre os serviços é realizada via **REST/HTTP** com o suporte de um **API Gateway** como ponto único de entrada.

### 7.1. Componentes da Solução

* **API Gateway (Central de Governança):**
* **Papel:** Porta de entrada única (Porta 8080).
* **Responsabilidade:** Implementa o filtro de segurança de **API Keys**, realiza o roteamento para os microserviços internos e enriquece os cabeçalhos (Headers) da requisição com dados de jurisdição (Unidade e Cidade).


* **MS-Equipamentos (Gestão de Inventário):**
* **Domínio:** Responsável pelo ciclo de vida do ativo (Cadastro, Marca, Modelo, Valor).
* **Persistência:** PostgreSQL para dados relacionais e estruturados.
* **Segurança:** Realiza o *double-check* dos headers de jurisdição injetados pelo Gateway.


* **MS-Operacional (Gestão de Eventos):**
* **Domínio:** Gerencia os eventos dinâmicos: Usos (vinculados ao SIGTAP) e Manutenções (Corretivas e Preventivas).
* **Regra de Ouro:** Valida a compatibilidade técnica entre o código de procedimento SUS e o tipo de máquina.


* **MS-Transparencia (Agregador de Inteligência):**
* **Domínio:** Consome dados dos serviços de escrita para gerar indicadores consolidados.
* **Performance:** Utiliza **Redis** para cache de indicadores de alto acesso (Sumários e Dashboards por cidade).
* **Responsabilidade:** Expõe os dados para o Front-end de auditoria.



### 7.2. Fluxo de Dados e Comunicação

A integração entre os serviços para a geração do Dashboard ocorre de forma síncrona através de **OpenFeign (Feign Clients)**.

1. O **MS-Transparencia** solicita a lista de equipamentos ao **MS-Equipamentos**.
2. Para cada equipamento, ele consulta o **MS-Operacional** para totalizar custos de manutenção e volume de exames.
3. Os dados são agregados por cidade e unidade de saúde para exibição no portal público.

---

## 8. Stack Tecnológica

| Camada | Tecnologia | Justificativa Sênior |
| --- | --- | --- |
| **Linguagem** | Java 21 | Uso de Virtual Threads (Loom) e Records para DTOs. |
| **Framework** | Spring Boot 3.3+ | Ecossistema maduro para Cloud Native. |
| **Gateway** | Spring Cloud Gateway | Filtros reativos para alta performance em segurança. |
| **Persistência** | PostgreSQL 16 | Garantia de integridade referencial e transacional. |
| **Cache** | Redis | Redução de latência em endpoints de transparência pública. |
| **Client** | OpenFeign | Abstração elegante para comunicação inter-serviços. |
| **Conteinerização** | Docker & Docker Compose | Padronização de ambiente e orquestração simplificada. |

---

## 9. Modelagem de Dados (Entidades Principais)

Para manter a clareza para os avaliadores, as entidades foram modeladas focando no **valor de auditoria**:

* **Equipamento:** `ID`, `TagPatrimonio`, `Tipo`, `ValorAquisicao`, `DataAquisicao`, `UnidadeSaude`, `Cidade`.
* **Uso:** `ID`, `EquipamentoID`, `CodigoSigtap`, `DataUso`, `OperadorID`.
* **Manutencao:** `ID`, `EquipamentoID`, `Tipo (Preventiva/Corretiva)`, `Valor`, `EmpresaPrestadora`.

---

## 10. Detalhamento dos Microserviços e Endpoints

Abaixo, detalhamos as responsabilidades de cada serviço e os contratos de API disponíveis. Todas as requisições de escrita (POST/PATCH) para os serviços operacionais e de equipamentos exigem o Header `X-API-KEY`.

### 10.1. MS-Equipamentos (Gestão de Inventário)

Este serviço é o detentor da "verdade" sobre o ativo físico. Ele gerencia o cadastro e a localização dos equipamentos de alto valor.

* **Responsabilidade:** Cadastro, filtragem e atualização de status de patrimônio.
* **Segurança:** Implementa o *Double-Check* de jurisdição comparando o JSON com os Headers `X-Unidade-Nome` e `X-Unidade-Cidade`.

| Verbo | Endpoint | Descrição |
| --- | --- | --- |
| **POST** | `/api/equipamentos` | Cadastra um novo equipamento (Requer Header de API Key). |
| **GET** | `/api/equipamentos/{id}` | Busca detalhes técnicos de um equipamento específico via UUID. |
| **GET** | `/api/equipamentos` | Lista equipamentos com suporte a paginação e filtros (Cidade, Unidade, Status, Tipo). |
| **PATCH** | `/api/equipamentos/{id}/status` | Atualiza o estado operacional (ATIVO, MANUTENCAO, OBSOLETO). |
| **PATCH** | `/api/equipamentos/{id}/localizacao` | Registra a transferência de um equipamento entre cidades ou estados. |

---

### 10.2. MS-Operacional (Gestão de Eventos e SIGTAP)

O motor dinâmico do sistema. Ele registra o que acontece com o equipamento durante sua vida útil.

* **Responsabilidade:** Registro de produtividade clínica e custos de manutenção.
* **Regra de Negócio:** Valida a compatibilidade do código SIGTAP com o tipo de equipamento no ato do registro de uso.

| Verbo | Endpoint | Descrição |
| --- | --- | --- |
| **POST** | `/api/operacional/usos` | Registra um atendimento vinculado a um código SIGTAP. |
| **POST** | `/api/operacional/manutencoes` | Registra intervenção técnica (Corretiva/Preventiva) e seu respectivo custo. |
| **GET** | `/api/operacional/usos/{id}` | Histórico completo de exames realizados por um equipamento. |
| **GET** | `/api/operacional/manutencoes/{id}` | Histórico de gastos e intervenções de uma máquina específica. |
| **POST** | `/api/operacional/batch-summary` | **Internal:** Endpoint otimizado para o MS-Transparência consolidar dados de múltiplos IDs. |

---

### 10.3. MS-Transparencia (Portal de Auditoria)

O serviço de leitura e agregação. Ele não possui banco de dados de escrita próprio (PostgreSQL), utilizando **Redis** para cachear visões consolidadas.

* **Responsabilidade:** Cruzar dados de Inventário + Operacional para gerar insights de fiscalização.
* **Consumo:** Utiliza OpenFeign para buscar dados dos outros microserviços em tempo real.

| Verbo | Endpoint | Descrição |
| --- | --- | --- |
| **GET** | `/api/transparencia/sumario` | Retorna o "Painel Executivo" (Total investido, Disponibilidade Geral, Ociosidade). |
| **GET** | `/api/transparencia/cidades` | Consolida indicadores agrupados por município (Foco em Gestão Municipal). |
| **GET** | `/api/transparencia/unidades` | Lista a performance individual de cada hospital ou UBS (Paginado no Frontend). |
| **GET** | `/api/transparencia/alertas` | Filtra apenas equipamentos que exigem atenção imediata (Custo > 50% ou Ociosos). |
| **GET** | `/api/transparencia/auditoria/{tag}` | Gera o **Dossiê Completo** de um patrimônio para fins de fiscalização. |
| **GET** | `/api/transparencia/painel-geral` | Listagem bruta de todos os ativos enriquecidos com dados operacionais. |

---

### 10.4. API Gateway (Roteamento e Segurança)

O ponto de entrada (Porta `8080`) que orquestra as chamadas para os serviços internos.

* **Funcionalidade:** Load Balancing e Filtro de API Key.
* **Mapeamento de Rotas:**
* `/api/equipamentos/**` → MS-Equipamentos
* `/api/operacional/**` → MS-Operacional
* `/api/transparencia/**` → MS-Transparencia

---

## 16. Autopopulação Dinâmica de Dados (Data Seeding)

Diferente de uma carga estática via SQL, o **EquipMed** implementa uma estratégia de **Carga Dinâmica de Domínio**. Ao iniciar os microserviços em perfil de demonstração (`prod` ou `dev`), um componente `CommandLineRunner` é disparado para popular o ecossistema.

### 16.1. O Mecanismo de Seed

O processo de automação lê arquivos JSON de referência (contendo modelos de equipamentos e códigos SIGTAP) e gera uma "Super Massa" de dados realista para três polos principais: **Recife/PE**, **São Paulo/SP** e **Rio de Janeiro/RJ**.

### 16.2. O que é gerado automaticamente?

* **Variabilidade Geográfica:** Equipamentos distribuídos proporcionalmente entre as três cidades.
* **Histórico de Vida Útil:** O gerador calcula datas de aquisição retroativas, gerando "idades" diferentes para as máquinas, o que permite testar os cálculos de depreciação e obsolescência.
* **Simulação de Estresse Financeiro:** * O sistema gera aleatoriamente manutenções preventivas (baixo custo).
* O sistema injeta **manutenções corretivas críticas** em equipamentos aleatórios para que os alertas de auditoria (Custo > 50%) apareçam de forma orgânica no Dashboard.


* **Volume de Produção:** Milhares de registros de "Uso" são criados e vinculados a códigos SIGTAP reais, permitindo a visualização imediata da produtividade por município.

### 16.3. Vantagens desta Abordagem

* **Validação de Regras de Negócio:** Como os dados passam pelas camadas de `Service`, todas as validações de domínio (como a compatibilidade SIGTAP) são testadas durante a carga.
* **Demonstração de Performance:** A automação gera massa suficiente para validar a paginação do Frontend e a eficiência do cache **Redis** no `ms-transparencia`.
* **Zero Configuração:** O avaliador não precisa executar scripts manuais; basta subir o ambiente e os dados "brotam" no dashboard em poucos segundos.

---

## 17. Como visualizar os dados gerados

Após o `docker-compose up`, você verá nos logs dos microserviços:
`INFO: [SEED] Iniciando carga de 500 registros de uso para a Unidade X...`

Ao abrir o Dashboard (`http://localhost`), você encontrará:

1. **Indicadores Reais:** Gráficos e sumários populados com a média de Recife, SP e RJ.
2. **Cenários de Teste:** Equipamentos em vermelho (críticos) e verde (excelentes) prontos para inspeção.
3. **Dossiês Completos:** Tags de patrimônio prontas para serem consultadas na busca individual.

---