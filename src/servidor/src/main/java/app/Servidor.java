package app;

// para criar socket REP e falar com o broker
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

// para (de)serializar JSON
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

// gerar timestamp ISO-8601
import java.time.OffsetDateTime;

// coleções padrões do Java
import java.util.*;


public class Servidor{

    private static final String BROKER = "tcp://broker:5556";

    private static final ObjectMapper JSON = new ObjectMapper();

    // conjunto de usuários sem duplicados (depois trocar pelo BD)
    private static final Set<String> USERS = new HashSet(); 

    public static void main(String[] args) throws Exception{
        
        // "try-with-resources": ao sair do bloco, o ZContext é fechado automaticamente
        try(ZContext ctx = new ZContext()){
            
            // cria uma socket do tipo replay
            ZMQ.Socket rep = ctx.createSocket(ZMQ.REP);
           
            // conecta ao broker
            rep.connect(BROKER);
            System.out.println("Servidor Java conectado em: " + BROKER);
            
            while(true){
                
                // esperando mensagem do cliente
                byte[] reqBytes = rep.recv(0);

                if(reqBytes == null) continue;

                Map<String,Object> resposta;
                try{
                    
                    // TypeReference preversa os tipos genéricos
                    Map<String,Object> req = JSON.readValue(
                        reqBytes, new TypeReference<Map<String,Object>>() {}
                    );

                    String service = verificaString(req.get("service"));
                    Map<String,Object> data = (Map<String,Object>) req.get("data");

                    if(service.equals("login")){
                        resposta = tratarLogin(data);
                    }else{
                        resposta = error("serviço desconhecido: " + service);
                    }

                }catch(Exception e){
                    resposta = error("JSON inválido");
                }
                
                // '0' = single-part
                rep.send(JSON.writeValueAsBytes(resposta), 0);

            }

        }

    }

    private static Map<String, Object> tratarLogin(Map<String,Object> data){
        String user = verificaString(data.get("user"));

        if(user == null || user.trim().isEmpty()){
            return serviceError("login", "dados de usuário inválidos");
        }

        user = user.trim();

        if(USERS.contains(user)){
            return serviceError("login", "usuário já existente");
        }

        USERS.add(user);

        Map<String,Object> resposta = baseDataOk();
        resposta.put("description", user + "cadastrado!");

        return ok("login", resposta);
    }

    private static Map<String,Object> ok(String service, Map<String,Object> data){
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("service", service);
        resposta.put("data", data);
        return resposta;
    }

    private static Map<String,Object> baseDataOk(){
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("timestamp", OffsetDateTime.now().toString());
        return resposta;
    }

    private static Map<String,Object> error(String descricao){
        Map<String,Object> d = baseDataError(descricao);
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("service", "error");
        resposta.put("data", d);
        return resposta;
    }

    private static Map<String,Object> serviceError(String service, String descricao){
        Map<String,Object> d = baseDataError(descricao);
        Map<String,Object> resposta = new HashMap<>();
        resposta.put("service", service);
        resposta.put("data", d);
        return resposta;
    }

    private static Map<String,Object> baseDataError(String descricao){
        Map<String,Object> d = new HashMap<>();
        d.put("status", "erro");
        d.put("timestamp", OffsetDateTime.now().toString());
        d.put("description", descricao);
        return d;
    }

    // instanceof verifica se o objeto é de um tipo (em tempo de execução)
    // exemplo: (o instanceof Number)
    private static String verificaString(Object o){
        return (o instanceof String) ? (String) o : null;
    }

}