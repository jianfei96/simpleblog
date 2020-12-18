# 自作アプリ説明

## Links

- Demo site: 
   - Link: [Sample](https://sample.lixian.download)
   - Login
      - username: admin
      - password: admin
- source code: [sourcecode.zip](https://share.lixian.download/file/sourcecode.zip)
- jar package: [Blog.jar](https://share.lixian.download/file/Blog.jar)

## 詳細

### frameworks:
   - Spring
      - Spring MVC
      - Spring Data JPA
   - Thymeleaf

### jars:
   - urlrewritefilter：rewrite url
   - flexmark：Markdownサポート
   - jsoup：HTML解析

### File structure

#### src/main/java:
   - SpringBoot main
      - BlogApplication.java
   - MVC
      - domain
      - repository
      - service
      - controller
   - 異常処理
      - exception
   - ツール
      - component
   - 属性
      - properties

#### src/main/resources:
   - 静的リソース
      - static
   - テンプレート
      - templates
   - Spring Boot属性
      - application.properties
   - url rewrite属性
      - urlrewrite.xml

### Data Structure:

   - User

| Field    | Type         | Null | Key | Default | Extra |
|----------|--------------|------|-----|---------|-------|
| id       | int(11)      | NO   | PRI | NULL    |       |
| birthday | date         | YES  |     | NULL    |       |
| mail     | varchar(255) | YES  |     | NULL    |       |
| nickname | varchar(255) | YES  |     | NULL    |       |
| passwd   | varchar(255) | NO   |     | NULL    |       |
| sex      | char(1)      | NO   |     | NULL    |       |
| uname    | varchar(255) | NO   | UNI | NULL    |       |

- Post

| Field   | Type         | Null | Key | Default | Extra |
|---------|--------------|------|-----|---------|-------|
| pid     | int(11)      | NO   | PRI | NULL    |       |
| content | longtext     | YES  |     | NULL    |       |
| date    | date         | YES  |     | NULL    |       |
| image   | varchar(255) | YES  |     | NULL    |       |
| time    | time         | YES  |     | NULL    |       |
| title   | varchar(255) | NO   |     | NULL    |       |

- Comment

| Field              | Type         | Null | Key | Default | Extra |
|--------------------|--------------|------|-----|---------|-------|
| cid                | bigint(20)   | NO   | PRI | NULL    |       |
| pid                | int(11)      | NO   | PRI | NULL    |       |
| avatar             | varchar(255) | YES  |     | NULL    |       |
| commenter_email    | varchar(255) | YES  |     | NULL    |       |
| commenter_name     | varchar(255) | YES  |     | NULL    |       |
| content            | longtext     | YES  |     | NULL    |       |
| pre_cid            | bigint(20)   | NO   |     | NULL    |       |
| pre_commenter_name | varchar(255) | YES  |     | NULL    |       |
| pre_content        | longtext     | YES  |     | NULL    |       |
| post_pid           | int(11)      | YES  | MUL | NULL    |       |



##  Screenshots

### Index

![FireShot Capture 003 - Blog Home - sample.lixian.download.png](https://i.loli.net/2020/12/18/oVN73wbga9cDTsL.png)

### Post![FireShot Capture 006 - Simple Blog Post - sample.lixian.download.png](https://i.loli.net/2020/12/18/uWk4mrxU9qNAoeG.png)

### Login

![FireShot Capture 040 - Login - sample.lixian.download.png](https://i.loli.net/2020/12/18/wOub6ZsrcI7L1qa.png)

### User Manager

![FireShot Capture 025 - Change User Information - localhost.png](https://i.loli.net/2020/12/18/oGNc1ru4FLQIO3e.png)

### Post Manager

![FireShot Capture 028 - Post List - sample.lixian.download.png](https://i.loli.net/2020/12/18/Yd1ykgsZGw8jnKb.png)

### Post Editor(Markdown Support)

![FireShot Capture 031 - Add Psot - sample.lixian.download.png](https://i.loli.net/2020/12/18/xSjmAhJtCqnURLN.png)

### Search Engine

![FireShot Capture 034 - Simlpe Blog Search - sample.lixian.download.png](https://i.loli.net/2020/12/18/BlZDP9it6741Hxv.png)

### Comment System

![FireShot Capture 037 - Simple Blog Post - sample.lixian.download.png](https://i.loli.net/2020/12/18/4cvNQUkuzqOSKT8.png)
