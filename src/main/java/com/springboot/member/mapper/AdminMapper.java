package com.springboot.member.mapper;

import com.springboot.member.dto.AdminDto;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {
    AdminDto.MemberResponse responseDtoToMember(Member member);
}
