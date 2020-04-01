oauth2的实现验证方式为客户端方式：client_credentials
建表语句在oauth2.sql文件中，目前用到表为oauth_client_details

以JdbcClientDetailsService为例：
调用http://localhost:8080/oauth/token
执行流程：
1.DaoAuthenticationProvider在该类中通过client_id去查询client信息
select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?
2.在ClientCredentialsTokenEndpointFilter.attemptAuthentication方法中进行请求参数和数据库中参数对比
3.请求TokenEndpoint.postAccessToken方法

注：通过设置日志
logging:
  level:
    root: debug去追踪代码执行流程
    
通过keytool生成JKS证书文件：
keytool.exe -genkey -alias mytest -keyalg RSA -keypass keystorepass -keystore D:\test.jks -storepass keystorepass
解析文件：
keytool.exe -list -rfc -keystore D:\workspace\springcloud\oauth\src\main\resources\demojwt.jks -storepass keystorepass
