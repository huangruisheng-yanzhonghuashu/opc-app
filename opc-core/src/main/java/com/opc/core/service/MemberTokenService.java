package com.opc.core.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.opc.common.constant.CacheConstants;
import com.opc.common.constant.Constants;
import com.opc.common.core.redis.RedisCache;
import com.opc.common.utils.ServletUtils;
import com.opc.common.utils.http.UserAgentUtils;
import com.opc.common.utils.ip.AddressUtils;
import com.opc.common.utils.ip.IpUtils;
import com.opc.common.utils.uuid.IdUtils;
import com.opc.core.domain.vo.MemberLoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class MemberTokenService
{
    private static final Logger log = LoggerFactory.getLogger(MemberTokenService.class);

    @Value("${member.token.header:Member-Authorization}")
    private String header;

    @Value("${member.token.secret:memberSecretKeyForJwtTokenGeneration2024}")
    private String secret;

    @Value("${member.token.expireTime:30}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    public MemberLoginVO getLoginUser(HttpServletRequest request)
    {
        String token = getToken(request);
        if (StringUtils.hasText(token))
        {
            try
            {
                Claims claims = parseToken(token);
                String uuid = (String) claims.get(Constants.MEMBER_LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey, MemberLoginVO.class);
            }
            catch (Exception e)
            {
                log.error("获取会员信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    public void setLoginUser(MemberLoginVO memberLoginVO)
    {
        if (memberLoginVO != null && StringUtils.hasText(memberLoginVO.getToken()))
        {
            refreshToken(memberLoginVO);
        }
    }

    public void delLoginUser(String token)
    {
        if (StringUtils.hasText(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    public String createToken(MemberLoginVO memberLoginVO)
    {
        String token = IdUtils.fastUUID();
        memberLoginVO.setToken(token);
        setUserAgent(memberLoginVO);
        refreshToken(memberLoginVO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.MEMBER_LOGIN_USER_KEY, token);
        claims.put(Constants.JWT_EMAIL, memberLoginVO.getEmail());
        return createToken(claims);
    }

    public void verifyToken(MemberLoginVO memberLoginVO)
    {
        long expireTime = memberLoginVO.getExpireTime().toEpochMilli();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY)
        {
            refreshToken(memberLoginVO);
        }
    }

    public void refreshToken(MemberLoginVO memberLoginVO)
    {
        Instant loginTime = Instant.now();
        memberLoginVO.setLoginTime(loginTime);
        memberLoginVO.setExpireTime(loginTime.plusMillis(expireTime * MILLIS_MINUTE));
        String userKey = getTokenKey(memberLoginVO.getToken());
        redisCache.setCacheObject(userKey, memberLoginVO, expireTime, TimeUnit.MINUTES);
    }

    public void setUserAgent(MemberLoginVO memberLoginVO)
    {
        String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        String ip = IpUtils.getIpAddr();
        memberLoginVO.setIpaddr(ip);
        memberLoginVO.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        memberLoginVO.setBrowser(UserAgentUtils.getBrowser(userAgent));
        memberLoginVO.setOs(UserAgentUtils.getOperatingSystem(userAgent));
    }

    private String createToken(Map<String, Object> claims)
    {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.hasText(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return CacheConstants.MEMBER_LOGIN_TOKEN_KEY + uuid;
    }

    public String getHeader()
    {
        return header;
    }
}