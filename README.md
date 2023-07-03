# UserAPIについて

## １．概要
- ユーザーの取得、追加、更新、削除を行うRestAPI。  
- JUnitによるテストコードを実装。
  - UserMapper, UserServiceImplの単体テスト
  - 結合テスト  
- CIの実装（mainブランチへのpush時、プルリクエスト時に実行）
  - Checkstyle
  - 自動テスト
- アプリケーション概略図
![application-schematic.png](images%2Fapplication-schematic.png)

## ２．ユーザーが持つ情報

| 項目       | 型      | 備考          |
|----------|--------|-------------|
| id（ID）   | int    | 登録時は自動採番される |
| name（名前） | String |             |
| age（年齢）  | int    |             |


## ３．起動手順
- 自分のPCにリポジトリをgit cloneする。  
`git clone https://github.com/rinna-kawaguchi/UserAPI.git`

- Dockerを起動し、ターミナルで以下のコマンドを実行する。  
`docker compose up`  

- `src/main/java/com/example/userapi`配下にある
`Lecture09taskApplication.java`クラスを実行する。  

- Postmanを起動し、実行したい操作に応じてHTTPメソッドの選択、URLの入力、
リクエストボディの入力を行う。詳細は４．API仕様書を参照。  

**例：指定したIDのユーザーを更新**
![update-user-example.png](images%2Fupdate-user-example.png)

## ４．API仕様書

### [SwaggerによるAPI仕様書](https://rinna-kawaguchi.github.io/UserAPI/dist/index.html)  

![swagger.png](images%2Fswagger.png)

## ５．自動テスト
**以下のテストコードを実装。**
- 単体テスト
  - Serviceクラス
  - Mapperクラス
- 結合テスト

**自動テストの実行結果**  
![test-result.png](images%2Ftest-result.png)

## ６．AWSへデプロイ
＊現在は停止済み  
- 使用したサービス
  - VPC
  - EC2
  - RDS(MySQL)
  - ロードバランサー

- インフラ構成図  
![infrastructure-diagram.png](images%2Finfrastructure-diagram.png)

## ７．その他
### 苦労した点
- **CRUD処理の実装**  
最初は各クラスの繋がりが理解できず、 エラーが出てもどう解消すれば良いか分かりませんでした。
他の方のコードを繰り返し読み、トライアンドエラーを重ねることで、
コードの中身を少しずつ理解できるようになりました。
- **GitHub Actions**  
最初はエディター上でワークフローファイルを作成し、mainブランチへのpush時と
プルリクエスト時にCIが起動するよう設定したのですが、うまくCIが起動されず、
何度設定を見直しても解決できませんでした。
最終的にはGitHubのActionsのページからワークフローファイルを
作成することで解決できました。

### 今後の展望
- フロントエンドの実装
- 実用的なアプリケーションの作成
- codecovの導入
- AWSへのデプロイ
- 基本情報技術者試験の取得
