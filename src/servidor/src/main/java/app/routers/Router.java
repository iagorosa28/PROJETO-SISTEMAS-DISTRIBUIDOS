package app.routers;

// coleções padrões do Java
import java.util.*;

import org.zeromq.ZMQ;

// importações dos packages
import app.utils.*;
import app.services.*;
import app.persistence.*;

public class Router{

    private final SimpleDB usersDB;
    private final SimpleDB channelsDB;

    // reaproveita serviços (evita new a cada request)
    private final CadastroService userCadastro;
    private final ListagemService userListagem;
    private final PubService userMessage;
    private final CadastroService channelCadastro;
    private final ListagemService channelListagem;
    private final PubService channelMessage;

    private final ZMQ.Socket pub;

    public Router(SimpleDB usersDB, SimpleDB channelsDB, ZMQ.Socket pub){
        this.usersDB = usersDB;
        this.channelsDB = channelsDB;
        this.pub = pub;
        this.userCadastro = new CadastroService(this.usersDB);
        this.userListagem = new ListagemService(this.usersDB);
        this.userMessage = new PubService(this.usersDB, this.pub);
        this.channelCadastro = new CadastroService(this.channelsDB);
        this.channelListagem = new ListagemService(this.channelsDB);
        this.channelMessage = new PubService(this.channelsDB, this.pub);
    }

    public Map<String, Object> qualService(String service, Map<String, Object> data){
        
        if(service.equals("login")){
            return userCadastro.tratarCadastro(service, data);

        }else if(service.equals("users")){
            return userListagem.listar(service);

        }else if(service.equals("message")){
            return userMessage.tratarPub(service, data);

        }else if(service.equals("channel")){
            return channelCadastro.tratarCadastro(service, data);

        }else if(service.equals("channels")){
            return channelListagem.listar(service);

        }else if(service.equals("publish")){
            return channelMessage.tratarPub(service, data);

        }

        return Responses.error("serviço desconhecido: " + service);

    }

}