package com.sparta.studytrek.aop;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class UserRoleAop {

    private final JwtUtil jwtUtil;

    @Pointcut("execution(* com.sparta.studytrek.domain.recruitment.controller.RecruitmentController.createRecruitment(..)) || " +
        "execution(* com.sparta.studytrek.domain.recruitment.controller.RecruitmentController.updateRecruitment(..)) || " +
        "execution(* com.sparta.studytrek.domain.recruitment.controller.RecruitmentController.deleteRecruitment(..))")
    public void recruitmentRoleCheck() {}

    @Pointcut("execution(* com.sparta.studytrek.domain.admin.controller.AdminController.adminDelete(..)) || " +
        "execution(* com.sparta.studytrek.domain.admin.controller.AdminController.approveProfile(..)) || " +
        "execution(* com.sparta.studytrek.domain.admin.controller.AdminController.rejectProfile(..)) || " +
        "execution(* com.sparta.studytrek.domain.admin.controller.AdminController.getAllProfiles(..)) || " +
        "execution(* com.sparta.studytrek.domain.admin.controller.AdminController.getProfileByStatus(..)) || " +
        "execution(* com.sparta.studytrek.domain.admin.controller.AdminController.getProfileById(..)) || " +
        "execution(* com.sparta.studytrek.domain.admin.controller.AdminController.createCamp(..))")
    public void adminRoleCheck() {}

    @Pointcut("execution(* com.sparta.studytrek.domain.review.controller.ReviewController.createReview(..)) || " +
        "execution(* com.sparta.studytrek.domain.review.controller.ReviewController.updateReview(..)) || " +
        "execution(* com.sparta.studytrek.domain.review.controller.ReviewController.deleteReview(..))")
    public void reviewRoleCheck() {}

    @Pointcut("execution(* com.sparta.studytrek.domain.study.controller.StudyController.createStudy(..)) || " +
        "execution(* com.sparta.studytrek.domain.study.controller.StudyController.updateStudy(..)) || " +
        "execution(* com.sparta.studytrek.domain.study.controller.StudyController.deleteStudy(..))")
    public void studyRoleCheck() {}


    public String getUserRole() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = jwtUtil.getJwtFromHeader(request);
        return jwtUtil.getUserRoleFromToken(token);
    }

    @Before("recruitmentRoleCheck()")
    public void recruitmentUserCheck() {
        String userRole = getUserRole();
        if (!UserRoleEnum.BOOTCAMP.name().equals(userRole) && !UserRoleEnum.ADMIN.name().equals(userRole)) {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED_USER);
        }
    }

    @Before("adminRoleCheck()")
    public void adminUserCheck() {
        String userRole = getUserRole();
        if (!UserRoleEnum.ADMIN.name().equals(userRole)) {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED_ADMIN);
        }
    }

    @Before("reviewRoleCheck() || studyRoleCheck()")
    public void reviewAndStudyUserCheck() {
        String userRole = getUserRole();
        if (!UserRoleEnum.USER.name().equals(userRole) && !UserRoleEnum.ADMIN.name().equals(userRole)) {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED_BOOTCAMP);
        }
    }
}
