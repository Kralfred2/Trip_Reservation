// infrastructure/ui/ViewFactory.js

import { LoginView } from './LoginView.js'
import { RegisterView } from './RegisterView.js'
import { HomeView } from './HomeView.js'

export class ViewFactory {
  constructor(authAdapter, appState) {
    // These are the "ingredients" needed to build any view
    this.authAdapter = authAdapter;
    this.appState = appState;
  }

  getLoginView() {
    return new LoginView(this.authAdapter, this.appState);
  }

  getHomeView() {
    return new HomeView(this.appState);
  }

  getRegisterView() {
    return new RegisterView(this.authAdapter, this.appState);
  }
}