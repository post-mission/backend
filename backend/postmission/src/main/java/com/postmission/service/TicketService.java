package com.postmission.service;

import com.postmission.exceptions.NotExistException;
import com.postmission.model.MusicalInfo;
import com.postmission.model.Ticket;
import com.postmission.model.TicketComments;
import com.postmission.model.User;
import com.postmission.model.enums.ErrorMessage;
import com.postmission.repository.MusicalInfoRepository;
import com.postmission.repository.TicketCommentRepository;
import com.postmission.repository.TicketRepository;
import com.postmission.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final MusicalInfoRepository musicalInfoRepository;

    public String getStartDate(Long userId) {
        String startDate = ticketRepository.getStartDate(userId);
        return startDate;
    }

    public String getEndDate(Long userId) {
        String endDate = ticketRepository.getEndDate(userId);
        return endDate;
    }

    public List<Ticket> findTicketListByUserId(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        User user = findUser.orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));

        return ticketRepository.findByUserId(user.getId());
    }


    public List<Ticket> getTicketListByDate(String date, Long userId){
        System.out.println("티켓 서비스 || 검색날짜 : "+date);

        List<Ticket> tickets = ticketRepository.findTicketByDate(date, userId);

//        for(Ticket cur : t) System.out.println(cur);

        return tickets;
    }
    public Long registTicket(Ticket ticket) throws Exception {
        Ticket temp = ticketRepository.save(ticket);
        return temp.getId();
    }

    public Ticket getTicketById(Long ticketId) throws Exception {
        Optional<Ticket> optional = ticketRepository.findById(ticketId);
        Ticket ticket = optional.get();
        return ticket;
    }

    public void deleteTicket(Long ticketId) throws Exception {
        ticketRepository.deleteById(ticketId);
    }

    public Long modifyTicket(Ticket ticket, Long ticketId) throws Exception {


        Optional<Ticket> optional = ticketRepository.findById(ticketId);
        Ticket oldT = optional.get();

        oldT = Ticket.builder()
                .user(ticket.getUser())
                .makeColor(ticket.getMakeColor())
                .watchedDate(ticket.getWatchedDate())
                .actors(ticket.getActors())
                .description(ticket.getDescription())
                .ranking(ticket.getRanking())
                .userImage(ticket.getUserImage())
                .privateCheck(ticket.isPrivateCheck())
                .summary(ticket.getSummary())
                .kakaoAlert(ticket.isKakaoAlert())
                .name(ticket.getName())
                .posterPath(ticket.getPosterPath())
                .place(ticket.getPlace())
                .seat(ticket.getSeat())
                .watched(ticket.isWatched())
                .build();

        ticket.setId(ticketId);
        Ticket temp = ticketRepository.save(ticket);
        return temp.getId();


    }

    public List<Ticket> getAllMyTickets(Long userId) throws Exception {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> getAllMyTicketsWatched(Long userId) throws Exception {
        List<Ticket> list = ticketRepository.findByUserId(userId);
        List<Ticket> watchedList = new ArrayList<>();
        for(Ticket ticket : list) {
            if(ticket.isWatched()) { // 이미 관람한 티켓북만 리스트에 추가
                watchedList.add(ticket);
            }
        }
        return watchedList;
    }

    public int getMostTicketCount(Long userId) throws Exception {
        return ticketRepository.getMostTicketCount(userId);
    }

    public List<Ticket> getCollections() throws Exception {
        return ticketRepository.findByPrivateCheckAndWatched(false,true);
    }

    public List<Ticket> getAllTicketsByMusicalId(String musicalId) throws Exception {
        MusicalInfo musicalInfo = musicalInfoRepository.findById(musicalId).get();
        return ticketRepository.findByName(musicalInfo.getName());
    }
}
