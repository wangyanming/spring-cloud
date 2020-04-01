package com.hds.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Resource
    private AuthenticationManager authenticationManagerBean;

    @Autowired
    private DataSource dataSource;

    @Resource
    private PasswordEncoder passwordEncoder;

    public AuthorizationServerConfigurer(AuthenticationManager authenticationManagerBean, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBean = authenticationManagerBean;
        this.passwordEncoder = passwordEncoder;
    }

    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManagerBean)
                //配置JwtAccessToken转换器
                .accessTokenConverter(accessTokenConverter());
    }

    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * JwtAccessTokenConverter是用来生成token的转换器，
     * 而token令牌默认是有签名的，且资源服务器需要验证这个签名。此处的加密及验签包括两种方式：
     * 对称加密、非对称加密（公钥密钥）对称加密需要授权服务器和资源服务器存储同一key值，而非对称加密
     * 可使用密钥加密，暴露公钥给资源服务器验签
     * @return
     */
    @Bean
    public AccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    /**
     * 使用非对称加密算法来对Token进行签名
     * 通过 JDK 工具生成 JKS 证书文件，并将 keystore.jks 放入resource目录下
     * keytool.exe -genkey -alias mytest -keyalg RSA -keypass keystorepass -keystore D:\test.jks -storepass keystorepass
     * 解析demo.jks，keytool.exe执行文件在C:\Program Files\Java\jdk1.8.0_221\bin>
     * keytool.exe -list -rfc -keystore D:\workspace\springcloud\oauth\src\main\resources\demojwt.jks -storepass keystorepass
     * @return
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("demojwt.jks"), "keystorepass".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "keypairpass".toCharArray());
    }

}