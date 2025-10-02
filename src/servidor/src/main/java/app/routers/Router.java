package app.routers;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;
import app.services.*;

public class Router{

    private final Set<String> listaUsers;
    private final Set<String> listaChannels;

    // reaproveita serviços (evita new a cada request)
    private final CadastroService userCadastro;
    private final ListagemService userListagem;
    private final CadastroService channelCadastro;
    private final ListagemService channelListagem;

    public Router(Set<String> listaUsers, Set<String> listaChannels){
        this.listaUsers = listaUsers;
        this.listaChannels = listaChannels;
        this.userCadastro = new CadastroService(this.listaUsers);
        this.userListagem = new ListagemService(this.listaUsers);
        this.channelCadastro = new CadastroService(this.listaChannels);
        this.channelListagem = new ListagemService(this.listaChannels);
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