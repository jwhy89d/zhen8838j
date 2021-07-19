独立完成显示所有文章的动态页面

要求:
首页index.html上有一个超链接，为:文章列表。对应的href="/myweb/showAllArticle"

点击该超链接后，浏览器会看到一个动态页面，该页面为一个表格，2列
第一列为文章的标题，第二列为文章的作者


实现:
1:在index.html上添加超链接
2:在ArticleController中添加一个方法:showAllArticle
  该方法可参考UserController的showAllUser方法来生成动态页面
3:在DispatcherServlet上添加一个分支，如果path的值与该超链接
  一致，则调用showAllArticle方法来生成动态页面







