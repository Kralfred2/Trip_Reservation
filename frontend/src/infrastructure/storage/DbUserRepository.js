import { UserRepository } from "../../domain/irepositories/UserRepository.js";

export class DbUserRepository extends UserRepository {

  constructor(database) {
    super()
    this.database = database
  }

  async validateToken(token) {

    const result = await this.database.query(`
      SELECT users.*
      FROM tokens
      JOIN users ON tokens.user_id = users.id
      WHERE tokens.token = ?
      AND tokens.expires_at > NOW()
    `, [token])

    return result[0] ?? null
  }

  async login(){
    
  }

}