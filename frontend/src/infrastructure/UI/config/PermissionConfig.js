export const PERMISSION_CONFIG = {
    "can_modify_permissions": { 
        type: 'CHECKBOX', 
        label: "Account Activation",
        description: "Allows the user to enable or disable accounts."
    },
    "can_modify_email": { 
        type: 'TEXT', 
        label: "Primary Email",
        description: "Directly edit the user's registered email address."
    },
    "role": { 
        type: 'SELECT', 
        label: "System Access Tier", 
        options: ["GUEST", "STAFF", "MANAGER", "EXECUTIVE"],
        description: "Determines the priority level for reservations."
    },
    "daily_limit": {
        type: 'NUMBER',
        label: "Daily Reservation Limit",
        description: "Maximum number of bookings allowed per 24h."
    }
};