// main.js

import { CONFIG } from './config.js';
import { ApiUserRepository } from './infrastructure/API/ApiUserRepository.js'
import { MockApiUserRepository } from '../tests/mockDb/MockApiUserRepository.js'
import { App } from './application/state/AppState.js';
import { AuthAdapter } from './application/adapters/AuthAdapter.js';
import { CookieTokenRepository } from './infrastructure/storage/CookieTokenRepository.js';
import { Router } from './infrastructure/routing/Router.js';
import { getRoutes } from './infrastructure/routing/routes.js';
import { ViewFactory } from './infrastructure/UI/views/ViewFactory.js';
import { NavigationDispatcher } from './application/navigation/NavigationDispatcher.js';
import { AuthService } from './application/service/AuthService.js';


const isDevelopment = false;

const tokenRepo = new CookieTokenRepository();
const userRepo = isDevelopment 
    ? new MockApiUserRepository() 
    : new ApiUserRepository(CONFIG.API_BASE_URL);


const authAdapter = new AuthAdapter(userRepo, tokenRepo);
const appState = new App();


const authService = new AuthService(authAdapter ,appState)
const viewFactory = new ViewFactory(authService, appState);
const appRoutes = getRoutes(viewFactory);


const container = document.getElementById("app");

const dispatcher = new NavigationDispatcher(appState, viewFactory, container, authService);
const router = new Router(appRoutes, dispatcher);


dispatcher.setRouter(router); 
window.addEventListener('hashchange', () => router.handleRoute());

async function init() {
  await authService.checkToken(); 
  router.handleRoute();
}

init();

