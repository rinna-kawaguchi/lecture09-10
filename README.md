# UserAPI仕様書

## １．概要
・ユーザーの取得、追加、更新、削除を行うRestAPI。  
・JUnitによるテストコードを実装。  
　- UserMapper, UserServiceImplの単体テスト  
　- 結合テスト  
・CIの実装（mainブランチへのpush時、プルリクエスト時に実行）  
　- Checkstyle  
　- 自動テスト

## ２．ユーザーが持つ情報

| 項目       | 型      | 備考          |
|----------|--------|-------------|
| id（ID）   | int    | 登録時は自動採番される |
| name（名前） | String |             |
| age（年齢）  | int    |             |


## ３．起動手順
自分のPCにリポジトリをgit cloneし、エディタでプロジェクトを開く。  
`git clone https://github.com/rinna-kawaguchi/lecture09-10.git`  

Dockerを起動し、ターミナルで以下のコマンドを実行する。  
`docker compose up`  

src/main/java/com.
Lecture09taskApplicationクラスを開き、実行する

Postmanを起動し、実行したい操作に応じてHTTPメソッドの選択、URLの入力、
リクエストボディの入力を行い、Sendボタンを押すとレスポンスが返される。  
URLの共通部分：http://localhost:8080  
各操作に応じたHTTPメソッド、URL、リクエストボディの入力内容は
４．API仕様を参照。  

例：指定したIDのユーザーを更新  
![postmanでユーザーの更新.png](..%2F..%2FDesktop%2F%E3%83%AC%E3%82%A4%E3%82%BA%E3%83%86%E3%83%83%E3%82%AF%2F%E8%AA%B2%E9%A1%8C%E7%94%A8%2F%E8%AA%B2%E9%A1%8C%E7%AC%AC10%E5%9B%9E%2FREADME%2Fpostman%E3%81%A7%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC%E3%81%AE%E6%9B%B4%E6%96%B0.png)

## ４．API仕様

### ・指定したIDのユーザーを取得  
#### URI
`GET /users/{id}`  
#### Request
#### Response
`200` - 指定したIDのユーザーが存在する場合は、
指定したユーザーの情報を返す。  
`404` - 指定したIDのユーザーが存在しない場合は、
エラーメッセージ（This id is not found）を返す。

### ・ユーザーを全件取得
#### URI
`GET /users`
#### Request
#### Response
`200` - 全ユーザーの情報を返す。  

### ・指定した年齢より上のユーザーを取得
#### URI
`GET /users?age={age}`
#### Request
#### Response
`200` - 指定した年齢より上のユーザーの情報を返す。
対象ユーザーがいない場合は空のリストを返す。

### ・ユーザーの登録
#### URI
`POST /users`
#### Request
以下のパラメータをJSON形式で送信。  
・name(string) - ユーザー名  
・age(int) - 年齢
#### Response
`201` - 登録成功。メッセージ（user successfully created）を返す。
`400` - リクエストでname, ageのいずれかが空白、nullの場合は、
エラーメッセージ（Please enter your name and age）を返す。

### ・指定したIDのユーザーを更新
#### URI
`PATCH /users/{id}`
#### Request
以下のパラメータをJSON形式で送信。  
・name(string) - ユーザー名  
・age(int) - 年齢
#### Response
`200` - 更新成功。メッセージ（user successfully created）を返す。
name, ageが空白の場合、その項目は更新しない。
`404` - 指定したIDのユーザーが存在しない場合は、
エラーメッセージ（This id is not found）を返す。

### ・指定したIDのユーザーを削除
#### URI
`DELETE /users/{id}`
#### Request
#### Response
`200` - 削除成功。メッセージ（user successfully deleted）を返す。
`404` - 指定したIDのユーザーが存在しない場合は、
エラーメッセージ（This id is not found）を返す。
