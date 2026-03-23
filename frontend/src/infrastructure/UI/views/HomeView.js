export class HomeView {
  render() {
    const container = document.createElement("div");
    const title = document.createElement("h1");
    title.textContent = "You are logged in";
    container.appendChild(title);

    return container;
  }
}