const uploadStatus = document.getElementById("uploadStatus");
const toast = document.getElementById("toast");
const fileInput = document.getElementById("fileInput");
const dropZone = document.getElementById("dropZone");
const filesList = document.getElementById("filesList");
const uploadBtn = document.getElementById("uploadBtn");

let selectedFiles = [];

function formatSize(bytes) {
    return (bytes / 1024 / 1024).toFixed(2) + " МБ";
}

function updateStatus() {
    if (selectedFiles.length === 0) {
        uploadStatus.textContent = "Готов к загрузке";
    } else {
        uploadStatus.textContent =
            `Выбрано файлов: ${selectedFiles.length}`;
    }
}

function showToast(message) {
    toast.textContent = message;

    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 2500);
}

function renderFiles() {

    filesList.innerHTML = "";

    selectedFiles.forEach((file, index) => {

        const item = document.createElement("div");

        item.className = "file-item";

        item.innerHTML = `
            <div class="file-info">
                <div class="file-name">${file.name}</div>
                <div class="file-size">${formatSize(file.size)}</div>
            </div>

            <button class="remove-btn" data-index="${index}">
                ✕
            </button>
        `;

        filesList.appendChild(item);
    });

    updateStatus();
}

function addFiles(files) {

    [...files].forEach(file => {

        if (file.size > 50 * 1024 * 1024) {
            showToast(`${file.name}: превышен лимит 50 МБ`);
            return;
        }

        const alreadyExists = selectedFiles.some(
            f => f.name === file.name && f.size === file.size
        );

        if (!alreadyExists) {
            selectedFiles.push(file);
        }
    });

    renderFiles();

    // Позволяет повторно выбрать тот же файл
    fileInput.value = "";
}

fileInput.addEventListener("change", e => {
    addFiles(e.target.files);
});

dropZone.addEventListener("dragover", e => {
    e.preventDefault();
    dropZone.classList.add("dragover");
});

dropZone.addEventListener("dragleave", () => {
    dropZone.classList.remove("dragover");
});

dropZone.addEventListener("drop", e => {

    e.preventDefault();

    dropZone.classList.remove("dragover");

    addFiles(e.dataTransfer.files);
});

filesList.addEventListener("click", e => {

    if (!e.target.classList.contains("remove-btn")) {
        return;
    }

    const index = e.target.dataset.index;

    selectedFiles.splice(index, 1);

    renderFiles();
});

uploadBtn.addEventListener("click", async () => {

    if (selectedFiles.length === 0) {
        showToast("Выберите файлы.");
        return;
    }

    uploadStatus.textContent = "Загрузка...";

    uploadBtn.disabled = true;

    try {

        const formData = new FormData();

        selectedFiles.forEach(file => {
            formData.append("files", file);
        });

        console.log("Отправка файлов:", selectedFiles);

        /*
        await fetch("/api/upload", {
            method: "POST",
            body: formData
        });
        */

        uploadStatus.textContent = "Загрузка завершена";

        showToast("Файлы успешно загружены");

        selectedFiles = [];

        renderFiles();

    } catch (error) {

        console.error(error);

        uploadStatus.textContent = "Ошибка загрузки";

        showToast("Не удалось загрузить файлы");

    } finally {

        uploadBtn.disabled = false;
    }
});

updateStatus();