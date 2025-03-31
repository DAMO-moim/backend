package com.springboot.member.service;

import com.springboot.auth.utils.AuthorityUtils;
import com.springboot.category.entity.Category;
import com.springboot.category.service.CategoryService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.file.Service.StorageService;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.service.GroupMemberService;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberCategory;
import com.springboot.member.repository.MemberCategoryRepository;
import com.springboot.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityUtils authorityUtils;
    private final CategoryService categoryService;
    private final StorageService storageService;
    private final GroupMemberService groupMemberService;
    private final String defaultImagePath;


    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                         AuthorityUtils authorityUtils, CategoryService categoryService,
                         StorageService storageService, MemberCategoryRepository memberCategoryRepository, GroupMemberService groupMemberService,
                         @Value("${file.default-image}") String defaultImagePath) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.categoryService = categoryService;
        this.storageService = storageService;
        this.defaultImagePath = defaultImagePath;
        this.memberCategoryRepository = memberCategoryRepository;
        this.groupMemberService = groupMemberService;
    }

    public Member createMember(Member member){
        //중복 이메일 여부 확인
        verifyExistsEmail(member.getEmail());

        //중복 이름 여부 확인
        verifyExistsName(member.getName());

        //카테고리 존재 여부 확인
        member.getMemberCategories().stream()
                .forEach(memberCategory ->
                        categoryService.findVerifiedCategory(memberCategory.getCategory().getCategoryId()));
        List<MemberCategory> memberCategories = member.getMemberCategories();

        //카테고리 중복 체크
        validateNoDuplicateCategories(memberCategories);

        //카테고리 우선순위 부여
        for(int i = 0; i < memberCategories.size(); i++){
            memberCategories.get(i).setPriority(i+1);
        }

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        //권한 목록 저장
        List<String> roles = authorityUtils.createAuthorities(member.getEmail());
        member.setRoles(roles);
        //회원가입이 완료되면 프로필이미지 기본이미지로 생성
        member.setImage(defaultImagePath);
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
        return memberRepository.findByMemberStatus(
                Member.MemberStatus.MEMBER_ACTIVE,
                PageRequest.of(page, size, Sort.by("memberId").descending())
        );
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void updatePassword(MemberDto.PatchPassword dto, long memberId){
        Member findMember = findVerifiedMember(memberId);
        //기존 비밀번호랑 해당 회원의 DB 비밀번호가 같은지 비교
        if(!passwordEncoder.matches(dto.getCurrentPassword(), findMember.getPassword())){
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_MATCHED);
        }

        if(!passwordEncoder.matches(dto.getNewPassword(), findMember.getPassword())){
            throw new BusinessLogicException(ExceptionCode.PASSWORD_SAME_AS_OLD);
        }

        //새로운 비밀번호로 수정
        String encoded = passwordEncoder.encode(dto.getNewPassword());
        findMember.setPassword(encoded);
        memberRepository.save(findMember);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member, long memberId){
        //중복 이름 여부 확인
        verifyExistsName(member.getName());

        //멤버가 DB에 존재하는지 확인
        Member findMember = findVerifiedMember(memberId);
        //로그인한 멤버가 맞는지 확인
        //isAuthenticatedMember(member.getMemberId(), memberId);

        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));

        return memberRepository.save(findMember);
    }

    public void myDeleteMember(Member member, long memberId){
        //사용자가 맞는지 검증
        Member findMember = findVerifiedMember(memberId);

        //사용자가 어떤 모임의 모임장인지 검증(탈퇴 불가)
        if(groupMemberService.findLeader(findMember)){
            throw new BusinessLogicException(ExceptionCode.CANNOT_DELETE_GROUP_LEADER);
        }

        //입력한 이메일과 비밀번호가 자신의 이메일과 비밀번호가 일치하는지 검증
        if(!member.getEmail().equals(findMember.getEmail())){
            throw new BusinessLogicException(ExceptionCode.INVALID_CREDENTIALS);
        }

        if(!passwordEncoder.matches(member.getPassword(), findMember.getPassword())){
            throw new BusinessLogicException(ExceptionCode.INVALID_CREDENTIALS);
        }

        //둘다 문제없으면 탈퇴
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        memberRepository.save(findMember);
        //회원이 탈퇴되면 모임원일 경우 가입된 모임과 모임일정에서 탈퇴
        groupMemberService.deleteAllGroups(findMember);
    }

    //관리자의 회원탈퇴
    public void deleteMember(long memberId, Member admin){
        if(!isAdmin(admin.getMemberId())){
           throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }

        //삭제할 멤버 찾기
        Member findMember = findVerifiedMember(memberId);

        //사용자가 어떤 모임의 모임장인지 검증(탈퇴 불가)
        if(groupMemberService.findLeader(findMember)){
            throw new BusinessLogicException(ExceptionCode.CANNOT_DELETE_GROUP_LEADER);
        }

        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        memberRepository.save(findMember);

        //회원이 탈퇴되면 모임원일 경우 가입된 모임과 모임일정에서 탈퇴
        groupMemberService.deleteAllGroups(findMember);
    }

    public void updateMemberCategories(long memberId, List<MemberCategory> memberCategories){
        Member findMember = findVerifiedMember(memberId);

        //회원의 카테고리 목록 비우기
        //우선순위가 존재하기 때문에 덮어씌우는게 더 효율적
        findMember.getMemberCategories().clear();
        memberRepository.flush();

        //새 카테고리 등록
        for(int i = 0; i< memberCategories.size(); i++){
            MemberCategory memberCategory = memberCategories.get(i);
            memberCategory.setMember(findMember);
            findMember.getMemberCategories().add(memberCategory);
        }
        //카테고리를 중복으로 골랐는지 검증
        validateNoDuplicateCategories(memberCategories);
        memberRepository.save(findMember);
    }

    //멤버의 카테고리 가져오는 메서드
    public List<MemberCategory> findMemberCategroies(long memberId){
        Member member = findVerifiedMember(memberId);
        List<MemberCategory> memberCategories = member.getMemberCategories();

        return memberCategories;
    }

    //이메일 중복 여부 확인 메서드
    public void verifyExistsEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    //닉네임 중복 여부 확인 메서드
    public void verifyExistsName(String name){
        Optional<Member> member = memberRepository.findByName(name);

        if(member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_NAME_EXISTS);
    }

    //폰번호 중복 여부 확인 메서드
    public void verifyExistsPhoneNumber(String phoneNumber){
        Optional<Member> member = memberRepository.findByPhoneNumber(phoneNumber);

        if(member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_PHONE_NUMBER_EXISTS);
    }

    //회원가입한 회원인지 확인하는 메서드
    public Member findVerifiedMember(long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return member;
    }

    //해당 사용자가 본인인지 검증하는 메서드
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

    //카테고리 중복체크를 위한 메서드
    private void validateNoDuplicateCategories(List<MemberCategory> memberCategories) {
        Set<Long> uniqueCategoryIds = new HashSet<>();

        for (MemberCategory mc : memberCategories) {
            Long categoryId = mc.getCategory().getCategoryId();
            if (!uniqueCategoryIds.add(categoryId)) {
                throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
            }
        }
    }

    //아이디를 찾기위한 메서드
    public Member findMemberEmail(Member member){
        return memberRepository.findByNameAndPhoneNumber(member.getName(), member.getPhoneNumber())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    //이미지 등록
    public void uploadImage(Member member, MultipartFile imageFile) {
        Member findMember = findVerifiedMember(member.getMemberId());

        // 파일을 가져왔을때 그 파일이 null이거나 빈 파일 일때 검증해야함
        if (imageFile != null && !imageFile.isEmpty()) {
            //덮어쓰기가 가능하도록 항상 같은 이름으로 저장
            String pathWithoutExt = "members/" + findMember.getMemberId() + "/profile";
            // 이미지가 저장되며 내부적으로 확장자를 붙임
            //String relativePath = storageService.store(imageFile, pathWithoutExt);
            // 실제 접근가능한 url -> 프론트가 이 링크 사용할 예정
            //String imageUrl = "/images/" + relativePath;
            // 실제 db에 이미지 경로 저장

            //s3로 변경
            String imageUrl = storageService.store(imageFile, pathWithoutExt);
            findMember.setImage(imageUrl);
        } else {
            // 이미지가 없다면 기본 이미지로 삽입
            findMember.setImage(defaultImagePath);
        }
    }

    //회원의 우선순위가 높은 카테고리를 가져오기 위한 메서드
    //회원의 우선순위가 가장 높은 카테고리를 가져온다(모임일정, 모임 조회시 디폴트)
    public Category findTopPriorityCategory(Member member){
        return memberCategoryRepository.findTopByMemberOrderByPriorityAsc(member)
                .map(MemberCategory::getCategory)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }
}
