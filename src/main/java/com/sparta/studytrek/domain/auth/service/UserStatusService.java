package com.sparta.studytrek.domain.auth.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import com.sparta.studytrek.domain.auth.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserService userService;
    private final UserStatusRepository userStatusRepository;

    public UserStatus getUserStatus(String username, Long campId) {
        User user = userService.findByUsername(username);

        List<UserStatus> userStatus = userStatusRepository.findByUser(user);

        // 캠프와 연관된 상태를 저장하는 리스트
        List<UserStatus> campStatuses = new ArrayList<>();
        // 캠프와 연관되지 않은 상태를 저장하는 리스트 분리 ?
        List<UserStatus> noncampStatuses = new ArrayList<>();

        // 특정 유저의 상태를 확인하기 위해 필요한 검증
        // camp 매핑 id 가 있는지 없는지 판별 필요
        // 있으면 List 로 뽑아오고 어느 캠프에 대한 상태코드가 필요한지 확인 필요
        // 없으면 그냥 그거 하나있는거 가져오기? => camp가 없는건 첫 회원가입 이후 캠프 등록을 하지 않은 일반 회원임
        // camp 도 같이 찾으려면 매개변수로 camp 에 대한 것도 받아야하나?


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
