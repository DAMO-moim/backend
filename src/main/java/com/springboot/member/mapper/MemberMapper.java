package com.springboot.member.mapper;

import com.springboot.category.entity.Category;
import com.springboot.member.dto.MemberCategoryDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberCategory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//매핑되지 않은 필드가 있더라도 에러를 무시하도록 설정
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    //Member memberPostToMember(MemberDto.Post requestBody);
    Member memberPatchToMember(MemberDto.Patch requestBody);
    MemberDto.Response memberToMemberResponse(Member member);
    List<MemberDto.Response> membersToMemberResponses(List<Member> members);
    Member memberDeleteToMember(MemberDto.Delete requestBody);
    Member findIdDtoToMember(MemberDto.FindId requestBody);
    MemberDto.FindIdResponse memberToFindId(Member member);
    MemberDto.MyPageResponse memberToMyPage(Member member);

    default Member memberPostToMember(MemberDto.Post requestBody) {
        Member member = new Member();
        member.setEmail(requestBody.getEmail());
        member.setPassword(requestBody.getPassword());
        member.setName(requestBody.getName());
        member.setPhoneNumber(requestBody.getPhoneNumber());
        member.setGender(requestBody.getGender());
        member.setBirth(requestBody.getBirth());
        List<MemberCategory> memberCategories = requestBody.getMemberCategories().stream()
                .map(memberCategoryDto -> {
                    Category category = new Category();
                    category.setCategoryId(memberCategoryDto.getCategoryId());

                    MemberCategory memberCategory = new MemberCategory();
                    memberCategory.setCategory(category);
                    memberCategory.setMember(member);
                    return memberCategory;
                })
                .collect(Collectors.toList());
        member.setMemberCategories(memberCategories);
        return member;
    }

    default List<MemberCategoryDto.Response> memberCategoriesToResponseDto(List<MemberCategory> memberCategories) {
        return memberCategories.stream()
                .map(mc -> new MemberCategoryDto.Response(
                        mc.getCategory().getCategoryId(),
                        mc.getCategory().getCategoryName(),
                        mc.getPriority()
                ))
                .collect(Collectors.toList());
    }

    default MemberCategory dtoToMemberCategory(MemberCategoryDto.Patch.MemberCategoryUpdate dto){
        Category category = new Category();
        category.setCategoryId(dto.getCategoryId());

        MemberCategory memberCategory = new MemberCategory();
        memberCategory.setCategory(category);
        memberCategory.setPriority(dto.getPriority());

        return memberCategory;
    }
    List<MemberCategory> dtoToMemberCategories(List<MemberCategoryDto.Patch.MemberCategoryUpdate> dtoList);
}