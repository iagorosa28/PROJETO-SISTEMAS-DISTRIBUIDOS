import zmq from "zeromq";
import { encode, decode } from "@msgpack/msgpack";
import * as msg from "./msg.js";
import { faker } from "@faker-js/faker"; // biblioteca para criar dados fake

const brokerUrl = "tcp://broker:5555"
const proxyUrl = "tcp://proxy:5558"

async function main(){
    const sock = new zmq.Request();
    await sock.connect(brokerUrl);
    console.log("Bot JS conectado em: ", brokerUrl);
    
    const botName = faker.person.firstName(); // criando um nome fake
    const loginMsg = msg.logarUsuario(botName);
    const loginBytes = encode(loginMsg);
    try{
        await sock.send(loginBytes);
        const [loginBuf] = await sock.receive();
        const loginResp = decode(loginBuf);
        console.log("Login OK:", loginResp);
    }catch(e){
        console.error("Falha no login:", e);
        process.exit(1);
    }

    const sub = new zmq.Subscriber();
    await sub.connect(proxyUrl);
    sub.subscribe(botName);
    console.log("Logado como:", botName);

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
        } catch (e) {}
    })().catch(e => console.error("Loop SUB:", e));

    /* * */
    // sla como isso funciona...
    function randomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    const sleep = ms => new Promise(r => setTimeout(r, ms));
    /* * */

    while(true){

        const channelsMsg = msg.listarCanais();
        const channelsBytes = encode(channelsMsg);

        let channelsResp;

        try{
            await sock.send(channelsBytes);
            const [channelsBuf] = await sock.receive();
            channelsResp = decode(channelsBuf);
            console.log("Channels OK:", channelsResp);
        }catch(e){
            console.error("Channels NOT OK:", e);
            process.exit(1);
        }

        const listaChannels = channelsResp.data.lista;
        const valor = randomInt(0, listaChannels.length - 1);
        const channel = listaChannels[valor];
        sub.subscribe(channel);
        console.log("Entrou em:", channel);

        for (let index = 0; index < 10; index++) {
            const mensagemAleatoria = faker.lorem.sentence(); // gerar mensagem aleatória
            const mensagem = msg.publicarNoCanal(botName, channel, mensagemAleatoria);
            try{
                const msgBytes = encode(mensagem);
                await sock.send(msgBytes);
                const [buf] = await sock.receive();
                const resposta = decode(buf);
                console.log("Resposta do servidor: ", resposta);
            }catch(erro){
                console.error("Falha na troca de mensagem: ", erro);
                break;
            }
            await sleep(2000);
        }

        await sleep(2000);
    }

    // const shuttingDown = false;
    // const shutdown = async (code = 0) => {
    //     if (shuttingDown) return;
    //     shuttingDown = true;
    //     try {
    //         try { sub.close(); } catch {}
    //         try { await receiver; } catch {}
    //         try { await sock.close(); } catch {}
    //     } finally {
    //         process.exit(code);
    //     }
    // };

    // await shutdown(0);
    // console.log("Bot finalizado");
}

main().catch((erro) => {
    console.error("Erro inesperado no bot: ", erro)
    process.exit(1);
})