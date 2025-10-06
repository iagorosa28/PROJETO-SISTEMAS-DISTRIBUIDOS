import zmq from "zeromq";
import { menu } from "./menu.js"

const brokerUrl = "tcp://broker:5555"

async function main(){ // async retorna uma promessa
    /* Traduzido de python (para conectar na URL e conversar com o servidor) */
    const sock = new zmq.Request();
    await sock.connect(brokerUrl);
    console.log("Cliente JS conectado em: ", brokerUrl);
    /* * */
    
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