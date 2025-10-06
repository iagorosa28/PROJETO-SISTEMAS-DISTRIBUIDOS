package app.routers;

// coleções padrões do Java
import java.util.*;

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
    private final CadastroService channelCadastro;
    private final ListagemService channelListagem;

    public Router(SimpleDB usersDB, SimpleDB channelsDB){
        this.usersDB = usersDB;
        this.channelsDB = channelsDB;
        this.userCadastro = new CadastroService(this.usersDB);
        this.userListagem = new ListagemService(this.usersDB);
        this.channelCadastro = new CadastroService(this.channelsDB);
        this.channelListagem = new ListagemService(this.channelsDB);

    }

    public Map<String, Object> qualService(String service, Map<String, Object> data){
        
        if(service.equals("login")){
            return userCadastro.tratarCadastro(service, data);

        }else if(service.equals("users")){
            return userListagem.listar(service);

        }else if(service.equals("channel")){
            return channelCadastro.tratarCadastro(service, data);

        }else if(service.equals("channels")){
            return channelListagem.listar(service);

        }

        return Responses.error("serviço desconhecido: " + service);

    }

}