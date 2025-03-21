package com.springboot.member.service;

import com.springboot.auth.utils.AuthorityUtils;
import com.springboot.category.entity.Category;
import com.springboot.category.service.CategoryService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberCategory;
import com.springboot.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityUtils authorityUtils;
    private final CategoryService categoryService;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuthorityUtils authorityUtils, CategoryService categoryService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.categoryService = categoryService;
    }

    public Member createMember(Member member){
        //중복 이메일 여부 확인
        verifyExistsEmail(member.getEmail());

        //카테고리 존재 여부 확인
        member.getMemberCategories().stream()
                .forEach(memberCategory ->
                        categoryService.findVerifiedCategory(memberCategory.getCategory().getCategoryId()));

        //우선순위 부여
        List<MemberCategory> memberCategories = member.getMemberCategories();
        for(int i = 0; i < memberCategories.size(); i++){
            memberCategories.get(i).setPriority(i+1);
        }
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        //권한 목록 저장
        List<String> roles = authorityUtils.createAuthorities(member.getEmail());
        member.setRoles(roles);


        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(long memberId){
        return findVerifiedMember(memberId);
    }

    @Transactional(readOnly = true)
    public Page<Member> findMembers(int page, int size, long memberId){
        //관리자 인지 확인(관리자만 회원 전체를 조회할 수 있어야한다.)
        if(!isAdmin(memberId)){
            throw new BusinessLogicException(ExceptionCode.FORBIDDEN_OPERATION);
        }

        //모든 회원을 페이지 단위로 받아 반환 (Page 객체를 반환한다.)
        //회원 목록을 페이지네이션 및 정렬하여 조회
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member, long memberId){
        //멤버가 DB에 존재하는지 확인
        Member findMember = findVerifiedMember(member.getMemberId());

        //로그인한 멤버가 맞는지 확인
        isAuthenticatedMember(member.getMemberId(), memberId);

        //null처리를 위해서 Optional를 사용한다.
        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));

        return memberRepository.save(findMember);
    }

    public void verifyExistsEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    public Member findVerifiedMember(long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return member;
    }

    public void isAuthenticatedMember(long memberId, long authenticationMemberId){
        if(memberId != authenticationMemberId){
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_MEMBER_ACCESS);
        }
    }
    //관리자 여부 확인 메서드
    public boolean isAdmin(long memberId){
        Member member = findVerifiedMember(memberId);
        return member.getRoles().contains("ADMIN");
    }
}
