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

//매핑되지 않은 필드가 있더라도 에러를 무시하도록 설정
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post requestBody);
    Member memberPatchToMember(MemberDto.Patch requestBody);
    MemberDto.Response memberToMemberResponse(Member member);
    List<MemberDto.Response> membersToMemberResponses(List<Member> members);
    MemberCategory categoryPostToMemberCategory(List<MemberCategory> memberCategories);

}