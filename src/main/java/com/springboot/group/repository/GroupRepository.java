package com.springboot.group.repository;

import com.springboot.group.entity.Group;
import com.springboot.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    //Optional<Group> findByGroupName(String groupName);
    //대소문자까지 구분해서 모임명 중복을 방지
    @Query("SELECT COUNT(g) > 0 FROM Group g " +
            "WHERE LOWER(REPLACE(g.groupName, ' ', '')) = LOWER(REPLACE(:groupName, ' ', ''))")
    boolean existsByNormalizedGroupName(@Param("groupName") String groupName);

//    @Query("SELECT g FROM Group g JOIN g.groupMembers gm WHERE gm.member.memberId = :memberId")
//    Page<Group> findAllByMemberId(@Param("memberId") long memberId, Pageable pageable);

    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.member = :member")
    Page<Group> findAllByMember(@Param("member") Member member, Pageable pageable);

    //member를 가지고있는건 groupMember 이기 때문에 쿼리사용
    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.member = :member AND gm.group.groupStatus = :status")
    Page<Group> findAllByMemberAndGroupStatus(Member member, Group.GroupStatus status, Pageable pageable);

    //해당 카테고리의 모임들을 조회한다(id로 받을경우)
    @Query("SELECT g FROM Group g WHERE g.subCategory.category.categoryId = :categoryId")
    Page<Group> findByCategory(@Param("categoryId") long categoryId, Pageable pageable);

//    //해당 카테고리의 모임들을 조회한다(name으로 받을경우)
//    @Query("SELECT g FROM Group g WHERE g.subCategory.category.categoryName = :categoryName")
//    Page<Group> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
}
