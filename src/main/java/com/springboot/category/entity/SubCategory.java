package com.springboot.category.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;

    @Column(nullable = false)
    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subCategory")
    private List<Group> groups;

    public void setCategory(Category category) {
        this.category = category;
        if (!category.getSubCategories().contains(this)) {
            category.getSubCategories().add(this);
        }
    }

    public void setGroup(Group group) {
        groups.add(group);
        if (group.getSubCategory() != this) {
            group.setSubCategory(this);
        }
    }
}
