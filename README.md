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

---
