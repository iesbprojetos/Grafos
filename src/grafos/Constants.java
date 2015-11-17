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

    public static final int MODE_CREATE_GRAPH = 0;
    public static final int MODE_INSERT_ARC = 1;
    public static final int MODE_REMOVE_ARC = 2;
    public static final int MODE_FIND_PATH = 3;
    public static final int MODE_DEPTH_SEARCH = 4;
    public static final int MODE_DAGMIN = 5;
    public static final int MODE_DIJKSTRA = 6;

    public static final int RESULT_OK = 0;
    public static final int RESULT_UNKNOWN = 1;
    public static final int RESULT_INVALID_VERTEX = 2;
    public static final int RESULT_ARC_ALREADY_EXISTS = 3;
    public static final int RESULT_ARC_NOT_FOUND = 4;


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
