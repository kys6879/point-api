package com.musinsa.pointapi.member.dto;

import com.musinsa.pointapi.member.MemberEntity;

public class MemberDto {
    private Long id;

    private String email;

    private String password;

    public MemberDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public static MemberDto from(MemberEntity memberEntity) {
        return new MemberDto(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword()
        );
    }
}
