package pl.lodz.p.it.mateusz.gielczynski.VoteService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteEngineImpl;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VoteDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.i18n;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.*;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.*;
import pl.lodz.p.it.mateusz.gielczynski.VotingEngine;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;
import pl.lodz.p.it.mateusz.gielczynski.logger.Sensitive;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
@MethodCallLogged
public class VotingWriteService {


    private final VotingRepository votingRepository;
    private final VoteAbilityRepository voteAbilityRepository;
    private final QuestionRepository questionRepository;
    private final VotingGroupRepository votingGroupRepository;
    private final VoteRepository voteRepository;
    private final VotingResultRepository votingResultRepository;


    public Voting createVoting(Voting voting) {
        return votingRepository.save(voting); //todo exceptions
    }

    public Voting updateVoting(UUID id, Voting voting) throws ConflictException, NotFoundException {
        Voting oldVoting = votingRepository.findById(id).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));
        if (oldVoting.getLockTime() != null) {
            throw new ConflictException(i18n.VOTING_LOCKED);
        }

        if (!oldVoting.getVersion().equals(voting.getVersion())) {
            throw new ConflictException(i18n.VOTING_CONCURRENT_UPDATE);
        }

        oldVoting.setName(voting.getName());
        oldVoting.setOwner(voting.getOwner());
        oldVoting.setQuestions(voting.getQuestions());
        oldVoting.setPlannedStartTime(voting.getPlannedStartTime());
        oldVoting.setVotingGroups(voting.getVotingGroups());

        return votingRepository.save(oldVoting);
    }

    public void deleteVoting(UUID id) throws NotFoundException, ConflictException {
        Voting voting = votingRepository.findById(id).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));
        if (voting.getLockTime() != null) {
            throw new ConflictException(i18n.VOTING_LOCKED);
        }
        if (voting.getStartTime() != null) {
            throw new ConflictException(i18n.VOTING_STARTED);
        }
        voteRepository.deleteVoting(voting);
    }

    public LocalDateTime startVoting(UUID id) throws NotFoundException, ConflictException {
        Voting voting = votingRepository.findById(id).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));
        if (voting.getLockTime() == null) {
            throw new ConflictException(i18n.VOTING_MOT_LOCKED);
        }
        if (voting.getStartTime() != null) {
            throw new ConflictException(i18n.VOTING_STARTED);
        }

        List<UUID> users = voting.getVotingGroups().stream().flatMap(votingGroup -> votingGroup.getVoters().stream()).distinct().toList();
        List<UUID> questions = voting.getQuestions().stream().map(AbstractEntity::getId).toList();

        for (UUID user : users) {
            for (UUID question : questions) {
                voteAbilityRepository.saveAndFlush(new VoteAbility(voting, user, question));
            }
        }

        LocalDateTime startTime = LocalDateTime.now().plusSeconds(20);
        voting.setStartTime(startTime);
        return startTime;
    }

    public void vote(@Sensitive VoteDTO voteDTO) throws NotFoundException {
        if (!voteAbilityRepository.existsByUserAndQuestion(voteDTO.userId(), voteDTO.questionId())) {
            throw new NotFoundException(i18n.VOTE_ABILITY_NOT_FOUND);
        }

        Question question = questionRepository.findById(voteDTO.questionId()).orElseThrow(() -> new NotFoundException(i18n.QUESTION_NOT_FOUND));
        Voting voting = votingRepository.findById(voteDTO.votingId()).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));

        List<VotingGroup> votingGroups = votingGroupRepository.findAllByVoters(List.of(voteDTO.userId()));
        for (VotingGroup votingGroup : votingGroups) {
            Vote vote = new Vote(
                    voting, question, votingGroup, voteDTO.answer()
            );
            voteRepository.saveAndFlush(vote);
        }
        voteAbilityRepository.deleteByUserAndQuestion(voteDTO.userId(), voteDTO.questionId());
        question.setModifiedAt(LocalDateTime.now());
        questionRepository.saveAndFlush(question);
    }

    public void endVoting(UUID id) throws NotFoundException, ConflictException {
        voteAbilityRepository.deleteAllByVotingId(id);

        Voting voting = votingRepository.findById(id).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));

        if (voting.getStartTime() == null) {
            throw new ConflictException(i18n.VOTING_NOT_STARTED);
        }
        if (voting.getEndTime() != null) {
            throw new ConflictException(i18n.VOTING_ENDED);
        }
        voting.setEndTime(LocalDateTime.now());

        List<Vote> votes = voteRepository.findAllByVoting_Id(id);


        VotingEngine votingEngine = new VoteEngineImpl();

        votes
                .stream()
                .collect(Collectors.groupingBy(Vote::getQuestion))
                .entrySet()
                .stream()
                .map(entry ->
                        Map.entry(
                                entry.getKey(),
                                votingEngine.calculate(entry.getValue().stream().map(vote -> new pl.lodz.p.it.mateusz.gielczynski.model.Vote(vote.getGroup().getId(), vote.getAnswer())).toList())
                        )
                ).map(
                        entry ->
                                VotingResult.builder()
                                        .voteCount(entry.getValue().voteCount())
                                        .results(
                                                entry.getValue().results().stream().map(result ->
                                                        VotingResultOption.builder()
                                                                .option(result.option())
                                                                .count(result.count())
                                                                .build()
                                                ).toList()
                                        )
                                        .question(entry.getKey())
                                        .voting(voting)
                                        .build()
                ).forEach(votingResultRepository::saveAndFlush);

        voteRepository.deleteByVoting_Id(id);
    }

    public void lockVoting(UUID id) throws ConflictException, NotFoundException {
        Voting oldVoting = votingRepository.findById(id).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));
        if (oldVoting.getLockTime() != null) {
            throw new ConflictException(i18n.VOTING_LOCKED);
        }
        oldVoting.setLockTime(LocalDateTime.now());
        votingRepository.save(oldVoting);
    }
}
