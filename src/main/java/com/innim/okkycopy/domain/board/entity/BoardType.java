package com.innim.okkycopy.domain.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "board_type")
@DynamicUpdate
public class BoardType {
    @Id
    @Column(name = "type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long typeId;

    @OneToMany(mappedBy = "boardType")
    List<BoardTopic> boardTopics;

    @Column(name = "name")
    private String name;


}
