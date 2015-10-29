# Grafos #

Este programa é uma implementação simples em Java de dois tipos de representação computacional de Grafos:
 - Matrizes de Adjacência
 - Vetores de Adjacência.

Ele foi desenvolvido como projeto da disciplina **Teoria dos Grafos**, no **IESB** (Instituto de Educação Superior de Brasília), pelos alunos **Danilo Carvalho** e **André Corrêa** durante o segundo semestre de 2015, sob orientação do professor **João Paulo Ataide Martins**.

## Dependências ##
* Java 1.7+
* JavaFX 2.2+

## Configurações de Projeto por IDE ##

### IntelliJ IDEA ###

Basta importar ou clonar o projeto

### Netbeans ###

1. Importar/clonar o projeto
2. Abrir configurações do projeto
3. Build -> Deployment
4. Clicar em "Switch Project To JavaFX Deployment Model"
5. Confirmar em "Modify Project"
6. Deletar o arquivo criado: <default package>/GrafosDigrafosFX.java

### Eclipse ###

Instalar plugin e(fx)clipse: http://www.eclipse.org/efxclipse/index.html 

## Funcionalidades ##

* Criar grafos simples (sem loops ou multiplas arestas entre dois vértices)
* Buscar caminho entre dois vértices
* Busca por Profundidade, resultando em:
  * Árvores
  * Ordem de visitação
  * Vetor de pais
  * Vetor de tempo
  * Ordem topológica (quando houver)
