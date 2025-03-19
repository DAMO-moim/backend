package com.springboot.category.entity;

import com.springboot.member.entity.MemberCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<SubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<MemberCategory> memberCategories = new ArrayList<>();
}
