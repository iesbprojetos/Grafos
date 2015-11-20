package grafos;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public final class Constants {
    public static final int TYPE_GRAPH = 0;
    public static final int TYPE_DIGRAPH = 1;

    public static final int COST_NO = 0;
    public static final int COST_YES = 1;

    public static final int ALGO_MATRIX = 0;
    public static final int ALGO_LIST = 1;

    public static final String ALGO_MATRIX_NAME = "Matriz de Adjacência";
    public static final String ALGO_LIST_NAME = "Lista de Adjacência";

    public static final int RESULT_OK = 0;
    public static final int RESULT_UNKNOWN = 1;
    public static final int RESULT_INVALID_VERTEX = 2;
    public static final int RESULT_ARC_ALREADY_EXISTS = 3;
    public static final int RESULT_ARC_NOT_FOUND = 4;

    public static final String MODE_NAME_CREATE_GRAPH = "Criar Grafo";
    public static final String MODE_NAME_INSERT_ARC = "Inserir arco(s)";
    public static final String MODE_NAME_REMOVE_ARC = "Remover arco(s)";
    public static final String MODE_NAME_DEPTH_SEARCH = "Busca por Profundidade (DFS)";
    public static final String MODE_NAME_FIND_PATH = "Encontrar caminho entre dois vértices (por DFS)";
    public static final String MODE_NAME_BREADTH_SEARCH = "Busca por Largura (BFS) - Caminhos Mínimos";
    public static final String MODE_NAME_DIJKSTRA = "Dijkstra: Árvore de Menores Caminhos (SPT)";
    public static final String MODE_NAME_DIJKSTRA_HEAP = "Dijkstra: Árvore de Menores Caminhos (SPT)";
    public static final String MODE_NAME_DAGMIN = "DAGmin: Árvore de Menores Caminhos (SPT)";
    public static final String MODE_NAME_BELLMANFORD = "Bellman-Ford: Árvore de Menores Caminhos (SPT)";
    public static final String MODE_NAME_BF_SENTINEL = "Bellman-Ford (c/ Sentinela): Árvore de Menores Caminhos (SPT)";
    public static final String MODE_NAME_FLOYDWARSHALL = "Floyd-Warshall: Tabela de Menores Custos";

    public static final String MODE_INST_CREATE_GRAPH = "Clique em \"Criar Grafo\" e entre o número de vértices do novo grafo.";
    public static final String MODE_INST_INSERT_ARC = "Clique no vértice inicial do arco e arraste até o vértice final.";
    // TODO: remove arc instruction
    public static final String MODE_INST_DEPTH_SEARCH = "A tabela à direita indica os resultados.\n" +
            "Vértices de mesma cor pertencem a um mesmo componente conexo.\n" +
            "Os Arcos são coloridos de acordo com sua classificação (ver legenda à direita).";
    public static final String MODE_INST_FIND_PATH = "Clique no vértice inicial e em seguida clique no vértice final.\n" +
            "Os arcos que formam o caminho entre os vértices estará em vermelho.";
    public static final String MODE_INST_BREADTH_SEARCH = "A árvore de caminhos mínimos está representada pelas arestas em vermelho.";
    public static final String MODE_INST_SPT = "Clique no vértice inicial.\n" +
            "A Árvore de Caminhos Mínimos estará em vermelho.\n" +
            "Os custos mínimos partindo do vértice inicial até cada vértice são indicados pelos números em vermelho ao lado de cada vértice.";
    public static final String MODE_INST_FLOYDWARSHALL = "A tabela indica o custo mínimo de um vértice para outro.\n" +
            "(TODO:)Selecionar um vértice para mostrar a SPT partindo dele.";

    public static final String ERROR_MSG_UNKNOWN = "Erro desconhecido.";
    public static final String ERROR_MSG_NO_PATH = "Caminho não encontrado.";
    public static final String ERROR_MSG_INVALID_VERTEX = "Vértices inválidos";
    public static final String ERROR_MSG_ARC_ALREADY_EXISTS = "O arco que você tentou inserir já existe";
    public static final String ERROR_MSG_ARC_NOT_FOUND = "O arco que você tentou remover não existe";

    public static final int ARC_TYPE_TEMP = 0;
    public static final int ARC_TYPE_TREE = 1;
    public static final int ARC_TYPE_DESC = 2;
    public static final int ARC_TYPE_RTRN = 3;
    public static final int ARC_TYPE_CRSS = 4;
    public static final int ARC_TYPE_PATH = 5;
}
