export function logarUsuario(nomeUsuario){
    return { 
        service: "login", 
        data: { 
            user: nomeUsuario, 
            timestamp: new Date().toISOString() 
        } 
    };
}

export function listarUsuarios(){ 
    return { 
        service:"users", 
        data: { 
            timestamp:new Date().toISOString() 
        } 
    }; 
}

export function publicarParaUsuario(userOrigin, userDestin){
    return { 
        service:"message", 
        data: { 
            src:userOrigin, 
            dst:userDestin, 
            message, 
            timestamp:new Date().toISOString() 
        } 
    };
}

export function entrarNoCanal(nomeCanal){
    return { 
        service:"channel", 
        data: { 
            channel:nomeCanal, 
            timestamp:new Date().toISOString() 
        } 
    };
}

export function listarCanais(){ 
    return { 
        service:"channels", 
        data: { 
            timestamp:new Date().toISOString() 
        } 
    }; 
}

export function publicarNoCanal(user, channel, message){
    return { 
        service:"publish", 
        data:{ 
            user, 
            channel, 
            message, 
            timestamp:new Date().toISOString() 
        } 
    };
}