package com.postmission.controller;

import com.postmission.configuration.SecurityConfig;
import com.postmission.model.Ticket;
import com.postmission.model.TicketComments;
import com.postmission.model.dto.ApiMessage;
import com.postmission.model.dto.response.TicketCommentApiResponse;
import com.postmission.model.enums.Status;
import com.postmission.service.TicketCommentService;
import com.postmission.service.TicketService;
import com.postmission.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ticketcomment")
@RequiredArgsConstructor
public class TicketCommentController {

    @Autowired
    private TicketCommentService ticketCommentService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @GetMapping("/list/{ticketNo}")
    public ApiMessage<List<TicketCommentApiResponse>> listCommentsByticket(@PathVariable Long ticketNo) {
        System.out.println("해당 티켓 댓글들 조회 ctrl"+ticketNo);
        List<TicketComments> list = ticketCommentService.findTicketCommentsByTicketId(ticketNo);

        List<TicketCommentApiResponse> ticketCommentApiResponseList = new ArrayList<>();


        list.forEach((ticketComments -> {
            TicketCommentApiResponse ticketCommentApiResponse = TicketCommentApiResponse.builder()
                    .ticketCommentsId(ticketComments.getId())
                    .userId(ticketComments.getUser().getId())
                    .ticketId(ticketNo)
                    .description(ticketComments.getDescription())
                    .createAt(ticketComments.getCreatedAt())
                    .build();
            ticketCommentApiResponseList.add(ticketCommentApiResponse);
        }));

        for(TicketCommentApiResponse t : ticketCommentApiResponseList) System.out.println(t);



        return ApiMessage.RESPONSE(Status.OK, ticketCommentApiResponseList);
    }


    @PostMapping
    public ApiMessage<Long> registTicketComment(HttpServletRequest request, @RequestParam Long ticketId, @RequestParam String comment ) throws Exception {
        System.out.println("해당 티켓 댓글 등록 ctrl");
        Long userId = SecurityConfig.getUserIdFromJwtToken(request);

        System.out.println("유저 아이디 : "+userId);
        System.out.println("댓글 : "+comment);
        System.out.println("해당 티켓 ID" +ticketId);
        LocalDateTime now = LocalDateTime.now();


        TicketComments ticketComment = TicketComments.builder()
                .user(userService.findUserById(userId))
                .ticket(ticketService.getTicketById(ticketId))
                .description(comment)
                .createdAt(now)
                .build();

        Long ticketCommentId = ticketCommentService.registTicketcommnet(ticketComment);

        return ApiMessage.RESPONSE(Status.OK, ticketCommentId);
    }

    @DeleteMapping ("/{ticketcommentNo}")
    public ApiMessage<String> deleteComment(@PathVariable Long ticketcommentNo) throws Exception{
        ticketCommentService.deleteTicketcomment(ticketcommentNo);

        return ApiMessage.RESPONSE(Status.OK, "삭제 성공");
    }
}
