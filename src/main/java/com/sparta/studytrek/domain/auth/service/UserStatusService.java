package com.sparta.studytrek.domain.auth.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import com.sparta.studytrek.domain.auth.repository.UserStatusRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserService userService;
    private final UserStatusRepository userStatusRepository;

    public UserStatus getUserStatus(String username, Long campId) {
        User user = userService.findByUsername(username);

        List<UserStatus> userStatus = userStatusRepository.findByUser(user);
        List<UserStatus> campStatuses = new ArrayList<>();
        List<UserStatus> noncampStatuses = new ArrayList<>();

        /*
        특정 유저의 상태를 확인하기 위해 필요한 검증 => camp 매핑 id 가 있는지 없는지 판별 필요
        있으면 List 로 뽑아오고 어느 캠프에 대한 상태코드가 필요한지 확인 필요, 없으면 일반 회원
         */

        // UserStatus 를 순회하면서 캠프 있는 상태와 없는 상태 분류
        for (UserStatus status : userStatus) {
            if (status.getCamp() != null) {
                campStatuses.add(status);
            } else {
                noncampStatuses.add(status);
            }
        }

        // 매개변수에 campId 를 받아서 처리
        UserStatus generalStatus = null;
        for (UserStatus status : userStatus) {
            // 캠프Id 가 null 이 아니고 입력받은 캠프 ID 와 동일할 때
            if (status.getCamp() != null && campId.equals(status.getCamp().getId())) {
                return status;
            }
            // 그 외 일반 상태
            else if (status.getCamp() == null) {
                generalStatus = status;
            }
        }
        return generalStatus;
    }

    public UserStatus findByStatus(UserStatusEnum status) {
        return userStatusRepository.findByStatus(status).orElseThrow(
                () -> new CustomException(ErrorCode.STATUS_NOT_FOUND)
        );
    }
}
