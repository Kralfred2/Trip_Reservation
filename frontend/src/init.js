// main.js

import { ApiUserRepository } from './infrastructure/API/ApiUserRepository.js'
import { MockApiUserRepository } from '../tests/mockDb/MockApiUserRepository.js'
import { App } from './application/state/AppState.js';
import { AuthAdapter } from './application/adapters/AuthAdapter.js';
import { CookieTokenRepository } from './infrastructure/storage/CookieTokenRepository.js';
import { Router } from './infrastructure/routing/Router.js';
import { CheckAuthentication } from './application/multiplugs/CheckAuthentication.js';
import { ViewFactory } from './infrastructure/UI/views/ViewFactory.js';

const isDevelopment = true;

// 1. Initialize Infrastructure
const tokenRepo = new CookieTokenRepository();
const userRepo = isDevelopment ? new MockApiUserRepository() : new ApiUserRepository(); 

// 2. Initialize Application Logic
const authAdapter = new AuthAdapter(userRepo, tokenRepo);
const appState = new App();
const checkAuth = new CheckAuthentication(authAdapter);

const viewFactory = new ViewFactory(authAdapter, appState);

// 4. Update the routes to use these specific instances
const routes = {
  "/login": { 
    protected: false, 
    // We pass a function (callback) instead of an instance
    createView: () => viewFactory.getLoginView() 
  },
  "/app": { 
    protected: true, 
    createView: () => viewFactory.getHomeView() 
  },
  "/register": {
    protected: false,
    createView: () => viewFactory.getRegisterView()
  }
};

// 5. Initialize Routing
const rootElement = document.getElementById("app"); // This is the 'container'
const router = new Router(appState, routes, rootElement);

async function init() {
  const user = await checkAuth.execute();
  if (user) {
    appState.setUser(user);
  }
  router.handleRoute(); // Trigger initial render based on URL
}

init();

