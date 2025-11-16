package app.utils;

// coleções padrões do Java
import java.util.*;

// gerar timestamp ISO-8601
import java.time.OffsetDateTime;

public final class Responses{

    private static final String INSTANCE_ID =
        System.getenv().getOrDefault("HOSTNAME", "unknown"); // nome do container

    private Responses() {}

    public static Map<String,Object> ok(String service, Map<String,Object> data){
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("servidor", INSTANCE_ID);
        resposta.put("service", service);
        resposta.put("data", data);
        return resposta;
    }

    public static Map<String,Object> baseDataOk(){
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("timestamp", OffsetDateTime.now().toString());
        return resposta;
    }

    public static Map<String,Object> error(String descricao){
        Map<String,Object> d = baseDataError(descricao);
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("servidor", INSTANCE_ID);
        resposta.put("service", "error");
        resposta.put("data", d);
        return resposta;
    }

    public static Map<String,Object> serviceError(String service, String descricao){
        Map<String,Object> d = baseDataError(descricao);
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("servidor", INSTANCE_ID);
        resposta.put("service", service);
        resposta.put("data", d);
        return resposta;
    }

    public static Map<String,Object> baseDataError(String descricao){
        Map<String,Object> d = new HashMap<>();
        d.put("status", "erro");
        d.put("timestamp", OffsetDateTime.now().toString());
        d.put("description", descricao);
        return d;
    }

    // sla, não quis pensar muito
    public static Map<String,Object> baseDataOkList(){
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("timestamp", OffsetDateTime.now().toString());
        return resposta;
    }

}