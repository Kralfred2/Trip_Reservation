// infrastructure/ui/LoginView.js
export class LoginView {
  constructor(authService) {
    this.authService = authService;
  }

  render() {
    const container = document.createElement("div"); 
    
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
    const navContainer = document.createElement("div");
    navContainer.style.marginTop = "1rem";

    const registerLink = document.createElement("a");
    registerLink.href = "#/register";
    registerLink.textContent = "Register";
    registerLink.style.cursor = "pointer";

    navContainer.appendChild(registerLink);
    container.appendChild(navContainer);

    return container;
  }

async handleLogin(email, username, password) {
  const user = await this.authService.login(email, username, password);
console.log("found user :(" );
  if (user) {
    console.log("found user :(");
    window.location.hash = "/app"; 
  } else {
    console.log("login failed :(");
  }
}
}