// infrastructure/UI/views/RegisterView.js

export class RegisterView {
    static loadStyles() {
        if (document.getElementById('register-view-css')) return;
        const link = document.createElement('link');
        link.id = 'register-view-css';
        link.rel = 'stylesheet';
        // Path relative to your index.html
        link.href = 'src/infrastructure/UI/styles/RegisterView.css'; 
        document.head.appendChild(link);
    }

    constructor(authService) {
        RegisterView.loadStyles();
        this.authService = authService;
    }

    render() {
        const container = document.createElement("div");
        container.className = "register-container";

        container.innerHTML = `
            <div class="register-card">
                <h2>Create Account</h2>
                <form class="register-form" id="register-form">
                    <input type="text" name="username" placeholder="Username" required>
                    <input type="email" name="email" placeholder="Email Address" required>
                    <input type="password" name="password" placeholder="Password" required>
                    
                    <button type="submit" class="register-btn">Sign Up</button>
                </form>
                <p class="login-link">
                    Already have an account? <a href="#/login">Login here</a>
                </p>
            </div>
        `;

        container.querySelector("#register-form").onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            
            try {
                await this.authService.register(
                    formData.get("username"),
                    formData.get("email"),
                    formData.get("password")
                );
                alert("Registration successful! You can now login.");
                window.location.hash = "/login";
            } catch (err) {
                alert("Registration failed: " + err.message);
            }
        };

        return container;
    }
}