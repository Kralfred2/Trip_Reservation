export class RegisterView {
  constructor(authAdapter, appState) {
    this.authAdapter = authAdapter;
    this.appState = appState;
  }

  render() {
    const container = document.createElement("div");
    const title = document.createElement("h1");
    title.textContent = "Create Account";

    const form = document.createElement("form");
    form.style.display = "flex";
    form.style.flexDirection = "column";
    form.style.gap = "10px";
    form.style.maxWidth = "300px";

    const usernameInput = document.createElement("input");
    usernameInput.placeholder = "Username";
    usernameInput.required = true;

    const emailInput = document.createElement("input");
    emailInput.type = "email";
    emailInput.placeholder = "Email";
    emailInput.required = true;

    const passwordInput = document.createElement("input");
    passwordInput.type = "password";
    passwordInput.placeholder = "Password";
    passwordInput.required = true;

    // Role Selection (Temporary for Admin creation)
    const roleLabel = document.createElement("label");
    roleLabel.textContent = "Select Role:";
    
    const roleSelect = document.createElement("select");
    const roles = ["ROLE_GUEST", "ROLE_ADMIN"];
    roles.forEach(role => {
      const option = document.createElement("option");
      option.value = role;
      option.textContent = role.replace("ROLE_", "");
      roleSelect.appendChild(option);
    });

    const submitBtn = document.createElement("button");
    submitBtn.type = "submit";
    submitBtn.textContent = "Register";

    form.append(
      usernameInput, 
      emailInput, 
      passwordInput, 
      roleLabel, 
      roleSelect, 
      submitBtn
    );
    
    container.append(title, form);

    form.addEventListener("submit", async (e) => {
      e.preventDefault();

      // Use a register method on your adapter instead of login
      const success = await this.authAdapter.register({
        username: usernameInput.value,
        email: emailInput.value,
        password: passwordInput.value,
        role: roleSelect.value
      });
      
      if (success) {
        alert("Registration successful! Please log in.");
        window.location.hash = "/login";
      } else {
        alert("Registration failed.");
      }
    });

    const navContainer = document.createElement("div");
    navContainer.style.marginTop = "1rem";
    const loginLink = document.createElement("a");
    loginLink.href = "#/login";
    loginLink.textContent = "Already have an account? Login";
    navContainer.appendChild(loginLink);
    container.appendChild(navContainer);

    return container;
  }
}