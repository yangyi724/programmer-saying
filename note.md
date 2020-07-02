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