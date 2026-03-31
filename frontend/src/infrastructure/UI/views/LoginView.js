// infrastructure/ui/LoginView.js
export class LoginView {
  constructor(authAdapter, appState) {
    this.authAdapter = authAdapter;
    this.appState = appState;
  }

  render() {
    const container = document.createElement("div"); // Explicit DOM API
    
    const form = document.createElement("form");
    const emailInput = document.createElement("input");
    emailInput.type = "email";
    emailInput.placeholder = "Enter email";

    const username = document.createElement("input");
    username.type = "username";
    username.placeholder = "Enter username";

    const passInput = document.createElement("input");
    passInput.type = "password";
    passInput.placeholder = "Enter password";

    const submitBtn = document.createElement("button");
    submitBtn.textContent = "Login";

    form.append(emailInput,username, passInput, submitBtn);
    container.appendChild(form);

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      await this.handleLogin(emailInput.value,username.value, passInput.value);
    });

    return container;
  }

  async handleLogin(email,username, password) {
    // The View delegates to the Adapter (Facade)
    const user = await this.authAdapter.login(email,username, password);

    if (user) {
      // Dimension of Logic: Explicitly update the application state
      this.appState.setUser(user); 
      window.location.hash = "/app"; // Trigger navigation context change
    } else {
      alert("Invalid credentials. Please try again.");
    }
  }
}