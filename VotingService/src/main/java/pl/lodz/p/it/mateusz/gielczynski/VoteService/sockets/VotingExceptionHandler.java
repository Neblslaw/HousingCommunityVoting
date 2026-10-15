package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import pl.lodz.p.it.mateusz.gielczynski.exceptions.ApplicationException;

public class VotingExceptionHandler {

    static String handleException(Exception e) {
        System.out.println(e);
        e.printStackTrace();
        switch (e) {
            case ApplicationException applicationException -> {
                return applicationException.getTitle();
            }
            default -> {
                return "Unknown exception";
            }
        }
    }

}
