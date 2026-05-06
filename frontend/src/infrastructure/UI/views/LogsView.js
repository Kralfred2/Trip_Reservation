export class LogsView {
  constructor(apiService) {
    this.apiService = apiService;
  }

  async render() {
    const container = document.createElement("div");
    container.innerHTML = `<h1>System Logs</h1><div id="logs-list">Loading...</div>`;

    try {
      const logs = await this.apiService.get("/api/logs"); // Use your BaseApiRepository logic
      const list = container.querySelector("#logs-list");
      list.innerHTML = ""; 

      logs.forEach(log => {
        const logItem = document.createElement("div");
        logItem.className = "log-item";
        logItem.style.padding = "10px";
        logItem.style.borderBottom = "1px solid #ddd";
        logItem.innerHTML = `
            <strong>${log.timestamp}</strong> - 
            <span class="severity-${log.level}">${log.level}</span>: 
            ${log.message}
        `;
        list.appendChild(logItem);
      });
    } catch (err) {
      container.innerHTML = `<p style="color:red">Failed to load logs: ${err.message}</p>`;
    }

    return container;
  }
}