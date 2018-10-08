import { UserModel } from "../models/user";

export class AppGlobals {
         readonly ApiBaseUrl: string = "http://localhost:8080";

         getUTF8(base64string: string, key?: string) {
           const tokenThreeParts = base64string.split(".");
           const utf8String = atob(tokenThreeParts[1]);
           const jsonObject = JSON.parse(utf8String);
           if (!key) {
             return jsonObject;
           }
           return jsonObject[key];
         }

         getUserRole(token: string) {
           const userInfos = this.getUTF8(token);
           if (userInfos) {
             const role = userInfos.scopes[0].authority;
             return role;
           }
           return '';
         }

         getUserIdentifier(token: string): number {
           const userInfos = this.getUTF8(token);
           if (userInfos) {
             const identifier = userInfos['identifier'];
             return identifier;
           }
           return -1;
         }


         getUserEmail(token: string) {
            const userInfos = this.getUTF8(token);
            if (userInfos) {
              const email = userInfos.sub;
              return email;
            }
            return '';
         }

         isAdmin(role: string): boolean {
            return role === 'admin';
         }

         isManager(role: string): boolean {
           return role === 'manager';
         }

         isEmployee(role: string): boolean {
           return role === 'employee';
         }

         createUserModelFromObject(userObject: object): UserModel {
           const user: UserModel = new UserModel('', '' , '', '');
           user.firstName = userObject.hasOwnProperty('firstName') ? userObject['firstName'] : '';
           user.lastName = userObject.hasOwnProperty('lastName') ? userObject['lastName'] : '';
           user.email = userObject.hasOwnProperty('email') ? userObject['email'] : '';
           user.password = userObject.hasOwnProperty('password') ? userObject['password'] : '';
           if (userObject.hasOwnProperty('hisBoss')) {
              user.hisBoss = userObject['hisBoss'];
           }
           if (userObject.hasOwnProperty('role')) {
              user.role = userObject['role'];
           }
           if (userObject.hasOwnProperty('id')) {
              user.id  = Number(userObject['id']);
           }
           return user;
         }
       }
