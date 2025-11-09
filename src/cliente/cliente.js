import zmq from "zeromq";
import { menu, cadastrarUsuario } from "./menu.js"

const brokerUrl = "tcp://broker:5555"
const subUrl = "tcp://broker:5557"

async function main(){ // async retorna uma promessa
    /* Traduzido de python (para conectar na URL e conversar com o servidor) */
    const sock = new zmq.Request();
    await sock.connect(brokerUrl);
    console.log("Cliente JS conectado em: ", brokerUrl);
    /* * */
    
    const loginMsg = cadastrarUsuario(); // pergunta o nome e monta {service:"login", data:{...}}
    try{
        await sock.send(JSON.stringify(loginMsg));
        const [loginBuf] = await sock.receive();
        const loginResp = JSON.parse(loginBuf.toString("utf-8"));
        console.log("Login OK:", loginResp);
    }catch(e){
        console.error("Falha no login:", e);
        process.exit(1);
    }

    const myName = loginMsg.data.user;
    const sub = new zmq.Subscriber();
    await sub.connect(subUrl);
    sub.subscribe(myName);
    console.log("Inscrito no tópico:", myName);

    while(true){
        const mensagem = menu();

        if(mensagem === "sair"){
            break;
        }
        if(mensagem === "voltar"){
            continue;
        }

        try{
            await sock.send(JSON.stringify(mensagem)); // Envia a mensagem em JSON
            const [buf] = await sock.receive(); // Recebe "a lista de buffers" (algo assim, não lembro)
            // caso eu mudar para multport (algo assim, não lembro tbm kkkk), ai acho que vou ter que modificar isso
            const resposta = JSON.parse(buf.toString("utf-8")); // Traduz o JSOn para padrão utf-8 (string)
            console.log("Resposta do servidor: ", resposta);
        }catch(erro){
            console.error("Falha na troca de mensagem: ", erro);
            break;
        }
    }

    await sock.close(); // finaliza o cliente
    console.log("Cliente finalizado");
}

main().catch((erro) => { // caso algo dê errado que não foi tratado dentro da main(), devolve essa mensagem de erro
    console.error("Erro inesperado n cliente: ", erro)
    process.exit(1);
})
// se não me engano (já esqueci o que tinha visto), só é possível fazer isso porque a main retorna uma promisse...