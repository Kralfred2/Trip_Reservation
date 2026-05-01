// infrastructure/UI/components/PermissionModifier.js
import { UniversalForm } from './global/UniversalForm.js';
import { StyleLoader } from '../utils/StyleLoader.js';


export class PermissionModifier {
    constructor(targets, currentPermissions, attributes, onSave) {
        StyleLoader.load('src/infrastructure/UI/styles/PermissionModifier.css');
        
        this.targets = targets;
        this.currentPermissions = currentPermissions || [];
        this.attributes = attributes; // e.g., ['Profile', 'Settings', 'Financials']
        this.onSave = onSave;
    }

    render() {
        const container = document.createElement('div');
        container.className = 'modifier-container';

        container.innerHTML = `
            <div class="global-control">
                <input type="checkbox" id="master-toggle">
                <label for="master-toggle">Select Users for Bulk Update</label>
            </div>
            
            <table class="modifier-table">
                <thead>
                    <tr>
                        <th class="col-select">Include</th>
                        <th>User</th>
                        ${this.attributes.map(attr => `
                            <th class="col-attribute">
                                ${attr}
                                <div class="attr-sub-labels">
                                    <span>N</span><span>V</span><span>M</span>
                                </div>
                            </th>`).join('')}
                    </tr>
                </thead>
                <tbody id="modifier-body">
                    ${this.targets.map(user => this.#renderUserRow(user)).join('')}
                </tbody>
            </table>

            <div class="modal-actions">
                <button class="save-btn">Save Changes</button>
            </div>
        `;

        this.#setupEventListeners(container);
        return container;
    }

    #renderUserRow(user) {
        return `
            <tr data-user-id="${user.id}">
                <td class="col-select">
                    <input type="checkbox" class="row-include">
                </td>
                <td class="user-cell">${user.username}</td>
                ${this.attributes.map(attr => this.#renderRadioGroup(user.id, attr)).join('')}
            </tr>
        `;
    }

    #renderRadioGroup(userId, attr) {
        // Find existing permission for this user and attribute
        const existing = this.currentPermissions.find(p => 
            p.targetId === userId && p.attribute === attr
        );
        const currentLevel = existing ? existing.accessLevel : 'NONE'; // NONE, VIEW, or MODIFY

        return `
            <td class="col-attribute">
                <div class="radio-group">
                    <input type="radio" name="perm_${userId}_${attr}" value="NONE" ${currentLevel === 'NONE' ? 'checked' : ''}>
                    <input type="radio" name="perm_${userId}_${attr}" value="VIEW" ${currentLevel === 'VIEW' ? 'checked' : ''}>
                    <input type="radio" name="perm_${userId}_${attr}" value="MODIFY" ${currentLevel === 'MODIFY' ? 'checked' : ''}>
                </div>
            </td>
        `;
    }

    #setupEventListeners(container) {
        const masterToggle = container.querySelector('#master-toggle');
        const saveBtn = container.querySelector('.save-btn');

        masterToggle.onchange = () => {
            container.querySelectorAll('.row-include').forEach(cb => cb.checked = masterToggle.checked);
        };

        saveBtn.onclick = async () => {
            const updates = [];
            const rows = container.querySelectorAll('tbody tr');

            rows.forEach(row => {
                if (row.querySelector('.row-include').checked) {
                    const targetId = row.dataset.userId;
                    const permissions = {};

                    this.attributes.forEach(attr => {
                        const selected = row.querySelector(`input[name="perm_${targetId}_${attr}"]:checked`);
                        permissions[attr] = selected ? selected.value : 'NONE';
                    });

                    updates.push({ targetId, permissions });
                }
            });

            await this.onSave(updates);
        };
    }
}