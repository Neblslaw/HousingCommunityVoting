package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VoteDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.JwtAuth;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.VoterState;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.VotingSession;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingReadService;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteHandler extends TextWebSocketHandler {

    private final VotingReadService votingReadService;
    private final VotingWriteService votingWriteService;
    private final ObjectMapper objectMapper;
    Map<UUID, VotingSession> votingSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");
        UUID votingId = (UUID) session.getAttributes().get("ROOM_ID");

        if (!votingSessions.containsKey(votingId)) {
            try {
                VotingSession votingSession = new VotingSession();
                votingSession.setVoting(votingReadService.getVoting(votingId));

                votingSessions.put(votingId, votingSession);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        VotingSession votingSession = votingSessions.get(votingId);
        if (jwtAuth.id().equals(votingSession.getVoting().getOwner())) {
            votingSession.setOwnerSession(session);
            session.sendMessage(new TextMessage("witam szefa"));
        } else if (votingSession.getVoting().getVotingGroups().stream().anyMatch(votingGroup -> votingGroup.getVoters().contains(jwtAuth.id()))) {
            List<VoterState> voter = votingSession.getVoters().stream().filter(sessionState -> sessionState.getUserId().equals(jwtAuth.id())).toList();
            if (voter.isEmpty()) {
                votingSession.getVoters().add(new VoterState(
                        jwtAuth.id(),
                        votingSession.getVoting().getVotingGroups().stream().filter(vg -> vg.getVoters().contains(jwtAuth.id())).map(vg -> vg.getId()).toList(),
                        session
                ));
                session.sendMessage(new TextMessage("witam pajaca"));
            } else {
                voter.getFirst().setSession(session);
                session.sendMessage(new TextMessage("re witam pajaca"));
            }
        }

    }

    private void handleVote(JsonNode body, WebSocketSession session) throws JsonProcessingException, NotFoundException {
        VoteRequestBody req = objectMapper.readValue(body.toString(), VoteRequestBody.class);  //todo try catch
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");
        UUID votingId = (UUID) session.getAttributes().get("ROOM_ID");

        votingWriteService.vote(new VoteDTO(jwtAuth.id(), req.questionId, votingId, req.answer));
    }

    private void handleStartVoting(JsonNode body, WebSocketSession session) throws NotFoundException, JsonProcessingException {
        StartVoteRequestBody req = objectMapper.readValue(body.toString(), StartVoteRequestBody.class);  //todo try catch
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");

        if (votingReadService.getVoting(req.votingId).getOwner().equals(jwtAuth.id())) {
            votingWriteService.startVoting(req.votingId());
        }
    }


    private void handleEndVoting(JsonNode body, WebSocketSession session) throws NotFoundException, JsonProcessingException {
        EndVoteRequestBody req = objectMapper.readValue(body.toString(), EndVoteRequestBody.class);  //todo try catch
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");

        if (votingReadService.getVoting(req.votingId).getOwner().equals(jwtAuth.id())) {
            votingWriteService.endVoting(req.votingId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            System.out.println("Received message: " + message.getPayload());

            Request request = objectMapper.readValue(message.getPayload(), Request.class);

            switch (request.type) {
                case VOTE -> handleVote(request.body, session);
                case START_VOTING -> handleStartVoting(request.body, session);
                case END_VOTING -> handleEndVoting(request.body, session);
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage(VotingExceptionHandler.handleException(e)));
        }

//        if (!session.getAttributes().containsKey("votes")) {
//            session.getAttributes().put("votes", new ArrayList<String>());
//        }
//        ArrayList<String> votes = (ArrayList<String>) session.getAttributes().get("votes");
//        votes.add(message.getPayload());
//        session.sendMessage(new TextMessage(votes.toString()));
//
//        synchronized (this) {
//            if (canVote) {
//                messages.add(message.getPayload());
//                canVote = false;
//            }
//        }
//
//
//        voters.stream().filter(VoterState::isConnected).forEach(
//                session1 -> {
//                    synchronized (session1) {
//                        try {
//                            session1.getSession().sendMessage(new TextMessage(String.join("", messages)));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//        );
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        voters.remove(session);
//        organizers.forEach(
//                organizer -> {
//                    synchronized (organizer) {
//                        try {
//                            organizer.sendMessage(new TextMessage("User disconnected"));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//        );
    }


    private enum RequestType {
        VOTE, START_VOTING, END_VOTING
    }

    private record Request(
            RequestType type,
            JsonNode body
    ) {
    }

    private record VoteRequestBody(
            UUID votingId,
            UUID questionId,
            String answer
    ) {
    }

    private record StartVoteRequestBody(
            UUID votingId
    ) {
    }

    private record EndVoteRequestBody(
            UUID votingId
    ) {
    }
}
