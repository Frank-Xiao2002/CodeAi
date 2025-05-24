package top.frankxxj.codeai.midware.user.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import top.frankxxj.codeai.midware.user.DBUserDetailsService;

@Configuration
class UserServiceConfig {
    @Bean
    @Primary
    UserDetailsService realUserService(DBUserDetailsService dBUserService,
                                       UserCache userCache) {
        CachingUserDetailsService cachingUserDetailsService = new CachingUserDetailsService(dBUserService);
        cachingUserDetailsService.setUserCache(userCache);
        return cachingUserDetailsService;
    }

    @Bean
    UserCache userCache(CacheManager cacheManager) {
        return new SpringCacheBasedUserCache(cacheManager.getCache("users"));
    }
}