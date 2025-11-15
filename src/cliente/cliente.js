import zmq from "zeromq";
import { encode, decode } from "@msgpack/msgpack";
import { menu, logarUsuario} from "./menu.js"

const brokerUrl = "tcp://broker:5555"
const proxyUrl = "tcp://proxy:5558"

async function main(){ // async retorna uma promessa
    /* Traduzido de python (para conectar na URL e conversar com o servidor) */
    const sock = new zmq.Request();
    await sock.connect(brokerUrl);
    console.log("Cliente JS conectado em: ", brokerUrl);
    /* * */
    
    const loginMsg = await logarUsuario(); // pergunta o nome e monta {service:"login", data:{...}}
    const loginBytes = encode(loginMsg);
    try{
        // await sock.send(JSON.stringify(loginMsg));
        await sock.send(loginBytes);
        const [loginBuf] = await sock.receive();
        //const loginResp = JSON.parse(loginBuf.toString("utf-8"));
        const loginResp = decode(loginBuf);
        console.log("Login OK:", loginResp);
    }catch(e){
        console.error("Falha no login:", e);
        process.exit(1);
    }

    const myName = loginMsg.data.user;
    const sub = new zmq.Subscriber();
    await sub.connect(proxyUrl);
    sub.subscribe(myName);
    console.log("Logado como:", myName);

    const receiver = (async () => {
        try {
        for await (const [topicBuf, jsonBuf] of sub) {
            const topic = topicBuf.toString();
            const jsonStr = jsonBuf.toString();
            let json;
            try { json = JSON.parse(jsonStr); }
            catch { json = { message: jsonStr }; }

            console.log(`${json.timestamp} | from[${json.user}] → to[${topic}]`);
            console.log(`=> ${json.message}\n`);
        }
        } catch (e) {
        // erro esperado quando sub.close() é chamado durante shutdown
        }
    })().catch(e => console.error("Loop SUB:", e)); // não lembro disso... medo de tirar kkkkkk vou deixar

    // para finalizar o processo (sempre da erro, mas finaliza kkkkkk)
    const shuttingDown = false;
    const shutdown = async (code = 0) => {
        if (shuttingDown) return;
        shuttingDown = true;
        try {
            try { sub.close(); } catch {}
            try { await receiver; } catch {}
            try { await sock.close(); } catch {}
        } finally {
            process.exit(code);
        }
    };

    while(true){
        const mensagem = await menu(myName);   // <<< agora é assíncrono e não bloqueia o SUB :)
        if (mensagem === "sair") { console.log("saindo..."); break; }
        if (mensagem === "voltar") {  
            continue; 
    }

        try{
            // await sock.send(JSON.stringify(mensagem)); // Envia a mensagem em JSON
            const msgBytes = encode(mensagem);
            await sock.send(msgBytes);
            const [buf] = await sock.receive(); // Recebe "a lista de buffers" (algo assim, não lembro)
            // const resposta = JSON.parse(buf.toString("utf-8")); // Traduz o JSOn para padrão utf-8 (string)
            const resposta = decode(buf);
            console.log("Resposta do servidor: ", resposta);
        if(resposta.service === "channel" && resposta.data.status === "sucesso"){
            const channel = mensagem.data.channel;
            sub.subscribe(channel);
            console.log("Entrou em:", channel);
        }
        }catch(erro){
            console.error("Falha na troca de mensagem: ", erro);
            break;
        }
    }

    await shutdown(0);; // finaliza o cliente
    console.log("Cliente finalizado");
}

main().catch((erro) => { // caso algo dê errado que não foi tratado dentro da main(), devolve essa mensagem de erro
    console.error("Erro inesperado no cliente: ", erro)
    process.exit(1);
})
// se não me engano (já esqueci o que tinha visto), só é possível fazer isso porque a main retorna uma promisse...