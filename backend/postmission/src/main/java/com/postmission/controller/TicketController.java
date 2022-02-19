package com.postmission.controller;

import com.postmission.component.Uploader;
import com.postmission.configuration.SecurityConfig;
import com.postmission.model.Ticket;
import com.postmission.model.TicketComments;
import com.postmission.model.dto.ApiMessage;
import com.postmission.model.dto.response.CollectionsApiResponse;
import com.postmission.model.dto.response.TicketApiResponse;
import com.postmission.model.enums.Status;
import com.postmission.repository.MusicalInfoRepository;
import com.postmission.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;


@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    //private final MusicalService musicalService;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private MusicalService musicalService;
    @Autowired
    private UserHasTrophyService userHasTrophyService;
    @Autowired
    private MusicalInfoRepository musicalInfoRepository;
    @Autowired
    private TicketCommentService ticketCommentService;

    private final Uploader uploader;

/*
    @GetMapping("/user")
    public ApiMessage<List<TicketApiResponse>> getTicketInfo(HttpServletRequest request) {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Ticket> tickets = ticketService.findTicketListByUserId(userIdFromJwtToken);

        List<TicketApiResponse> ticketApiResponseList = new ArrayList<>();

        tickets.forEach((ticket -> {
//           Musical findMusical = musicalService.findMusicalListByTicketId(ticket.getMusical().getId());
//            TicketApiResponse ticketApiResponse = TicketApiResponse.builder()
//                    .actors(ticket.getActors())
//                    .description(ticket.getDescription())
//                    .ranking(ticket.getRanking())
//                    .watchedDate(ticket.getWatchedDate())
//                    .userImage(ticket.getUserImage())
//                    .spoiler(ticket.isSpoiler())
//                    .summary(ticket.getSummary())
//                    .isPrivate(ticket.isPrivate())
//                    .poster(findMusical.getPoster())
//                    .ticketPrice(findMusical.getTicketPrice())
//                    .runtime(findMusical.getRuntime())
//                    .endDate(findMusical.getEndDate())
//                    .startDate(findMusical.getStartDate())
//                    .name(findMusical.getName())
//                    .place(findMusical.getName())
//                    .build();
//            ticketApiResponseList.add(ticketApiResponse);
        }));

        return ApiMessage.RESPONSE(Status.OK, ticketApiResponseList);
    }
*/

//    @PostMapping
//    public ApiMessage<Long> registTicket(HttpServletRequest request, @RequestBody TicketApiResponse ticketResponse) {
//        //티켓 추가
//        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
//
//        //System.out.println("유저아이디 값 : "+userIdFromJwtToken);
//        //ticket 정보 중 사진 멀티파트 ?
//        //System.out.println(ticket);
//
//        Ticket ticket = Ticket.builder()
//                .user(userService.findUserById(userIdFromJwtToken))
//                .makeColor(ticketResponse.getMakeColor())
//                .watchedDate(ticketResponse.getWatchedDate())
//                .actors(ticketResponse.getActors())
//                .description(ticketResponse.getDescription())
//                .ranking(ticketResponse.getRanking())
//                .userImage(ticketResponse.getUserImage())
//                .privateCheck(ticketResponse.isPrivateCheck())
//                .summary(ticketResponse.getSummary())
//                .kakaoAlert(ticketResponse.isKakaoAlert())
//                .name(ticketResponse.getName())
//                .posterPath(ticketResponse.getPosterPath())
//                .place(ticketResponse.getPlace())
//                .seat(ticketResponse.getSeat())
//                .watched(ticketResponse.isWatched())
//                .build();
//
//        System.out.println(ticket);
//
//        try {
//            Long id = ticketService.registTicket(ticket);
//            return ApiMessage.RESPONSE(Status.OK, id);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiMessage.RESPONSE(Status.INTERNAL_SERVER_ERROR);
//        }
//
//    }

@PostMapping
public ApiMessage<Long> registTicketWithImages(HttpServletRequest request, @RequestPart("data") TicketApiResponse ticketResponse,
                                                @RequestPart(value = "ticket_images", required = false) List<MultipartFile> images) throws IOException {

    Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
    String saveName=null;

    if (images ==null || images.get(0).getSize()==0) { //이미지 업로드 없을 때
        //saveName NULL 값 그대로
    }
    else  { //이미지 업로드 있을 때
        saveName = new String();
        String [] fileName = new String[images.size()];

        for(int i =0; i<images.size(); i++) {
            fileName[i] = uploader.upload(images.get(i), "ticket_images");
            saveName += fileName[i]+"||";
        }
        int splitLen = saveName.length()-2;
        saveName = saveName.substring(0, splitLen);
        System.out.println(saveName);
    }


    Ticket ticket = Ticket.builder()
            .user(userService.findUserById(userIdFromJwtToken))
            .makeColor(ticketResponse.getMakeColor())
            .watchedDate(ticketResponse.getWatchedDate())
            .actors(ticketResponse.getActors())
            .description(ticketResponse.getDescription())
            .ranking(ticketResponse.getRanking())
            .userImage(saveName)
            .spoiler(ticketResponse.isSpoiler())
            .privateCheck(ticketResponse.isPrivateCheck())
            .summary(ticketResponse.getSummary())
            .kakaoAlert(ticketResponse.isKakaoAlert())
            .name(ticketResponse.getName())
            .posterPath(ticketResponse.getPosterPath())
            .place(ticketResponse.getPlace())
            .seat(ticketResponse.getSeat())
            .watched(ticketResponse.isWatched())
            .build();

    try {
        Long id = ticketService.registTicket(ticket);
        if(ticketResponse.isWatched()) { // 이미 관람한 티켓에 한해서
            userHasTrophyService.checkNumOfTicketTrophy(userIdFromJwtToken); // 티켓 수 관련 트로피 체크
        }
        if(ticketResponse.getRanking()==5) {
            userHasTrophyService.check5RankTicketTrophy(userIdFromJwtToken); // 평점5점 트로피 체크
        }
        return ApiMessage.RESPONSE(Status.OK, id);

    } catch (Exception e) {
        e.printStackTrace();
        return ApiMessage.RESPONSE(Status.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping("/getImages/{ticketNo}")
    public ApiMessage<String> getTicketImages(HttpServletRequest request, @PathVariable("ticketNo") Long ticketNo) throws Exception {
        System.out.println("사진 이름찾기 || 해당 티켓 id" +ticketNo );
        Ticket t = ticketService.getTicketById(ticketNo);

        return ApiMessage.RESPONSE(Status.OK, t.getUserImage());
    }



    @GetMapping("/{ticketNo}")
    public ApiMessage<TicketApiResponse> getTicket(@PathVariable("ticketNo") Long ticketNo) throws Exception {
        //Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        //티켓 상세 조회
        Ticket ticket = ticketService.getTicketById(ticketNo);
        TicketApiResponse ticketApiResponse = TicketApiResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUser().getId())
                .makeColor(ticket.getMakeColor())
                .watchedDate(ticket.getWatchedDate())
                .actors(ticket.getActors())
                .description(ticket.getDescription())
                .ranking(ticket.getRanking())
                .userImage(ticket.getUserImage())
                .spoiler(ticket.isSpoiler())
                .privateCheck(ticket.isPrivateCheck())
                .summary(ticket.getSummary())
                .kakaoAlert(ticket.isKakaoAlert())
                .name(ticket.getName())
                .posterPath(ticket.getPosterPath())
                .place(ticket.getPlace())
                .seat(ticket.getSeat())
                .watched(ticket.isWatched())
                .build();

        System.out.println(ticketApiResponse);

        if (ticketApiResponse != null) {
            return ApiMessage.RESPONSE(Status.OK, ticketApiResponse);
        } else return ApiMessage.RESPONSE(Status.INTERNAL_SERVER_ERROR);


    }

    @DeleteMapping("/{ticketNo}")
    public ApiMessage<String> deleteTicket(@PathVariable("ticketNo") Long ticketNo) throws Exception {
//        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        //티켓 삭제
        //해당 티켓 댓글들 확인
        List<TicketComments> list = ticketCommentService.findTicketCommentsByTicketId(ticketNo);
        if(list !=null || list.size()>0) {
            //댓글 삭제 먼저 시킴
            for(TicketComments t : list) {
                ticketCommentService.deleteTicketcomment(t.getId());
            }

        }
        ticketService.deleteTicket(ticketNo);

        return ApiMessage.RESPONSE(Status.OK, "삭제 완료");
    }

    @PutMapping("/{ticketNo}")
    public ApiMessage<String> modifyTicket(HttpServletRequest request, @RequestPart("data") TicketApiResponse ticketResponse,
                                         @RequestParam(value = "path", required = false) String url,
                                         @RequestPart(value = "new_ticket_images", required = false) List<MultipartFile> images,
                                         @PathVariable("ticketNo")Long ticketNo) throws Exception{
        //티켓 수정
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        Ticket t = ticketService.getTicketById(ticketNo);

//        if (userIdFromJwtToken != t.getUser().getId() ) {
//            return ApiMessage.RESPONSE(Status.BAD_REQUEST, "수정하려는 티켓 유저가 다릅니다");
//        }
        String saveName =null;
        //기존 사진 삭제
        if( url==null ||url.equals("") ) {
            if(images==null || images.get(0).getSize()==0) {
                //새로운 사진업로드 안된 경우
            }
            else {
                //새로운 사진 업로드 된 경우
                saveName = new String();
                String [] fileName = new String[images.size()];

                for(int i =0; i<images.size(); i++) {
                    fileName[i] = uploader.upload(images.get(i), "ticket_images");
                    saveName += fileName[i]+"||";
                }
                int splitLen = saveName.length()-2;
                saveName = saveName.substring(0, splitLen);
            }
        }
        //기존 사진 있음
        else {
            saveName = new String();
            saveName +=url;
            if(images==null || images.get(0).getSize()==0) {

            }
            else {
                String [] fileName = new String[images.size()];
                saveName +="||";

                for(int i =0; i<images.size(); i++) {
                    fileName[i] = uploader.upload(images.get(i), "ticket_images");
                    saveName += fileName[i]+"||";
                }
                int splitLen = saveName.length()-2;
                saveName = saveName.substring(0, splitLen);
            }
        }

        System.out.println(saveName);
        Ticket ticket = Ticket.builder()
                .user(userService.findUserById(userIdFromJwtToken))
                .makeColor(ticketResponse.getMakeColor())
                .watchedDate(ticketResponse.getWatchedDate())
                .actors(ticketResponse.getActors())
                .description(ticketResponse.getDescription())
                .ranking(ticketResponse.getRanking())
                .userImage(saveName)
                .spoiler(ticketResponse.isSpoiler())
                .privateCheck(ticketResponse.isPrivateCheck())
                .summary(ticketResponse.getSummary())
                .kakaoAlert(ticketResponse.isKakaoAlert())
                .name(ticketResponse.getName())
                .posterPath(ticketResponse.getPosterPath())
                .place(ticketResponse.getPlace())
                .seat(ticketResponse.getSeat())
                .watched(ticketResponse.isWatched())
                .build();

        Long id = ticketService.modifyTicket(ticket, ticketNo);
        if(ticketResponse.isWatched()) { // 이미 관람한 티켓에 한해서
            userHasTrophyService.checkNumOfTicketTrophy(userIdFromJwtToken); // 티켓 수 관련 트로피 체크
        }
        if(ticketResponse.getRanking()==5) {
            userHasTrophyService.check5RankTicketTrophy(userIdFromJwtToken); // 평점5점 트로피 체크
        }
        return ApiMessage.RESPONSE(Status.OK, id+"");

    }


    @GetMapping("/list")
    public ApiMessage<Map<String, List<TicketApiResponse>>> listTicekts(HttpServletRequest request) {
        Long userId = SecurityConfig.getUserIdFromJwtToken(request);
        String startDate = ticketService.getStartDate(userId);
        String endDate = ticketService.getEndDate(userId);

        int startY = Integer.parseInt(startDate.split("-")[0]);
        int startM = Integer.parseInt(startDate.split("-")[1]);
        int endY = Integer.parseInt(endDate.split("-")[0]);
        int endM = Integer.parseInt(endDate.split("-")[1]);

        //반환 할 맵
        Map<String, List<TicketApiResponse>> dateTickets = new HashMap<>();

        for (int i = startY; i <= endY; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == startY && j < startM) continue;

                String date = "";
                if (j < 10) date = i + "-0" + j;
                else date = i + "-" + j;

                List<Ticket> tickets = ticketService.getTicketListByDate(date, userId);
                if (tickets.size() > 0) {
                    //
                    List<TicketApiResponse> ticketApiResponseList = new ArrayList<>();

                    tickets.forEach((ticket -> {

                        TicketApiResponse ticketApiResponse = TicketApiResponse.builder()
                                .id(ticket.getId())
                                .userId(ticket.getUser().getId())
                                .makeColor(ticket.getMakeColor())
                                .watchedDate(ticket.getWatchedDate())
                                .actors(ticket.getActors())
                                .description(ticket.getDescription())
                                .ranking(ticket.getRanking())
                                .userImage(ticket.getUserImage())
                                .spoiler(ticket.isSpoiler())
                                .privateCheck(ticket.isPrivateCheck())
                                .summary(ticket.getSummary())
                                .kakaoAlert(ticket.isKakaoAlert())
                                .name(ticket.getName())
                                .posterPath(ticket.getPosterPath())
                                .place(ticket.getPlace())
                                .seat(ticket.getSeat())
                                .watched(ticket.isWatched())
                                .build();

                        ticketApiResponseList.add(ticketApiResponse);
                    }));

                    dateTickets.put(date, ticketApiResponseList);
                }

                if (i == endY && j == endM) {
                    break;
                }
            }
        }

        List<String> keys = new ArrayList<>(dateTickets.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            System.out.println(String.format("Key : %s, Value : %s", key, dateTickets.get(key)));
        }

        if(dateTickets!=null) return ApiMessage.RESPONSE(Status.OK, dateTickets);
        else return ApiMessage.RESPONSE(Status.INTERNAL_SERVER_ERROR);

    }

    @ApiOperation(value = "티켓 통계")
    @GetMapping(value = {"/stat"})
    public ApiMessage<Map<String,Map<String, Integer>>> getStat(HttpServletRequest request) throws Exception{
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Ticket> list = ticketService.getAllMyTickets(userIdFromJwtToken);
        if(list==null) return ApiMessage.RESPONSE(Status.valueOf("티켓이 존재하지 않음"));
        Map<String, Integer> monthlyMap = new HashMap<>();
        Map<String, Integer> dailyMap = new HashMap<>();
        for(Ticket ticket : list) {
            if(ticket.getWatchedDate()==null) continue;
            String monthStr = ticket.getWatchedDate().format(DateTimeFormatter.ofPattern("MM"));
//            System.out.println(monthStr);
            monthlyMap.put(monthStr,monthlyMap.getOrDefault(monthStr,0)+1);
            String dayStr = ticket.getWatchedDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            dailyMap.put(dayStr,dailyMap.getOrDefault(dayStr,0)+1);
        }
        Set<String> monthSet = monthlyMap.keySet();
        for(String key : monthSet) {
            System.out.println(key+": "+monthlyMap.get(key));
        }
        Set<String> daySet = dailyMap.keySet();
        for(String key : daySet) {
            System.out.println(key+": "+dailyMap.get(key));
        }
        Map<String, Map<String, Integer>> statMap = new HashMap<>();
        statMap.put("월별",monthlyMap);
        statMap.put("요일별",dailyMap);
        return ApiMessage.RESPONSE(Status.OK, statMap);
    }

    @ApiOperation(value = "작품모음집 목록")
    @GetMapping(value = {"/collections"})
    public ApiMessage<List<CollectionsApiResponse>> getCollections(HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Ticket> collections = ticketService.getCollections();
        List<CollectionsApiResponse> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for(Ticket ticket : collections) {
            if(!map.containsKey(ticket.getName())) {
                map.put(ticket.getName(), ticket.getPosterPath());
            }
        }
        for(String name : map.keySet()) {
            CollectionsApiResponse res =  CollectionsApiResponse.builder()
                    .musicalId(musicalInfoRepository.findByName(name).getId()).musicalName(name).posterPath(map.get(name)).build();
            list.add(res);
        }
        return ApiMessage.RESPONSE(Status.OK, list);
    }

    @ApiOperation(value = "작품모음집 작품 하나의 모든 티켓북")
    @GetMapping(value = {"/collections/{musicalId}"})
    public ApiMessage<List<TicketApiResponse>> getAllTicketsByMusicalId(@PathVariable String musicalId, HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Ticket> list = ticketService.getAllTicketsByMusicalId(musicalId);
        List<TicketApiResponse> ticketApiResponseList = new ArrayList<>();
        list.forEach((ticket -> {
            TicketApiResponse ticketApiResponse = TicketApiResponse.builder()
                    .id(ticket.getId())
                    .userId(ticket.getUser().getId())
                    .makeColor(ticket.getMakeColor())
                    .watchedDate(ticket.getWatchedDate())
                    .actors(ticket.getActors())
                    .description(ticket.getDescription())
                    .ranking(ticket.getRanking())
                    .userImage(ticket.getUserImage())
                    .spoiler(ticket.isSpoiler())
                    .privateCheck(ticket.isPrivateCheck())
                    .summary(ticket.getSummary())
                    .kakaoAlert(ticket.isKakaoAlert())
                    .name(ticket.getName())
                    .posterPath(ticket.getPosterPath())
                    .place(ticket.getPlace())
                    .seat(ticket.getSeat())
                    .watched(ticket.isWatched())
                    .build();
            ticketApiResponseList.add(ticketApiResponse);
        }));
        return ApiMessage.RESPONSE(Status.OK,ticketApiResponseList);
    }
}