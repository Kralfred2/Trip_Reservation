export const PermissionField = (key, config, currentValue) => {
    // Wrap every item in a common 'field-wrapper' for consistent spacing
    const wrapperClass = config.type === 'CHECKBOX' ? 'perm-row' : 'perm-column';
    
    let inputHtml = '';
    if (config.type === 'CHECKBOX') {
        inputHtml = `<input type="checkbox" name="perm_${key}" ${currentValue ? 'checked' : ''}>`;
    } else if (config.type === 'SELECT') {
        inputHtml = `<select name="perm_${key}">
            ${config.options.map(o => `<option value="${o}" ${currentValue === o ? 'selected' : ''}>${o}</option>`).join('')}
        </select>`;
    } else {
        inputHtml = `<input type="${config.type.toLowerCase()}" name="perm_${key}" value="${currentValue || ''}">`;
    }

    return `
        <div class="${wrapperClass}">
            <label>${config.label}</label>
            ${inputHtml}
        </div>
    `;
};