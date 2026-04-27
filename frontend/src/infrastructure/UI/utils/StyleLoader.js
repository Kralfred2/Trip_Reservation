// infrastructure/UI/utils/StyleLoader.js

export class StyleLoader {
    static loadedStyles = new Set();

    static load(path) {
        // Prevent loading the same file multiple times
        if (this.loadedStyles.has(path)) return;

        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = path;
        
        // Error handling to help you debug paths
        link.onerror = () => {
            console.error(`[StyleLoader] Failed to load style at: ${path}`);
        };

        document.head.appendChild(link);
        this.loadedStyles.add(path);
    }
}