// infrastructure/UI/components/UniversalForm.js
export class UniversalForm {
    /**
     * @param {Array} fields - Array of { key, label, type, value, options }
     * @param {Function} onSubmit - Function(data) called on save
     */
    constructor(fields, onSubmit) {
        this.fields = fields;
        this.onSubmit = onSubmit;
    }

    render() {
        const form = document.createElement('form');
        form.className = 'universal-form';

        form.innerHTML = `
            <div class="fields-container">
                ${this.fields.map(field => this.#renderField(field)).join('')}
            </div>
            <div class="form-actions">
                <button type="submit" class="save-btn">Save Changes</button>
                <button type="button" class="cancel-btn">Cancel</button>
            </div>
        `;

        form.onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(form);
            const result = {};

            this.fields.forEach(field => {
                const rawValue = formData.get(field.key);
                // Type conversion logic
                if (field.type === 'CHECKBOX') {
                    result[field.key] = rawValue === 'on';
                } else if (field.type === 'NUMBER') {
                    result[field.key] = Number(rawValue);
                } else {
                    result[field.key] = rawValue;
                }
            });

            await this.onSubmit(result);
        };

        return form;
    }

    #renderField(field) {
        const { key, label, type, value, options } = field;
        
        switch (type) {
            case 'CHECKBOX':
                return `
                    <div class="form-row">
                        <label>${label}</label>
                        <input type="checkbox" name="${key}" ${value ? 'checked' : ''}>
                    </div>`;
            case 'DROPDOWN':
                return `
                    <div class="form-column">
                        <label>${label}</label>
                        <select name="${key}">
                            ${options.map(opt => `<option value="${opt}" ${value === opt ? 'selected' : ''}>${opt}</option>`).join('')}
                        </select>
                    </div>`;
            default: // STRING, NUMBER
                return `
                    <div class="form-column">
                        <label>${label}</label>
                        <input type="${type.toLowerCase()}" name="${key}" value="${value || ''}">
                    </div>`;
        }
    }
}