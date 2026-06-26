const input = document.getElementById("input");
const sendBtn = document.getElementById("sendBtn");
const messages = document.getElementById("messages");

function sendMessage() {
    const text = input.value.trim();
    if (!text) return;
    const emptyState = document.getElementById("emptyState");
    if (emptyState) {
        emptyState.remove();
    }
    addMessage(text, "user");

    input.value = "";

    fetch("/api/chat", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ message: text })
    })
    .then(res => res.json())
    .then(data => {
        addMessage(data.answer, "bot");
    });
}

function addMessage(text, type) {
    const div = document.createElement("div");
    div.classList.add("message", type);
    div.textContent = text;
    messages.appendChild(div);

    messages.scrollTop = messages.scrollHeight;
}

sendBtn.addEventListener("click", sendMessage);

input.addEventListener("keydown", function (e) {
    if (e.key === "Enter") {
        sendMessage();
    }
});