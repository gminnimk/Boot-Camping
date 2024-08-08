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
    public void restrictedRoleCheck() {}

    @Before("restrictedRoleCheck()")
    public void restrictedUserCheck() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = jwtUtil.getJwtFromHeader(request);
        String userRole = jwtUtil.getUserRoleFromToken(token);

        if (!UserRoleEnum.BOOTCAMP.name().equals(userRole) && !UserRoleEnum.ADMIN.name().equals(userRole)) {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED_USER);
        }
    }
}
