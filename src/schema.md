FORMAT: 1A
 
# Group ユーザ
 
## ユーザのエンドポイント [/v1/users]
 
### ユーザ登録 [POST]
 
#### 処理概要
 
* ユーザ情報を新しく登録する。
* 登録に成功した場合、アクセストークンを返す。
 
+ Request (application/json)
 
    + Headers
 
            Accept: application/json
 
    + Attributes
        + email: test@example.com (string, required) - メールアドレス（format: email）
        + password: abc123 (string, required) - パスワード（pattern: ^[0-9A-Za-z]{6,16}$）
 
+ Response 201 (application/json)
 
    + Attributes
        + accessToken: f58ba22059f5a8aa8f346e0f40987adab326041fac99029c909bef2c6300821a (string, required) - アクセストークン