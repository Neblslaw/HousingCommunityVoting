package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

public class InvalidVoteException extends Exception {
    public InvalidVoteException(String message) {
        super(message);
    }
}
