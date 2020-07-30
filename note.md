```html
分页：
前端：传入 page, size
|
indexController：更新登录信息，调用 questionService 获取 PaginationDTO 以得到 questions 和分页信息
|
questionService：调用 paginationDTO.setPagination() 计算分页栏的展示信息【这里用到了 count() 查询，查询记录总数】，根据 offset : size*(page-1) 得到 questionDTO【这里用到了 select limit 查询】并放入 paginationDTO 中
|
前端：获得 paginationDTO，展示问题列表和分页
|
前端：用户点击分页产生新的 page，再次传入后端，迭代
```

# 登录

1. 从 github 获取所有信息之后，不直接插入新用户，先判断数据库中是否有该用户，再判断是插入还是更新

对于github用户，唯一的就是accountId，所以使用github登录成功，要使用accountId在数据库中查一下有没有这个数据，如果有这个数据就把最新的token更新进去，相当于之前的登录态是不需要的，需要把token刷新一下

具体操作：
如果通过数据库能够查到accountId等于当前登录成功的accountId，就把当前数据库的token更新；如果没有就做插入操作

# 维持登录态

拦截器，拦截每个页面，验证cookie中是否有与user表中的token字段相同的值，若有，显示该用户的登录态；否则，未登录
https://blog.csdn.net/qq_35098526/article/details/88734991

# 登出

把 session 中的 user 和 cookie 中的 token 都删除

# 问题详情页面

### 编辑逻辑

若发布人是登录用户，显示编辑按钮，否则不显示


# Mybatis 逆向工程

每次修改 Mapper 和 Model 会很麻烦，用逆向工程可以让我们不用写sql语句和model，mybatis自动帮我们产生

# 错误页面 AOP

可能产生错误的地方有很多个，若在这些地方都加上判断逻辑很杂乱，则想到封装，AOP

# 回复

Spring mvc 自动序列化，发序列化

JS 局部刷新

# Bug解决

拦截器把静态资源拦截了