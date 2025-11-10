import readline from "readline/promises";
import { stdin as input, stdout as output } from "node:process";

const rl = readline.createInterface({ input, output });

export async function menu(user){
    console.log("Menu Inicial...");
    console.log("1. Receber mensagens");
    console.log("2. Listar usuários");
    console.log("3. Publicar para usuário");
    console.log("4. Entrar no canal");
    console.log("5. Listar canais");
    console.log("6. Publicar no canal");
    console.log("7. Sair");
    const opcao = Number(await rl.question("Digite uma opção: "));

    if (opcao === 1)  return "rec";
    if (opcao === 2)  return listarUsuarios();
    if (opcao === 3)  return await publicarParaUsuario(user);
    if (opcao === 4)  return await entrarNoCanal();
    if (opcao === 5)  return listarCanais();
    if (opcao === 6)  return await publicarNoCanal(user);
    if (opcao === 7)  { console.log("Tchau!"); return "sair"; }

    console.log("Opção inválida!");
    return "voltar";
}

export async function logarUsuario(){
    const nomeUsuario = (await rl.question("Digite o nome de usuario que deseja logar: ")).trim();
    return { service: "login", data: { user: nomeUsuario, timestamp: new Date().toISOString() } };
}

/* As funções abaixo podem continuar síncronas se não perguntarem nada; 
   se perguntarem, use rl.question(...) também */
function listarUsuarios(){ return { service:"users", data:{ timestamp:new Date().toISOString() } }; }

async function publicarParaUsuario(userOrigin){
    const userDestin = (await rl.question("Digite o nome de usuario destino: ")).trim();
    const message    = (await rl.question("Digite a mensagem: ")).trim();
    return { service:"message", data:{ src:userOrigin, dst:userDestin, message, timestamp:new Date().toISOString() } };
}

async function entrarNoCanal(){
    const nomeCanal = (await rl.question("Digite o nome do canal: ")).trim();
    return { service:"channel", data:{ channel:nomeCanal, timestamp:new Date().toISOString() } };
}

function listarCanais(){ return { service:"channels", data:{ timestamp:new Date().toISOString() } }; }

async function publicarNoCanal(user){
    const channel = (await rl.question("Digite o canal: ")).trim();
    const message = (await rl.question("Digite a mensagem: ")).trim();
    return { service:"publish", data:{ user, channel, message, timestamp:new Date().toISOString() } };
}