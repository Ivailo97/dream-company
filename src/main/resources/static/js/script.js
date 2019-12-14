'use strict';

document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);

let clearButtonElement = document.querySelector('#clear');

if (clearButtonElement) {
    clearButtonElement.addEventListener('click', clearHistory, true);
}

let stompClient = null;
let name = null;

const http = (function () {

    const send = (url, method, body) => {

        const payload = {
            headers: {
                "Content-Type": "application/json",
            },
            method,
            body: JSON.stringify(body)
        };

        return fetch(url, payload)
            .then(response => {
                console.log(response.json());
            }).then(err => {
                console.log(err);
            });
    };

    const post = (url, body) => send(url, 'POST', body);

    const get = (url) => send(url, 'GET', null);

    return {send, post, get};

}());

connect();

function connect(event) {
    name = document.querySelector('#name').value.trim();

    if (name) {
        let socket = new SockJS('/websocketApp');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, connectionSuccess);
    }
    event.preventDefault();
}

function connectionSuccess() {
    stompClient.subscribe('/topic/javainuse', onMessageReceived);

    stompClient.send("/app/chat.newUser", {}, JSON.stringify({
        sender: name,
        type: 'newUser'
    }))
}

function sendMessage(event) {
    let messageContent = document.querySelector('#chatMessage').value.trim();

    if (messageContent && stompClient) {
        let chatMessage = {
            sender: name,
            content: document.querySelector('#chatMessage').value,
            imageUrl: document.getElementById('imageUrl').value,
            createdOn: Date.now(),
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));

        http.post('/createMessage', {
            type: chatMessage.type,
            sender: chatMessage.sender,
            content: chatMessage.content,
            imageUrl: chatMessage.imageUrl
        }).then();
        document.querySelector('#chatMessage').value = '';
    }

    event.preventDefault();
}

function clearHistory() {
    http.post('/clearHistory').then(() => {
        window.location = '/home'
    });
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    let messageElement = document.createElement('li');
    messageElement.classList.add("m-2");

    if (message.type === 'newUser') {
        messageElement.classList.add('event-data');
        message.content = message.sender + ' has joined the chat!';
    } else if (message.type === 'Leave') {
        messageElement.classList.add('event-data');
        message.content = message.sender + ' has left the chat!';
    } else {
        messageElement.classList.add('message-data');
        let imgElement = document.createElement("img");
        let imgUrl = message.imageUrl;
        imgElement.setAttribute("src", imgUrl);
        messageElement.appendChild(imgElement);


        let usernameElement = document.createElement('span');
        usernameElement.classList.add('text-white');
        let usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);

    let div = document.createElement('div');
    div.classList.add('text-center');
    let smallElement = document.createElement("small");
    smallElement.classList.add("message-date");
    let now = new Date();
    let hours = now.getHours() < 10 ? '0' + now.getHours() : now.getHours();
    let minutes = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes();
    smallElement.innerText = hours + ':' + minutes;
    div.appendChild(smallElement);
    messageElement.appendChild(div);


    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;
}