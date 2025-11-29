package org.baljaguk.domain.chat.config;
import lombok.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest serverHttpRequest,
                                   @NonNull ServerHttpResponse serverHttpResponse,
                                   @NonNull WebSocketHandler webSocketHandler,
                                   @NonNull Map<String, Object> attributes) {

        if (serverHttpRequest instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();

            // permessage-deflate 제거
            String ext = req.getHeader("Sec-WebSocket-Extensions");
            if (ext != null && ext.contains("permessage-deflate")) {
                req.setAttribute("Sec-WebSocket-Extensions", "");
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest,
                               @NonNull ServerHttpResponse serverHttpResponse,
                               @NonNull WebSocketHandler webSocketHandler,
                               Exception exception) {
    }
}
