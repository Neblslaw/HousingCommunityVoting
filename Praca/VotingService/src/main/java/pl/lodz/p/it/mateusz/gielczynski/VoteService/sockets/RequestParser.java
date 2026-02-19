package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.EndVoteRequestBody;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.Request;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.StartVoteRequestBody;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request.VoteRequestBody;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.i18n;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class RequestParser {

    private final ObjectMapper objectMapper;


    public Request parseRequest(String requestBody) throws BadRequestException {
        try {
            return objectMapper.readValue(requestBody, Request.class);
        } catch (Exception e) {
            throw new BadRequestException(i18n.CANNOT_PARSE_REQUEST);
        }
    }

    public EndVoteRequestBody parseEndVoteRequest(Object requestBody) throws BadRequestException {
        try {
            return objectMapper.convertValue(requestBody, EndVoteRequestBody.class);
        } catch (Exception e) {
            throw new BadRequestException(i18n.CANNOT_PARSE_REQUEST_BODY);
        }
    }

    public StartVoteRequestBody parseStartVoteRequest(Object requestBody) throws BadRequestException {
        try {
            return objectMapper.convertValue(requestBody, StartVoteRequestBody.class);
        } catch (Exception e) {
            throw new BadRequestException(i18n.CANNOT_PARSE_REQUEST_BODY);
        }
    }

    public VoteRequestBody parseVoteRequest(Object requestBody) throws BadRequestException {
        try {
            return objectMapper.convertValue(requestBody, VoteRequestBody.class);
        } catch (Exception e) {
            throw new BadRequestException(i18n.CANNOT_PARSE_REQUEST_BODY);
        }
    }


}
