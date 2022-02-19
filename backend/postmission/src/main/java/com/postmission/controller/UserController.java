package com.postmission.controller;

import com.postmission.component.Uploader;
import com.postmission.configuration.SecurityConfig;
import com.postmission.exceptions.AuthenticationException;
import com.postmission.model.Email;
import com.postmission.model.Follow;
import com.postmission.model.Trophy;
import com.postmission.model.User;
import com.postmission.model.dto.ApiMessage;
import com.postmission.model.dto.request.*;
import com.postmission.model.dto.response.*;
import com.postmission.model.enums.ErrorMessage;
import com.postmission.model.enums.Gender;
import com.postmission.model.enums.Status;
import com.postmission.repository.EmailRepository;
import com.postmission.service.TrophyService;
import com.postmission.service.UserHasTrophyService;
import com.postmission.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailRepository emailRepository;
    private final ServletContext servletContext;
    private final TrophyService trophyService;
    private final UserHasTrophyService userHasTrophyService;
    private final Uploader uploader;

    @ApiOperation(value = "회원 가입")
    @PostMapping("/join")
    public ApiMessage<String> joinAdmin(@RequestBody UserApiRequest userApiRequest) {
        User user = User.builder()
                .name(userApiRequest.getName())
                .email(userApiRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userApiRequest.getPassword()))
                .isSocial(false)
                .isDeleted(false)
                .roles("ROLE_USER")
                .age(userApiRequest.getAge())
                .profileImage(userApiRequest.getProfileImage())
                .gender(userApiRequest.getGender())
                .createdAt(LocalDateTime.now())
                .build();
        userService.registerUser(user);
        return ApiMessage.RESPONSE(Status.OK,"반환값 없음");
    }

    @ApiOperation(value = "이메일 보내기")
    @PostMapping("/send-email")
    public ApiMessage<String> sendEmail(@RequestBody EmailApiRequest emailApiRequest){
        String email = emailApiRequest.getEmail();
        if(userService.sendEmailConfirmToken(email)){
            String emailToken = userService.getEmailToken(email);
            return ApiMessage.RESPONSE(Status.OK,emailToken);
        }
        throw new AuthenticationException(ErrorMessage.EMAIL_ERROR);
    }

    @ApiOperation(value = "이메일 중복 확인")
    @PostMapping("/duplicate-email")
    public ApiMessage<String> checkDuplicateEmail(@RequestBody EmailApiRequest emailApiRequest){
        String email = emailApiRequest.getEmail();
        if(userService.checkEmailIsPresent(email)){
            return ApiMessage.RESPONSE(Status.BAD_REQUEST,"이메일 중복 O");
        }else{
            return ApiMessage.RESPONSE(Status.OK,"이메일 중복 X");
        }
    }

    @ApiOperation(value = "이메일 인증")
    @PostMapping("/verify-email")
    public ApiMessage<String> verifyEmail(@RequestBody EmailApiRequest emailApiRequest){
        String email = emailApiRequest.getEmail();
        String token = emailApiRequest.getToken();
        Email findEmail = emailRepository.findById(email).orElseThrow(() -> new AuthenticationException(ErrorMessage.SERVER_ERROR));
        if(userService.checkEmailConfirm(findEmail.getEmailAddress(), token)){
            return ApiMessage.RESPONSE(Status.OK,"인증 완료");
        }
        throw new AuthenticationException(ErrorMessage.EMAIL_ERROR);
    }

    @ApiOperation(value = "팔로우 목록 가져오기")
    @GetMapping("/follow")
    public ApiMessage<List<UserApiResponse>> getFollowList(HttpServletRequest request){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Follow> userFollowedList = userService.getFollowedList(userIdFromJwtToken);

        List<UserApiResponse> userApiResponseList = new ArrayList<>();
        userFollowedList.forEach((follow)->{
            User followingUser = follow.getFollowingUser();
            UserApiResponse userApiResponse = UserApiResponse.builder()
                    .username(followingUser.getName())
                    .profileImage(followingUser.getProfileImage())
                    .build();
            userApiResponseList.add(userApiResponse);
        });
        return ApiMessage.RESPONSE(Status.OK,userApiResponseList);
    }

    @ApiOperation(value = "팔로잉 목록 가져오기")
    @GetMapping("/following")
    public ApiMessage<List<UserApiResponse>> getFollowingList(HttpServletRequest request){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Follow> userFollowingList = userService.getFollowingList(userIdFromJwtToken);

        List<UserApiResponse> userApiResponseList = new ArrayList<>();
        userFollowingList.forEach((follow)->{
            User followingUser = follow.getFollowedUser();
            UserApiResponse userApiResponse = UserApiResponse.builder()
                    .username(followingUser.getName())
                    .profileImage(followingUser.getProfileImage())
                    .build();
            userApiResponseList.add(userApiResponse);
        });
        return ApiMessage.RESPONSE(Status.OK,userApiResponseList);
    }

    @ApiOperation(value = "팔로잉 여부 확인하기")
    @GetMapping("/check-follow/{targetUserId}")
    public ApiMessage<Boolean> checkIsFollowing(HttpServletRequest request,@PathVariable Long targetUserId){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        boolean result = userService.checkUserIsFollowingTargetUser(userIdFromJwtToken, targetUserId);
        return ApiMessage.RESPONSE(Status.OK,result);
    }

    @ApiOperation(value = "팔로우 하기")
    @PostMapping("/follow")
    public ApiMessage<String> followUser(HttpServletRequest request,@RequestBody FollowApiRequest followApiRequest) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        userService.followUser(userIdFromJwtToken,followApiRequest.getTargetUserId());
        userHasTrophyService.checkFollowTrophy(userIdFromJwtToken, followApiRequest.getTargetUserId()); // 팔로우 관련 트로피 체크
        return ApiMessage.RESPONSE(Status.CREATED);
    }

    @ApiOperation(value = "언 팔로우 하기")
    @DeleteMapping("/follow")
    public ApiMessage<String> unFollowUser(HttpServletRequest request,@RequestBody FollowApiRequest followApiRequest){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        userService.unFollowUser(userIdFromJwtToken,followApiRequest.getTargetUserId());
        return ApiMessage.RESPONSE(Status.OK);
    }

    @ApiOperation(value = "프로필 사진 등록하기")
    @PostMapping("/profile")
    public ApiMessage<String> registerUserProfileImage(HttpServletRequest request,
                                                       @RequestParam("profile_image")MultipartFile multipartFile) throws IOException {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        String fileName = uploader.upload(multipartFile,"profile_images");

        userService.registerUserProfileImage(userIdFromJwtToken,fileName);
        return ApiMessage.RESPONSE(Status.OK);
    }

    @ApiOperation(value = "프로필 사진 반환하기")
    @GetMapping("/profile")
    public ApiMessage<String> getUserProfileImage(HttpServletRequest request){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        String profileImage = userService.getProfileImage(userIdFromJwtToken);
        return ApiMessage.RESPONSE(Status.OK,profileImage);
    }

    @ApiOperation(value = "회원정보 가져오기")
    @GetMapping("/user")
    public ApiMessage<UserProfileApiResponse> getUserInfo(HttpServletRequest request){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        User user = userService.getUser(userIdFromJwtToken);

        List<Follow> followedList = userService.getFollowedList(userIdFromJwtToken);
        List<Follow> followingList = userService.getFollowingList(userIdFromJwtToken);

        List<Followings> followingsList = new ArrayList<>();
        List<Follows> followsList = new ArrayList<>();

        followingList.forEach((following -> {
            Followings followings = Followings.builder()
                    .username(following.getFollowedUser().getName())
                    .profileImage(following.getFollowedUser().getProfileImage())
                    .build();
            followingsList.add(followings);
        }));

        followedList.forEach((follow -> {
            Follows follows = Follows.builder()
                    .username(follow.getFollowingUser().getName())
                    .profileImage(follow.getFollowingUser().getProfileImage())
                    .build();
            followsList.add(follows);
        }));

        List<Trophy> selectedTrophies = trophyService.getAllTrophies(userIdFromJwtToken);

        List<Long> trophiesList = new ArrayList<>();

        selectedTrophies.forEach((trophy -> {
            trophiesList.add(trophy.getId());
        }));

        UserProfileApiResponse userProfileApiResponse = UserProfileApiResponse.builder()
                .profileImage(user.getProfileImage())
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .username(user.getName())
                .trophies(trophiesList)
                .follows(followsList)
                .secondCharacter(user.getSecondCharacter())
                .followings(followingsList)
                .build();

        return ApiMessage.RESPONSE(Status.OK,userProfileApiResponse);
    }

    @ApiOperation(value = "지정된 회원정보 가져오기")
    @GetMapping("/user/{userId}")
    public ApiMessage<UserProfileApiResponse> getUserInfoById(HttpServletRequest request,@PathVariable Long userId){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Follow> followedList = userService.getFollowedList(userId);
        List<Follow> followingList = userService.getFollowingList(userId);

        List<Followings> followingsList = new ArrayList<>();
        List<Follows> followsList = new ArrayList<>();

        followingList.forEach((following -> {
            Followings followings = Followings.builder()
                    .username(following.getFollowedUser().getName())
                    .profileImage(following.getFollowedUser().getProfileImage())
                    .build();
            followingsList.add(followings);
        }));

        followedList.forEach((follow -> {
            Follows follows = Follows.builder()
                    .username(follow.getFollowingUser().getName())
                    .profileImage(follow.getFollowingUser().getProfileImage())
                    .build();
            followsList.add(follows);
        }));

        List<Trophy> selectedTrophies = trophyService.getAllTrophies(userId);

        List<Long> trophiesList = new ArrayList<>();

        selectedTrophies.forEach((trophy -> {
            trophiesList.add(trophy.getId());
        }));

        User user = userService.getUser(userId);
        UserProfileApiResponse userProfileApiResponse = UserProfileApiResponse.builder()
                .profileImage(user.getProfileImage())
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .username(user.getName())
                .trophies(trophiesList)
                .followings(followingsList)
                .secondCharacter(user.getSecondCharacter())
                .follows(followsList)
                .build();

        return ApiMessage.RESPONSE(Status.OK,userProfileApiResponse);
    }

    @ApiOperation(value = "트로피 정보 선택된 값만 가져오기")
    @GetMapping("/trophy/selected")
    public ApiMessage<List<TrophyApiResponse>> getSelectedTrophies(HttpServletRequest request){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Trophy> selectedTrophies = trophyService.getSelectedTrophies(userIdFromJwtToken);
        return getTrophyApiResponse(selectedTrophies);
    }

    @ApiOperation(value = "트로피 정보 전체 가져오기")
    @GetMapping("/trophy")
    public ApiMessage<List<TrophyApiResponse>> getAllTrophies(HttpServletRequest request){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Trophy> allTrophies = trophyService.getAllTrophies(userIdFromJwtToken);
        return getTrophyApiResponse(allTrophies);
    }

    @ApiOperation(value = "회원 정보 수정")
    @PutMapping("/profile")
    public ApiMessage<String> updateUserProfile(HttpServletRequest request,
                                                @RequestParam(value = "profile_image",required = false) MultipartFile file,
                                                @RequestParam("username") String username,
                                                @RequestParam("password") String password,
                                                @RequestParam("gender") Gender gender,
                                                @RequestParam("age") Integer age) throws IOException {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);

        String fileName ="";

        if(file == null || file.isEmpty()){
            fileName = userService.getUserProfile(userIdFromJwtToken);
        }else{
            fileName = UUID.randomUUID().toString() + ".jpg";
            String folder = "./images/userprofile/";
            String saved = folder + fileName;
            FileCopyUtils.copy(file.getBytes(),new File(saved));
        }

        ProfileEdit profileEdit = ProfileEdit.builder()
                .age(age)
                .password(bCryptPasswordEncoder.encode(password))
                .profileImage(fileName)
                .gender(gender)
                .username(username)
                .build();

        userService.updateUserProfile(userIdFromJwtToken, profileEdit);

        return ApiMessage.RESPONSE(Status.OK,"수정 성공");
    }

    @ApiOperation(value = "부캐 등록")
    @PostMapping("/second")
    public ApiMessage<String> registerSecondCharacter(HttpServletRequest request, @RequestBody SecondRegisterApiRequest secondRegisterApiRequest){
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        Integer type = secondRegisterApiRequest.getType();
        userService.registerSecondCharacter(userIdFromJwtToken,type);
        return ApiMessage.RESPONSE(Status.OK,"부캐 등록 성공");
    }

    @ApiOperation(value = "이메일 비밀번호 찾기에서 유저 정보 반환하기")
    @PostMapping("/useremail")
    public ApiMessage<Map<String,Long>> getUserIdFromEmail(@RequestBody EmailFindApiRequest emailFindApiRequest){
        Long userId = userService.getUserIdFromEmail(emailFindApiRequest.getEmail());
        Map<String,Long> map = new HashMap<>();
        map.put("user_id",userId);
        // jenkins 테스트를 위한 주석코드

        return ApiMessage.RESPONSE(Status.OK,map);
    }

    @ApiOperation(value = "이메일을 통한 회원정보 수정")
    @PutMapping("/profile/password")
    public ApiMessage<String> updateUserPassword(@RequestBody PasswordApiRequest passwordApiRequest){
        userService.setNewPassword(passwordApiRequest.getUserId(),bCryptPasswordEncoder.encode(passwordApiRequest.getPassword()));
        return ApiMessage.RESPONSE(Status.OK);
    }



    // <-----> method <-----> //

    private ApiMessage<List<TrophyApiResponse>> getTrophyApiResponse(List<Trophy> selectedTrophies) {
        List<TrophyApiResponse> trophyApiResponses = new ArrayList<>();
        selectedTrophies.forEach((trophy -> {
            TrophyApiResponse trophyApiResponse = TrophyApiResponse.builder()
                    .name(trophy.getName())
                    .image(trophy.getImage())
                    .build();
            trophyApiResponses.add(trophyApiResponse);
        }));
        return ApiMessage.RESPONSE(Status.OK,trophyApiResponses);
    }


}
