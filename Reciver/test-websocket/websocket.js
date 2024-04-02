window.onload = function() {
    // Connessione al WebSocket
    const socket = new WebSocket("ws://localhost:8018/ws");

    socket.onopen = function(event) {
        console.log("WebSocket connection opened.");
        // Quando la connessione WebSocket è aperta, sottoscrivi al topic
        const subscribeMessage = {
            type: "SUBSCRIBE",
            destination: "/topic" // Modificato il percorso del topic
        };
        socket.send(JSON.stringify(subscribeMessage));
    };

    socket.onmessage = function(event) {
        console.log("Message received:", event.data);
        // Gestisci il messaggio ricevuto dal server WebSocket
        const messageContainer = document.getElementById("message-container");
        const newMessage = document.createElement("p");
        newMessage.textContent = event.data;
        messageContainer.appendChild(newMessage);
    };

    socket.onclose = function(event) {
        console.log("WebSocket connection closed.");
    };

    socket.onerror = function(error) {
        console.error("WebSocket error:", error);
    };
};
