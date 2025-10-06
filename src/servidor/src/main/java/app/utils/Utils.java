package app.utils;

public final class Utils{

    private Utils(){}

    // instanceof verifica se o objeto é de um tipo (em tempo de execução)
    // exemplo: (o instanceof Number)
    public static String verificaString(Object o){
        return (o instanceof String) ? (String) o : null;
    }

}