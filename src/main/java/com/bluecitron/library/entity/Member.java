package com.bluecitron.library.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Rent> rents = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @NonNull
    private String account;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;


    public Member(String account, String email) {
        this.account = account;
        this.email = email;
        this.grade = MemberGrade.NORMAL;
    }

    public void setGrade(MemberGrade grade) {
        this.grade = grade;
    }
}
