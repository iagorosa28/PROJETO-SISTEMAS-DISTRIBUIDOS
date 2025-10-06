package app;

// para criar socket REP e falar com o broker
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

// para (de)serializar JSON
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;
import app.routers.*;
import app.persistence.*;

public class Servidor{

    private static final String BROKER = "tcp://broker:5556";

    private static final ObjectMapper JSON = new ObjectMapper();

    private static final String url = "jdbc:sqlite:/app/data/meubanco.db";

    public static void main(String[] args) throws Exception{

        SimpleDB usersDB = new SimpleDB(url, "users");
        SimpleDB channelsDB = new SimpleDB(url, "channels");
        
        // "try-with-resources": ao sair do bloco, o ZContext é fechado automaticamente
        try(ZContext ctx = new ZContext()){
            
            // cria uma socket do tipo reply
            ZMQ.Socket rep = ctx.createSocket(ZMQ.REP);
           
            // conecta ao broker
            rep.connect(BROKER);
            System.out.println("Servidor Java conectado em: " + BROKER);

            Router router = new Router(usersDB, channelsDB);
            
            while(true){
                
                // esperando mensagem do cliente
                byte[] reqBytes = rep.recv(0);

                if(reqBytes == null) continue;

                Map<String,Object> resposta;
                try{
                    
                    // TypeReference preserva os tipos genéricos
                    Map<String,Object> req = JSON.readValue(
                        reqBytes, new TypeReference<Map<String,Object>>() {}
                    );

                    String service = Utils.verificaString(req.get("service"));
                    Map<String,Object> data = (Map<String,Object>) req.get("data");

                    resposta = router.qualService(service, data);

                }catch(Exception e){
                    resposta = Responses.error("JSON inválido");
                }
                
                // '0' = single-part
                rep.send(JSON.writeValueAsBytes(resposta), 0);

            }

        }

    }

}