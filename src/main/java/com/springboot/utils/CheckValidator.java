package com.springboot.utils;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CheckValidator {
    // 로그인한 사용자와 작성자가 동일한지 검증
    public void checkOwner(long ownerId, long principalOwnerId) {
        if (principalOwnerId != ownerId) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_OWNER);
        }
    }

    // 어드민 검증 (권한 없으면 즉시 예외 발생)
    public void checkAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }
    }

    // 관리자 또는 작성자인지 검증 (둘 중 하나라도 만족하면 통과)
    public void checkAdminOrOwner(long ownerId, long principalOwnerId) {
        if (principalOwnerId == ownerId) return; // 본인이면 통과

        // 본인이 아니면 관리자 권한 확인
        checkAdmin();
    }
}
