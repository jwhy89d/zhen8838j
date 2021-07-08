此版本完成用户注册业务操作
上一个版本我们已经将注册页面中表单提交的数据解析完毕并存入到HttpServletRequest对应属性中了

此版本我们完成DispatcherServlet处理请求的环节，增加对业务的处理。

实现:
1:新建一个包:com.webserver.controller
  这个包中保存所有将来用于处理业务的类
2:在controller包中新建处理与用户数据相关的业务类:UserController
3:在UserController中添加reg()方法,用于处理用户注册逻辑
4:在DispatcherServlet处理请求的环节中，首先我们将原来判断路径使用的请求对象中的uri换成
  requestURI.
  原因:uri中可能表示的路径中含有参数，而不是纯请求部分了。
5:如果请求路径是/myweb/reg,则说明这次请求是reg.html页面form表单提交的请求(action决定)
  那么这个请求就是要处理注册业务，因此我们实例化UserController并调用reg方法进行处理即可。