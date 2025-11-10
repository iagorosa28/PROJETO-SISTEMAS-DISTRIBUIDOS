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
    private final SimpleDB db;
    
    private final ZContext ctx;
    private final ZMQ.Socket pub;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String XSUB = "tcp://proxy:5557";

    public PubService(SimpleDB db) {
        this.db = db;
        this.ctx = new ZContext();
        this.pub = ctx.createSocket(SocketType.PUB);
        this.pub.setImmediate(true);
        this.pub.setSndHWM(10000);
        this.pub.setLinger(200);
        this.pub.connect(XSUB);
        System.out.println("Publisher conectado em: " + XSUB);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { pub.close(); } finally { ctx.close(); }
        }));
    }

    public synchronized Map<String, Object> tratarPub(String service, Map<String,Object> data){
        String name = null;

        if(service.equals("publish")){
            name = Utils.verificaString(data.get("channel"));
        }else if(service.equals("message")){
            name = Utils.verificaString(data.get("dst"));
        }

        if(name == null || name.trim().isEmpty()){
            if(service.equals("publish")){
                return Responses.serviceError("publish", "channel destino inválido");
            }else if(service.equals("message")){
                return Responses.serviceError("dst", "usuário destino inválido");
            }
        }

        name = name.trim();
        Map<String,Object> envio = new HashMap<>();
        if(service.equals("publish")){
            envio.put("user", data.get("user"));
        }else if(service.equals("message")){
            envio.put("user", data.get("src"));
        }
        envio.put("message", data.get("message"));
        envio.put("timestamp", data.get("timestamp"));

        try {
            String topic = name.trim();
            String json  = objectMapper.writeValueAsString(envio);
            boolean ok = pub.send(topic, ZMQ.SNDMORE | ZMQ.DONTWAIT);
            ok &= pub.send(json, ZMQ.DONTWAIT);
            if (!ok) return Responses.serviceError("message", "fila cheia ou rota indisponível");
            return Responses.ok(service, Responses.baseDataOk());
        } catch (JsonProcessingException e) {
            return Responses.serviceError("message", "falha ao serializar payload");
        } catch (Throwable t) {
            return Responses.serviceError("message", "erro no publisher: " + t.getClass().getSimpleName());
        }
    }
}