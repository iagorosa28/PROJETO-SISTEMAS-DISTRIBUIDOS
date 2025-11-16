package app;

// para criar socket REP e falar com o broker
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.SocketType;

// para (de)serializar JSON
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.msgpack.jackson.dataformat.MessagePackFactory;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;
import app.routers.*;
import app.persistence.*;

public class Servidor{

    private static final String BROKER = "tcp://broker:5556";
    private static final String PROXY = "tcp://proxy:5557";

    // private static final ObjectMapper JSON = new ObjectMapper();
    private static final ObjectMapper JSON = new ObjectMapper(new MessagePackFactory());

    private static final String url = "jdbc:sqlite:/app/data/meubanco.db";

    public static void main(String[] args) throws Exception{

        SimpleDB usersDB = new SimpleDB(url, "users");
        SimpleDB channelsDB = new SimpleDB(url, "channels");
        MsgDB msgDB = new MsgDB(url, "messages");
        
        // "try-with-resources": ao sair do bloco, o ZContext é fechado automaticamente
        try(ZContext ctx = new ZContext()){
            
            // cria sockets rep e pub
            ZMQ.Socket rep = ctx.createSocket(ZMQ.REP);
            ZMQ.Socket pub = ctx.createSocket(SocketType.PUB);
           
            // conecta ao broker / proxy
            rep.connect(BROKER);
            System.out.println("Servidor Java conectado em: " + BROKER);
            pub.connect(PROXY);
            System.out.println("Servidor Java conectado em: " + PROXY);

            Router router = new Router(usersDB, channelsDB, msgDB, pub);
            
            while(true){
                
                // esperando mensagem do cliente
                byte[] reqBytes = rep.recv(0);

                if(reqBytes == null) continue;

                Map<String,Object> resp = Responses.error("resposta ausente");
                try{
                    
                    // TypeReference preserva os tipos genéricos
                    Map<String,Object> req = JSON.readValue(
                        reqBytes, new TypeReference<Map<String,Object>>() {}
                    );

                    String service = Utils.verificaString(req.get("service"));
                    Map<String,Object> data = (Map<String,Object>) req.get("data");

                    resp = router.qualService(service, data);
                    if (resp == null) {
                        resp = Responses.error("resposta nula do serviço");
                    }
                }catch (Throwable t) {
                    resp = Responses.error("erro interno: " + t.getClass().getSimpleName());
                } finally {
                    try {
                        rep.send(JSON.writeValueAsBytes(resp), 0);
                    } catch (Throwable t2) {
                        System.err.println("Falha ao enviar resposta: " + t2);
                    }
                }

            }

        }

    }

}