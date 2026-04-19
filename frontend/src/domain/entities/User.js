
export class User {
  constructor({ id, username, email, role = 'guest', permissions = [] }) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
    this.permissions = permissions;
  }


  isAdmin() {
    return this.role === 'ROLE_ADMIN' || this.permissions.includes('all');
  }

  can(permission) {
    return this.isAdmin() || this.permissions.includes(permission);
  }
}