// infrastructure/UI/utils/PermissionConverter.js

import { PERMISSION_CONFIG } from '../config/PermissionConfig.js';

export class PermissionConverter {
    /**
     * Converts raw User data into UniversalForm fields
     */
    static toFormFields(user) {
        // Find the map (using 'settings' or 'permissions')
        const currentData = user.settings || user.permissions || {};

        return Object.keys(PERMISSION_CONFIG).map(key => {
            const config = PERMISSION_CONFIG[key];
            
            // Map your Config Types to Form Types
            const typeMap = {
                'CHECKBOX': 'CHECKBOX',
                'TEXT': 'STRING',
                'NUMBER': 'NUMBER',
                'SELECT': 'DROPDOWN'
            };

            return {
                key: key,
                label: config.label,
                type: typeMap[config.type] || 'STRING',
                value: currentData[key],
                options: config.options || [] // Only for dropdowns
            };
        });
    }
}