export class UserModel {
    constructor(
                  public firstName: string,
                  public lastName: string,
                  public email: string,
                  public password: string,
                  public hisBoss?: UserModel,
                  public role?: string,
                  public id?: number
    ) {}
}
