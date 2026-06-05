package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VoteDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event.*;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.EndVoteRequestBody;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.Request;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.StartVoteRequestBody;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.VoteRequestBody;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.JwtAuth;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.VoterState;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.VotingSession;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.AbstractEntity;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VoteAbility;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingReadService;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteHandler extends TextWebSocketHandler {

    private final VotingReadService votingReadService;
    private final VotingWriteService votingWriteService;
    private final RequestParser requestParser;
    private final EventSerializer eventSerializer;
    Map<UUID, VotingSession> votingSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
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
            notifyOrganizer(votingSession.getVoting().getId(), eventSerializer.serialize(new UserEvent(votingSession.getVoting().getId(), jwtAuth.id(), UserEventType.USER_CONNECTED)));
            notifyOrganizer(votingSession.getVoting().getId(), eventSerializer.serialize(new JoinedEvent(
                    votingSession.getVoters().stream().filter(voterState -> voterState.getSession().isOpen()).distinct().map(VoterState::getUserId).toList()
//                    votingSession.getVoting().getVotingGroups().stream().flatMap(votingGroup -> votingGroup.getVoters().stream()).distinct().toList()
            )));
        } else if (votingSession.getVoting().getVotingGroups().stream().anyMatch(votingGroup -> votingGroup.getVoters().contains(jwtAuth.id()))) {
            List<VoterState> voter = votingSession.getVoters().stream().filter(sessionState -> sessionState.getUserId().equals(jwtAuth.id())).toList();
            if (voter.isEmpty()) {
                votingSession.getVoters().add(new VoterState(
                        jwtAuth.id(),
                        votingSession.getVoting().getVotingGroups().stream().filter(vg -> vg.getVoters().contains(jwtAuth.id())).map(AbstractEntity::getId).toList(),
                        session
                ));
                notifyOrganizer(votingSession.getVoting().getId(), eventSerializer.serialize(new UserEvent(votingSession.getVoting().getId(), jwtAuth.id(), UserEventType.USER_CONNECTED)));
                notifyVoter(votingSession.getVoting().getId(), jwtAuth.id(), eventSerializer.serialize(new JoinedEvent(votingReadService.getVoteAbilitiesByVotingAndUser(votingSession.getVoting().getId(), jwtAuth.id()).stream().map(VoteAbility::getQuestion).toList())));
            } else {
                voter.getFirst().setSession(session);
                notifyOrganizer(votingSession.getVoting().getId(), eventSerializer.serialize(new UserEvent(votingSession.getVoting().getId(), jwtAuth.id(), UserEventType.USER_RECONNECTED)));
                notifyVoter(votingSession.getVoting().getId(), jwtAuth.id(), eventSerializer.serialize(new JoinedEvent(votingReadService.getVoteAbilitiesByVotingAndUser(votingSession.getVoting().getId(), jwtAuth.id()).stream().map(VoteAbility::getQuestion).toList())));
            }
        }
    }

    private void handleVote(Object body, WebSocketSession session) throws NotFoundException, BadRequestException {
        VoteRequestBody req = requestParser.parseVoteRequest(body);
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");
        UUID votingId = (UUID) session.getAttributes().get("ROOM_ID");

        votingWriteService.vote(new VoteDTO(jwtAuth.id(), req.questionId(), votingId, req.answer()));

        notifyOrganizer(req.votingId(), eventSerializer.serialize(new UserVotedEvent(req.votingId())));
    }

    private void handleStartVoting(Object body, WebSocketSession session) throws NotFoundException, BadRequestException, ConflictException {
        StartVoteRequestBody req = requestParser.parseStartVoteRequest(body);
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");

        if (votingReadService.getVoting(req.votingId()).getOwner().equals(jwtAuth.id())) {
            votingWriteService.startVoting(req.votingId());
        }

        notifyVoters(req.votingId(), eventSerializer.serialize(new VotingEvent(req.votingId(), VotingEventType.VOTING_STARTED)));
        notifyOrganizer(req.votingId(), eventSerializer.serialize(new VotingEvent(req.votingId(), VotingEventType.VOTING_STARTED)));
    }


    private void handleEndVoting(Object body, WebSocketSession session) throws NotFoundException, BadRequestException, ConflictException {
        EndVoteRequestBody req = requestParser.parseEndVoteRequest(body);
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");

        if (votingReadService.getVoting(req.votingId()).getOwner().equals(jwtAuth.id())) {
            votingWriteService.endVoting(req.votingId());
        }

        notifyVoters(req.votingId(), eventSerializer.serialize(new VotingEvent(req.votingId(), VotingEventType.VOTING_FINISHED)));
        notifyOrganizer(req.votingId(), eventSerializer.serialize(new VotingEvent(req.votingId(), VotingEventType.VOTING_FINISHED)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            Request request = requestParser.parseRequest(message.getPayload());

            switch (request.type()) {
                case VOTE -> handleVote(request.body(), session);
                case START_VOTING -> handleStartVoting(request.body(), session);
                case END_VOTING -> handleEndVoting(request.body(), session);
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage(VotingExceptionHandler.handleException(e)));
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        JwtAuth jwtAuth = (JwtAuth) session.getAttributes().get("AUTH");
        UUID votingId = (UUID) session.getAttributes().get("ROOM_ID");

        notifyOrganizer(votingId, eventSerializer.serialize(new UserEvent(votingId, jwtAuth.id(), UserEventType.USER_DISCONNECTED)));
    }

    private void notifyVoters(UUID votingId, String message) {
        if (!votingSessions.containsKey(votingId)) {
            log.error("Tried to notify voters of inexistent voting {}", votingId);
            return;
        }
        votingSessions.get(votingId).getVoters().stream().filter(voter -> {
            return voter.getSession().isOpen();
        }).forEach(
                voter -> {
                    try {
                        voter.getSession().sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        log.error("Failed to notify voter {} in voting {}", voter.getUserId(), votingId);
                    }
                }
        );
    }

    private void notifyVoter(UUID votingId, UUID voter, String message) {
        if (!votingSessions.containsKey(votingId)) {
            log.error("Tried to notify voters of inexistent voting {}", votingId);
            return;
        }
        votingSessions.get(votingId).getVoters().stream().filter(voterr -> {
            return voterr.getSession().isOpen() && voterr.getUserId().equals(voter);
        }).forEach(
                voterr -> {
                    try {
                        voterr.getSession().sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        log.error("Failed to notify voter {} in voting {}", voterr.getUserId(), votingId);
                    }
                }
        );
    }

    private void notifyOrganizer(UUID votingId, String message) {
        if (!votingSessions.containsKey(votingId)) {
            log.error("Tried to notify organizer of inexistent voting {}", votingId);
            return;
        }
        WebSocketSession session = votingSessions.get(votingId).getOwnerSession();
        if (session == null) {
            log.error("Tried to notify organizer that did not join voting");
            return;
        }
        if (!session.isOpen()) {
            log.error("Tried to notify organizer of inexistent voting");
            return;
        }
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("Failed to notify organizer in voting {}", votingId);
        }

    }
}
