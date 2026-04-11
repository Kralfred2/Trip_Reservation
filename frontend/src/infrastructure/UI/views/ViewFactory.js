// infrastructure/ui/ViewFactory.js

import { LoginView } from './LoginView.js'
import { RegisterView } from './RegisterView.js'
import { HomeView } from './HomeView.js'


export class ViewFactory {
  constructor(authService, appState) {
    this.authService = authService;
    this.appState = appState; 
  }

  getLoginView() {
    return new LoginView(this.authService);
  }

  getRegisterView() {
    return new RegisterView(this.authService);
  }

  getHomeView() {
    return new HomeView(this.appState);
  }
}