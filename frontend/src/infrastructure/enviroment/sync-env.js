import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const envPath = path.resolve(__dirname, '../../../../secret.env'); 
const configPath = path.resolve(__dirname, '../../config.js');

try {
   
    const envContent = fs.readFileSync(envPath, 'utf8');

    const match = envContent.match(/BACKEND_URL=(.*)/);
    const backendUrl = match ? match[1].trim() : 'http://localhost:8080';

    const fileContent = `export const CONFIG = {
    API_BASE_URL: "${backendUrl}"
};`;

    fs.writeFileSync(configPath, fileContent);
    console.log(`✅ config.js updated with: ${backendUrl}`);

} catch (err) {
    console.error("❌ Error reading secret.env from parent directory:", err.message);
}