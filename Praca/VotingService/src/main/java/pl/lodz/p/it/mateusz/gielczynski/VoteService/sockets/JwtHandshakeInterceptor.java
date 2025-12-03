package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.JwtAuth;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;

    private final JwtAuthenticationConverter authenticationConverter;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }

        try {
            attributes.put("ROOM_ID", extractRoomUUID(servletRequest.getURI().getPath()));
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }

        String token = resolveToken(servletRequest.getServletRequest());
        if (token == null) {
            return false;
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);
            JwtAuthenticationToken auth = (JwtAuthenticationToken) authenticationConverter.convert(jwt);
            JwtAuth jwtToken = new JwtAuth(
                    UUID.fromString((String) auth.getTokenAttributes().get("sub")),
                    (String) auth.getTokenAttributes().get("name"),
                    (List<String>) auth.getTokenAttributes().get("roles")
            );
            attributes.put("AUTH", jwtToken);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private UUID extractRoomUUID(String path) {
        return UUID.fromString(path.substring(path.lastIndexOf('/') + 1));
    }
}
