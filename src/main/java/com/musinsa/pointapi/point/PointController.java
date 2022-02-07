package com.musinsa.pointapi.point;

import com.musinsa.pointapi.http.BaseResponse;
import com.musinsa.pointapi.member.MemberController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController extends MemberController {

    @GetMapping("/{memberId}/points")
    ResponseEntity<BaseResponse<String>> getMemberPoint(@PathVariable String memberId) {

        Long integerMemberId = Long.valueOf(memberId);

        BaseResponse<String> response = new BaseResponse();

        return ResponseEntity.ok(response);
    }
}
