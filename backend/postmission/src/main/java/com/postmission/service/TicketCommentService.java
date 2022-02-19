package com.postmission.service;

import com.postmission.model.TicketComments;
import com.postmission.model.dto.response.TicketCommentApiResponse;
import com.postmission.repository.TicketCommentRepository;
import com.postmission.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketCommentService {
    private final TicketCommentRepository ticketCommentRepository;
    private final UserRepository userRepository;

    public List<TicketComments> findTicketCommentsByTicketId(Long ticketNo) {
        List<TicketComments> list = ticketCommentRepository.findTicketCommentsByTicketIdIs(ticketNo);
        return list;
    }

    public Long registTicketcommnet(TicketComments ticketComments) throws Exception {
        TicketComments temp = ticketCommentRepository.save(ticketComments);
        return temp.getId();
    }
    public void deleteTicketcomment(Long ticketcommentId) throws Exception {
        ticketCommentRepository.deleteById(ticketcommentId);
    }

}
