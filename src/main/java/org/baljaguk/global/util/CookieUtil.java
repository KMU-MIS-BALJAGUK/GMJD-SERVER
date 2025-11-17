package org.baljaguk.global.util;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class CookieUtil {
        public static void deleteCookie(HttpServletResponse response,
                                    String name,
                                    @Nullable String domain,
                                    boolean secure,
                                    @Nullable String sameSite) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s=; Path=/; Max-Age=0; HttpOnly", name));   // 값 비우고 즉시 만료

        // Domain
        if (StringUtils.hasText(domain)) {
            sb.append("; Domain=").append(domain);
        }

        // Secure
        if (secure) {                       // prod ⇒ true, local ⇒ false
            sb.append("; Secure");
        }

        // SameSite
        if (StringUtils.hasText(sameSite)) {
            sb.append("; SameSite=").append(sameSite);
        }

        response.addHeader("Set-Cookie", sb.toString());
    }
}
