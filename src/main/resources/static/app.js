const archiveListElement = document.getElementById("archiveList");
const missionOutputElement = document.getElementById("missionOutput");
const missionSummaryElement = document.getElementById("missionSummary");
const reportOutputElement = document.getElementById("reportOutput");
const uploadStatusElement = document.getElementById("uploadStatus");
const selectedMissionIdElement = document.getElementById("selectedMissionId");
const missionFileElement = document.getElementById("missionFile");
const reportTypeElement = document.getElementById("reportType");

const state = {
    archive: [],
    selectedMissionId: null
};

document.getElementById("refreshArchiveButton").addEventListener("click", () => fetchArchive(true));
document.getElementById("importButton").addEventListener("click", importMission);
document.getElementById("showReportButton").addEventListener("click", showTextReport);
document.getElementById("downloadReportButton").addEventListener("click", downloadReport);

fetchArchive(false);

async function fetchArchive(showStatus) {
    if(showStatus) {
        setStatus("Архив обновляется...", "neutral");
    }

    try {
        const response = await fetch("/api/missions");
        const archive = await parseJsonResponse(response);
        state.archive = archive;
        renderArchive();

        if(showStatus) {
            setStatus(`Архив обновлен. Записей: ${archive.length}.`, "success");
        }
    } catch(error) {
        renderArchiveError(error.message);
        if(showStatus) {
            setStatus(error.message, "error");
        }
    }
}

async function importMission() {
    const file = missionFileElement.files[0];

    if(!file) {
        setStatus("Сначала выберите файл миссии.", "error");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);
    setStatus("Миссия импортируется...", "neutral");

    try {
        const response = await fetch("/api/missions/import", {
            method: "POST",
            body: formData
        });
        const mission = await parseJsonResponse(response);
        setStatus(`Миссия ${mission.missionId} успешно импортирована.`, "success");
        await fetchArchive(false);
        applyMissionSelection(mission);
    } catch(error) {
        setStatus(error.message, "error");
    }
}

async function loadMission(missionId) {
    try {
        const response = await fetch(`/api/missions/${encodeURIComponent(missionId)}`);
        const mission = await parseJsonResponse(response);
        applyMissionSelection(mission);
    } catch(error) {
        setStatus(error.message, "error");
    }
}

async function showTextReport() {
    const missionId = state.selectedMissionId;

    if(!missionId) {
        setStatus("Сначала выберите миссию из архива.", "error");
        return;
    }

    const reportType = reportTypeElement.value;
    reportOutputElement.textContent = "Формирование отчета...";

    try {
        const response = await fetch(
            `/api/missions/${encodeURIComponent(missionId)}/textreport?reportType=${encodeURIComponent(reportType)}`
        );
        const reportText = await parseTextResponse(response);
        reportOutputElement.textContent = reportText;
        setStatus(`Текстовый отчет ${reportType} сформирован.`, "success");
    } catch(error) {
        reportOutputElement.textContent = "Не удалось сформировать отчет.";
        setStatus(error.message, "error");
    }
}

async function downloadReport() {
    const missionId = state.selectedMissionId;

    if(!missionId) {
        setStatus("Сначала выберите миссию из архива.", "error");
        return;
    }

    const reportType = reportTypeElement.value;

    try {
        const response = await fetch(
            `/api/missions/${encodeURIComponent(missionId)}/filereport?reportType=${encodeURIComponent(reportType)}`
        );

        if(!response.ok) {
            throw new Error(await readProblem(response));
        }

        const blob = await response.blob();
        const objectUrl = URL.createObjectURL(blob);
        const link = document.createElement("a");
        const contentDisposition = response.headers.get("Content-Disposition");
        const fileName = extractFileName(contentDisposition) || `mission-${missionId}-${reportType}.txt`;

        link.href = objectUrl;
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        link.remove();
        URL.revokeObjectURL(objectUrl);

        setStatus(`Файловый отчет ${reportType} скачан.`, "success");
    } catch(error) {
        setStatus(error.message, "error");
    }
}

function applyMissionSelection(mission) {
    state.selectedMissionId = mission.missionId;
    selectedMissionIdElement.value = mission.missionId;
    renderMissionSummary(mission);
    missionOutputElement.textContent = JSON.stringify(mission, null, 2);
    highlightSelectedMission();
}

function renderArchive() {
    if(state.archive.length === 0) {
        archiveListElement.innerHTML = "<p class=\"placeholder\">Архив пуст. Импортируйте первую миссию.</p>";
        return;
    }

    archiveListElement.innerHTML = "";

    for(const mission of state.archive) {
        const button = document.createElement("button");
        button.type = "button";
        button.className = "archive-item";
        button.dataset.missionId = mission.missionId;
        button.innerHTML = `
            <span class="archive-item__title">${escapeHtml(mission.missionId)}</span>
            <span class="archive-item__meta">
                Дата: ${escapeHtml(mission.date || "—")}<br>
                Локация: ${escapeHtml(mission.location || "—")}<br>
                Итог: ${escapeHtml(mission.outcome || "—")}
            </span>
        `;
        button.addEventListener("click", () => loadMission(mission.missionId));
        archiveListElement.appendChild(button);
    }

    highlightSelectedMission();
}

function renderArchiveError(message) {
    archiveListElement.innerHTML = `<p class="placeholder">Не удалось загрузить архив: ${escapeHtml(message)}</p>`;
}

function renderMissionSummary(mission) {
    missionSummaryElement.className = "mission-summary";
    missionSummaryElement.innerHTML = `
        ${renderSummaryItem("Mission ID", mission.missionId)}
        ${renderSummaryItem("Дата", mission.date)}
        ${renderSummaryItem("Локация", mission.location)}
        ${renderSummaryItem("Итог", mission.outcome)}
        ${renderSummaryItem("Урон", mission.damageCost)}
        ${renderSummaryItem("Техник", mission.techniques?.length ?? 0)}
        ${renderSummaryItem("Колдунов", mission.sorcerers?.length ?? 0)}
    `;
}

function renderSummaryItem(label, value) {
    return `
        <div class="mission-summary__item">
            <span class="mission-summary__label">${escapeHtml(label)}</span>
            <span class="mission-summary__value">${escapeHtml(value ?? "—")}</span>
        </div>
    `;
}

function highlightSelectedMission() {
    for(const item of archiveListElement.querySelectorAll(".archive-item")) {
        item.classList.toggle("is-active", item.dataset.missionId === state.selectedMissionId);
    }
}

function setStatus(message, type) {
    uploadStatusElement.textContent = message;
    uploadStatusElement.className = `status-card status-card--${type}`;
}

async function parseJsonResponse(response) {
    if(!response.ok) {
        throw new Error(await readProblem(response));
    }

    return response.json();
}

async function parseTextResponse(response) {
    if(!response.ok) {
        throw new Error(await readProblem(response));
    }

    return response.text();
}

async function readProblem(response) {
    const contentType = response.headers.get("Content-Type") || "";

    if(contentType.includes("application/json") || contentType.includes("application/problem+json")) {
        const payload = await response.json();
        return payload.detail || payload.title || `Ошибка ${response.status}`;
    }

    const text = await response.text();
    return text || `Ошибка ${response.status}`;
}

function extractFileName(contentDisposition) {
    if(!contentDisposition) {
        return null;
    }

    const match = contentDisposition.match(/filename="([^"]+)"/);
    return match ? match[1] : null;
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
}
