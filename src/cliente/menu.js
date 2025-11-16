import readline from "readline/promises";
import { stdin as input, stdout as output } from "node:process";

const rl = readline.createInterface({ input, output });

export async function menu(user, clock){
    console.log("Menu Inicial...");
    console.log("1. Listar usuários");
    console.log("2. Publicar para usuário");
    console.log("3. Entrar no canal");
    console.log("4. Listar canais");
    console.log("5. Publicar no canal");
    console.log("6. Sair");
    const opcao = Number(await rl.question("Digite uma opção: "));

    if (opcao === 1)  return listarUsuarios(clock);
    if (opcao === 2)  return await publicarParaUsuario(user, clock);
    if (opcao === 3)  return await entrarNoCanal(clock);
    if (opcao === 4)  return listarCanais(clock);
    if (opcao === 5)  return await publicarNoCanal(user, clock);
    if (opcao === 6)  { console.log("Tchau!"); return "sair"; }

    console.log("Opção inválida!");
    return "voltar";
}

export async function logarUsuario(clock){
    const nomeUsuario = (await rl.question("Digite o nome de usuario que deseja logar: ")).trim();
    return { 
        service: "login", 
        data: { 
            user: nomeUsuario, 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    };
}

/* As funções abaixo podem continuar síncronas se não perguntarem nada; */
function listarUsuarios(clock){ 
    return { 
        service: "users", 
        data: { 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    }; 
}

async function publicarParaUsuario(userOrigin, clock){
    const userDestin = (await rl.question("Digite o nome de usuario destino: ")).trim();
    const message    = (await rl.question("Digite a mensagem: ")).trim();
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

async function entrarNoCanal(clock){
    const nomeCanal = (await rl.question("Digite o nome do canal: ")).trim();
    return { 
        service: "channel", 
        data:{ channel: nomeCanal, 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    };
}

function listarCanais(clock){ 
    return { 
        service: "channels", 
        data: { 
            timestamp: new Date().toISOString(),
            clock: clock
        } 
    }; 
}

async function publicarNoCanal(user, clock){
    const channel = (await rl.question("Digite o canal: ")).trim();
    const message = (await rl.question("Digite a mensagem: ")).trim();
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