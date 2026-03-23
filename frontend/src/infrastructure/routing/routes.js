import { LoginView } from "../UI/views/LoginView.js";
import { RegisterView } from "../UI/views/RegisterView.js";
import { HomeView } from "../UI/views/HomeView.js";

export const routes = {
  "/login": {
    protected: false,
    view: new LoginView()
  },

  "/register": {
    protected: false,
    view: new RegisterView()
  },

  "/app": {
    protected: true,
    view: new HomeView()
  }
};