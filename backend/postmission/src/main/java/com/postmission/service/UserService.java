package com.postmission.service;

import com.postmission.exceptions.AuthenticationException;
import com.postmission.exceptions.NotExistException;
import com.postmission.model.Email;
import com.postmission.model.Follow;
import com.postmission.model.User;
import com.postmission.model.dto.request.ProfileEdit;
import com.postmission.model.enums.ErrorMessage;
import com.postmission.repository.EmailRepository;
import com.postmission.repository.FollowRepository;
import com.postmission.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;
    private final EmailRepository emailRepository;
    private final FollowRepository followRepository;
    private final EntityManager em;

    public void registerUser(User user){
        userRepository.findByEmail(user.getEmail()).ifPresent((email)->{
            throw new AuthenticationException(ErrorMessage.EMAIL_ALREADY_EXIST);
        });
        userRepository.save(user);
    }

    public boolean checkEmailIsPresent(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser.isPresent();
    }
    public String getEmailToken(String email){
        Email findEmail = emailRepository.findById(email).orElseThrow(() -> new NotExistException(ErrorMessage.EMAIL_ERROR));
        return findEmail.getEmailCheckToken();
    }

    public boolean sendEmailConfirmToken(String email) {
        Optional<Email> findEmail = emailRepository.findById(email);
        if(findEmail.isPresent()){
            Email targetEmail = findEmail.get();
            Email tokenSavedEmail = Email.builder()
                    .emailAddress(targetEmail.getEmailAddress())
                    .emailCheckToken(Email.generateEmailCheckToken())
                    .build();
            emailRepository.save(tokenSavedEmail);
        }else{
            Email targetEmail = Email.builder()
                    .emailAddress(email)
                    .emailCheckToken(Email.generateEmailCheckToken())
                    .build();
            emailRepository.save(targetEmail);
        }

        Email userEmail = emailRepository.findById(email).orElseThrow(()->new NotExistException(ErrorMessage.EMAIL_CONFIRM_NOT_ALLOWED));

        Context context = new Context();
        context.setVariable("message", "포스트미션 서비스를 사용 하시려면 아래 6자리 숫자를 회원가입 페이지에 입력해주세요.");
        context.setVariable("token", userEmail.getEmailCheckToken());
        context.setVariable("host", "postmission");

        String template = templateEngine.process("mail", context);

        emailService.sendEmail(email,template);
        return true;
    }

    public void followUser(Long userId,Long targetUserId){
        User user = userRepository.findById(userId).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User targetUser = userRepository.findById(targetUserId).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        Follow follow = Follow.builder()
                .followingUser(user)
                .followedUser(targetUser)
                .build();
        followRepository.save(follow);
    }

    public void unFollowUser(Long userId, Long targetUserId){
        User user = userRepository.findById(userId).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User targetUser = userRepository.findById(targetUserId).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        Optional<Follow> findFollow = followRepository.findByFollowingUserAndFollowedUser(user, targetUser);
        Follow follow = findFollow.orElseThrow(() -> new NotExistException(ErrorMessage.FOLLOW_DOES_NOT_EXIST));
        followRepository.delete(follow);
    }

    public void registerUserProfileImage(Long userId,String profileImage){
        User user = userRepository.findById(userId).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User updatedUser = User.builder()
                .id(userId)
                .profileImage(profileImage)
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .gender(user.getGender())
                .isSocial(user.isSocial())
                .password(user.getPassword())
                .age(user.getAge())
                .isDeleted(user.isDeleted())
                .name(user.getName())
                .roles(user.getRoles())
                .build();
        userRepository.save(updatedUser);
    }

    public boolean checkEmailConfirm(String email, String token) {
        Email findEmail = emailRepository.findByEmailAddressAndEmailCheckToken(email, token).orElseThrow(() -> new AuthenticationException(ErrorMessage.TOKEN_MISMATCH));
        return true;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
    }

    public List<Follow> getFollowingList(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return followRepository.findByFollowingUser(user);
    }

    public List<Follow> getFollowedList(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return followRepository.findByFollowedUser(user);
    }

    public boolean checkUserIsFollowingTargetUser(Long userId, Long targetUserId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        Optional<Follow> findFollow = followRepository.findByFollowingUserAndFollowedUser(user, targetUser);
        return findFollow.isPresent();
    }

    public String getProfileImage(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return findUser.getProfileImage();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new NotExistException(ErrorMessage.USER_ID_DOES_NOT_MATCH));
    }

    public String getEmailFromUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return user.getEmail();
    }

    public void updateUserProfile(Long userId, ProfileEdit profileEdit) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User updatedUser = User.builder()
                .id(userId)
                .profileImage(profileEdit.getProfileImage())
                .createdAt(findUser.getCreatedAt())
                .email(findUser.getEmail())
                .gender(profileEdit.getGender())
                .isSocial(findUser.isSocial())
                .password(profileEdit.getPassword())
                .age(profileEdit.getAge())
                .isDeleted(findUser.isDeleted())
                .name(profileEdit.getUsername())
                .roles(findUser.getRoles())
                .build();
        userRepository.save(updatedUser);
    }

    public void registerSecondCharacter(Long userId,int type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User updatedUser = User.builder()
                .id(userId)
                .profileImage(user.getProfileImage())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .gender(user.getGender())
                .isSocial(user.isSocial())
                .password(user.getPassword())
                .age(user.getAge())
                .isDeleted(user.isDeleted())
                .name(user.getName())
                .roles(user.getRoles())
                .secondCharacter(type)
                .build();
        userRepository.save(updatedUser);
    }

    public String getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return user.getProfileImage();
    }

    public Long getUserIdFromEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return user.getId();
    }

    public void setNewPassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        User updatedUser = User.builder()
                .id(userId)
                .profileImage(user.getProfileImage())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .gender(user.getGender())
                .isSocial(user.isSocial())
                .password(password)
                .age(user.getAge())
                .isDeleted(user.isDeleted())
                .name(user.getName())
                .roles(user.getRoles())
                .secondCharacter(user.getSecondCharacter())
                .build();
        userRepository.save(updatedUser);
    }
}
