package com.musinsa.pointapi.member.dto;

import com.musinsa.pointapi.member.MemberEntity;

public class MemberDto {
    private Long id;

    private String email;

    private String password;

    private Integer point;

    public MemberDto(Long id, String email, String password, Integer point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public static MemberDto from(MemberEntity memberEntity) {
        return new MemberDto(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }
}
