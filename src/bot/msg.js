export function logarUsuario(nomeUsuario, clock){
    return { 
        service: "login", 
        data: { 
            user: nomeUsuario, 
            timestamp: new Date().toISOString(),
            clock: clock 
        } 
    };
}

export function listarUsuarios(clock){ 
    return { 
        service: "users", 
        data: { 
            timestamp: new Date().toISOString(),
            clock: clock 
        } 
    }; 
}

export function publicarParaUsuario(userOrigin, userDestin, clock){
    return { 
        service: "message", 
        data: { 
            src: userOrigin, 
            dst: userDestin, 
            message: message, 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    };
}

export function entrarNoCanal(nomeCanal, clock){
    return { 
        service: "channel", 
        data: { 
            channel: nomeCanal, 
            timestamp: new Date().toISOString(),
            clock: clock 
        } 
    };
}

export function listarCanais(clock){ 
    return { 
        service: "channels", 
        data: { 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    }; 
}

export function publicarNoCanal(user, channel, message, clock){
    return { 
        service: "publish", 
        data: { 
            user: user, 
            channel: channel, 
            message: message, 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    };
}