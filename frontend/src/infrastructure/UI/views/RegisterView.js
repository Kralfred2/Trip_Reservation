export class RegisterView{
      constructor(authAdapter, appState) {
    this.authAdapter = authAdapter;
    this.appState = appState;
  }

  render() {
    const container = document.createElement("div");
    const title = document.createElement("h1");
    title.textContent = "Register";

    const form = document.createElement("form");
    const emailInput = document.createElement("input");
    emailInput.placeholder = "Email";
    
    const submitBtn = document.createElement("button");
    submitBtn.type = "submit";
    submitBtn.textContent = "Register";

    form.append(emailInput, submitBtn);
    container.append(title, form);

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      // Interaction via the Facade
      const user = await this.authAdapter.login(emailInput.value, "password_placeholder");
      
      if (user) {
        // Dispatching a state change instead of manual navigation
        this.appState.setUser(user); 
        window.location.hash = "/app";
      }
    });

    return container;
  }
}