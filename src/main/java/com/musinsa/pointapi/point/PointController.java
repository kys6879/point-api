package com.musinsa.pointapi.point;

import com.musinsa.pointapi.http.BaseResponse;
import com.musinsa.pointapi.http.CodeEnum;
import com.musinsa.pointapi.member.MemberController;
import com.musinsa.pointapi.point.dto.PointDto;
import com.musinsa.pointapi.point.request.PointActionRequest;
import com.musinsa.pointapi.point.response.GetPointResponse;
import com.musinsa.pointapi.point.response.GetPointsResponse;
import com.musinsa.pointapi.point.response.GetTotalPointResponse;
import com.musinsa.pointapi.point_detail.PointDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PointController extends MemberController {

    private final PointService pointService;
    private final PointDetailService pointDetailService;

    public PointController(PointService pointService, PointDetailService pointDetailService) {
        this.pointService = pointService;
        this.pointDetailService = pointDetailService;
    }

    /* 이력 조회 */
    @GetMapping("/{memberId}/points")
    ResponseEntity<BaseResponse<GetPointsResponse>> getPoints(
            @PathVariable String memberId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(name = "direction") Sort.Direction sort
            ) {

        Long integerMemberId = Long.valueOf(memberId);

        /* 등록순으로 sorting */
        PageRequest pageRequest = PageRequest.of(page,size,sort,"actionAt");

        Page<PointEntity> pointPage = this.pointService.findPoints(integerMemberId,pageRequest);

        int totalSize = pointPage.getTotalPages();

        List<PointDto> points = pointPage.getContent()
                .stream()
                .map(PointDto::from)
                .collect(Collectors.toList());

        GetPointsResponse responseDto = new GetPointsResponse(points,totalSize);

        BaseResponse<GetPointsResponse> response = new BaseResponse(
                true,
                CodeEnum.OK,
                responseDto
        );

        return ResponseEntity.ok(response);
    }

    /* 합계 조회 */
    @GetMapping("/{memberId}/points/total")
    ResponseEntity<BaseResponse<GetTotalPointResponse>> getTotalPoint(@PathVariable String memberId) {

        Long integerMemberId = Long.valueOf(memberId);

        Integer totalPoint = this.pointDetailService.findTotalPoint(integerMemberId);

        GetTotalPointResponse responseDto = new GetTotalPointResponse(totalPoint);

        BaseResponse<GetTotalPointResponse> response = new BaseResponse(
                true,
                CodeEnum.OK,
                responseDto
        );

        return ResponseEntity.ok(response);
    }

    /* 적립 */
    @PostMapping("/{memberId}/points/earn")
    ResponseEntity<BaseResponse<GetPointResponse>> earnPoint(@PathVariable String memberId, @RequestBody PointActionRequest pointActionRequest) {

        Integer amount = pointActionRequest.getAmount();
        Long integerMemberId = Long.valueOf(memberId);

        PointEntity savedPoint = this.pointService.earnPoint(amount,integerMemberId);

        BaseResponse<GetPointResponse> response = new BaseResponse(
                true,
                CodeEnum.OK,
                PointDto.from(savedPoint)
        );

        return ResponseEntity.ok(response);
    }

    /* 사용 */
    @PostMapping("/{memberId}/points/use")
    ResponseEntity<BaseResponse<GetPointResponse>> usePoint(@PathVariable String memberId, @RequestBody PointActionRequest pointActionRequest) {

        Integer amount = pointActionRequest.getAmount();
        Long integerMemberId = Long.valueOf(memberId);

        PointEntity usedPoint = this.pointService.usePoint(amount,integerMemberId);

        BaseResponse<GetPointResponse> response = new BaseResponse(
                true,
                CodeEnum.OK,
                PointDto.from(usedPoint)
        );

        return ResponseEntity.ok(response);
    }
}
