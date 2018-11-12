# summer

代理dubbo服务网关 模板

把dubbo接口jar包放入到 指定路径下面 按照application服务分服务包。 
热更新dubbo接口 需要调用DELETE的请求 然后替换对应 dubbo接口服务包 重启dubbo server服务 即实现代理服务更新


用到的框架
dubbo   泛化调用
springboot 提供主框架
javaasist  读取接口包信息(接口文档)
Hystrix 线程隔离 服务降级
