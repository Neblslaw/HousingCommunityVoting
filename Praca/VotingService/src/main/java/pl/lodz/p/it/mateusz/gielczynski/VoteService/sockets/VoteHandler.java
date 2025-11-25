package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VoteHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new ArrayList<>();
    List<String> messages = new ArrayList<>();
    boolean canVote = true;


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        System.out.println("connection established");
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        if (!session.getAttributes().containsKey("votes")) {
            session.getAttributes().put("votes", new ArrayList<String>());
        }
        ArrayList<String> votes = (ArrayList<String>) session.getAttributes().get("votes");
        votes.add(message.getPayload());
        session.sendMessage(new TextMessage(votes.toString()));

        synchronized (this) {
            if (canVote) {
                messages.add(message.getPayload());
                canVote = false;
            }
        }


        sessions.forEach(
                session1 -> {
                    synchronized (session1) {
                        try {
                            session1.sendMessage(new TextMessage(String.join("", messages)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
