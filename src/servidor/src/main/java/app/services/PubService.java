package app.services;

// coleções padrões do Java
import java.util.*;

// para criar socket REP e falar com o broker
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.SocketType;

// para (de)serializar JSON
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;

// importações dos packages
import app.utils.*;
import app.persistence.*;

public class PubService{
    private final MsgDB db;
    
    private final ZMQ.Socket pub;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PubService(MsgDB db, ZMQ.Socket pub) {
        this.db = db;
        this.pub = pub;
    }

    public synchronized Map<String, Object> tratarPub(String service, Map<String,Object> data, int clock){
        String name = null;

        if(service.equals("publish")){
            name = Utils.verificaString(data.get("channel"));
            if(name == null || name.trim().isEmpty()){
                return Responses.serviceError("publish", "channel destino inválido", clock);
            }
        }else if(service.equals("message")){
            name = Utils.verificaString(data.get("dst"));
            if(name == null || name.trim().isEmpty()){
                return Responses.serviceError("dst", "usuário destino inválido", clock);
            }
        }

        name = name.trim();
        String origem = null;
        
        Map<String,Object> envio = new HashMap<>();
        if(service.equals("publish")){
            origem = (String) data.get("user");
        }else if(service.equals("message")){
            origem = (String) data.get("src");
        }

        String mensagem = (String) data.get("message");
        String timestamp = (String) data.get("timestamp");

        envio.put("user", origem);
        envio.put("message", mensagem);
        envio.put("timestamp", timestamp);
        envio.put("clock", clock);

        try {
            String topic = name;
            String json  = objectMapper.writeValueAsString(envio);
            boolean ok = pub.send(topic, ZMQ.SNDMORE | ZMQ.DONTWAIT);
            ok &= pub.send(json, ZMQ.DONTWAIT); // &= -> AND ex: 'if (topic == OK && json == ok) -> true'... algo assim ok = ok & algo -> retorna true or false
            if (!ok) return Responses.serviceError("message", "fila cheia ou rota indisponível", clock);
            db.addMsg(origem, topic, mensagem, timestamp, clock);
            return Responses.ok(service, Responses.baseDataOk(clock));
        } catch (JsonProcessingException e) {
            return Responses.serviceError("message", "falha ao serializar payload", clock);
        } catch (Throwable t) {
            return Responses.serviceError("message", "erro no publisher: " + t.getClass().getSimpleName(), clock);
        }
    }
}
