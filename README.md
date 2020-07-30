## 问答系统


## 资源
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)
[菜鸟教程](https://www.runoob.com/mysql/mysql-insert-query.html)
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)
[spring boot docs](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-error-handling)
## GitHub 登录

[Creating an OAuth App](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

[OkHttp 发出 POST](https://square.github.io/okhttp/)

## 脚本
```sql
create table user
(
	id int auto_increment
		primary key,
	account_id varchar(100) null,
	name varchar(50) null,
	token char(36) null,
	gmt_create bigint null,
	gmt_modified bigint null
);
```
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```


