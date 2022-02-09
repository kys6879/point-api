package com.musinsa.pointapi.point;

import com.musinsa.pointapi.http.BaseResponse;
import com.musinsa.pointapi.member.MemberController;
import com.musinsa.pointapi.point.request.EarnPointRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PointController extends MemberController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/{memberId}/points")
    ResponseEntity<BaseResponse<String>> getMemberPoint(@PathVariable String memberId) {

        Long integerMemberId = Long.valueOf(memberId);

        BaseResponse<String> response = new BaseResponse();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{memberId}/points")
    ResponseEntity<BaseResponse<String>> earnPoint(@PathVariable String memberId, @RequestBody EarnPointRequest earnPointRequest) {

        Integer amount = earnPointRequest.getAmount();
        Long integerMemberId = Long.valueOf(memberId);

        this.pointService.earnPoint(amount,integerMemberId);

        BaseResponse<String> response = new BaseResponse();

        return ResponseEntity.ok(response);
    }
}
