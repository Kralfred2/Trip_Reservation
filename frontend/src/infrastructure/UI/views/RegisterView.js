export class RegisterView{
      constructor(authAdapter) {
    this.authAdapter = authAdapter;
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

      const user = await this.authAdapter.login(emailInput.value, "password_placeholder");
      
      if (user) {
        this.appState.setUser(user); 
        window.location.hash = "/app";
      }
    });
    const navContainer = document.createElement("div");
    navContainer.style.marginTop = "1rem";

    const registerLink = document.createElement("a");
    registerLink.href = "#/login";
    registerLink.textContent = "Login";
    registerLink.style.cursor = "pointer";

    navContainer.appendChild(registerLink);
    container.appendChild(navContainer);

    return container;
  }
}