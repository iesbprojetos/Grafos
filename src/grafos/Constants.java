package grafos;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public final class Constants {
    public static final int TYPE_GRAPH = 0;
    public static final int TYPE_DIGRAPH = 1;

    public static final int ALGO_MATRIX = 0;
    public static final int ALGO_LIST = 1;

    public static final String ALGO_MATRIX_NAME = "Matriz de Adjacência";
    public static final String ALGO_LIST_NAME = "Lista de Adjacência";

    public static final int MODE_INSERT_ARC = 1;
    public static final int MODE_REMOVE_ARC = 2;
    public static final int MODE_FIND_PATH = 3;

    public static final String ERROR_MSG_NO_PATH = "Caminho não encontrado.";

    public static final int ARC_TYPE_TEMP = 0;
    public static final int ARC_TYPE_TREE = 1;
    public static final int ARC_TYPE_DESC = 2;
    public static final int ARC_TYPE_RTRN = 3;
    public static final int ARC_TYPE_CRSS = 4;
    public static final int ARC_TYPE_PATH = 5;
}
