package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.JwtAuth;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.Question;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.VoterState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class VoteHandler extends TextWebSocketHandler {

    List<WebSocketSession> organizers = new ArrayList<>();
    List<VoterState> voters = new ArrayList<>();
    List<String> messages = new ArrayList<>();
    boolean canVote = true;


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        System.out.println(session.getAttributes().get("AUTH"));
        JwtAuth xd = (JwtAuth) session.getAttributes().get("AUTH");
        System.out.println(xd);
        System.out.println("connection established");
        if (organizers.isEmpty()) {
            organizers.add(session);
        } else {
            if (voters.stream().anyMatch(voter -> voter.getSession().equals(session))) {
                voters.stream().filter(voter -> voter.getSession().equals(session)).forEach(voter -> {
                    voter.setConnected(true);
                });
            } else {
                voters.add(new VoterState(UUID.randomUUID(), UUID.randomUUID(), List.of(new Question("what", List.of("yes", "no"))), session));
            }
        }
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


        voters.stream().filter(VoterState::isConnected).forEach(
                session1 -> {
                    synchronized (session1) {
                        try {
                            session1.getSession().sendMessage(new TextMessage(String.join("", messages)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        voters.remove(session);
        organizers.forEach(
                organizer -> {
                    synchronized (organizer) {
                        try {
                            organizer.sendMessage(new TextMessage("User disconnected"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
}
