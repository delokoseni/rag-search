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

    setTimeout(() => {
        addMessage("Это ответ от RAG (пока заглушка)", "bot");
    }, 500);
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