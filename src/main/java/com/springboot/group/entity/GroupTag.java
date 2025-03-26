package com.springboot.group.entity;

import com.springboot.tag.entity.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class GroupTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupTagId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void setGroup(Group group) {
        this.group = group;
        if (!group.getGroupTags().contains(this)) {
            group.setGroupTag(this);
        }
    }

}
