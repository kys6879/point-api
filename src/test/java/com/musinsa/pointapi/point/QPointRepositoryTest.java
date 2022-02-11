package com.musinsa.pointapi.point;


import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.repository.MemberRepository;
import com.musinsa.pointapi.persistence.QueryDslConfig;
import com.musinsa.pointapi.point.repository.PointRepository;
import com.musinsa.pointapi.point.repository.QPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(QueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class QPointRepositoryTest {

    @Autowired
    private QPointRepository qPointRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    private MemberEntity member01;

    @BeforeEach
    void setUp() {

        this.member01 = this.memberRepository.save(new MemberEntity(
                null,
                "test01",
                "1234"
        ));

    }

    @DisplayName("현재로부터 만료일이 가장 가까운 적립된 포인트를 찾는다.")
    @Test
    void findOneFirstEarnedPointTest() {
        PointEntity FirstEarnedPoint = this.qPointRepository.findOneFirstEarnedPoint();

        assertEquals(1000,FirstEarnedPoint.getAmount());

    }

    @DisplayName("특정 유저의 포인트 내역을 페이지별로 가져온다.")
    @Test
    void findPointPagingTest() {

        /* Given */
        List<PointEntity> targets = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            LocalDateTime now = LocalDateTime.now();
            targets.add(new PointEntity(null,PointStatusEnum.EARN,1000 * i,now.plusDays(i),now.plusYears(1),this.member01));
        }

        this.pointRepository.saveAll(targets);

        int size = 5;

        /* When */
        Pageable pageRequest01 = PageRequest.of(0, size, Sort.by("actionAt").ascending());
        Page<PointEntity> page01 = this.qPointRepository.findPointPage(this.member01.getId(), pageRequest01);

        List<PointEntity> result01 = page01.getContent();

        /* Then */

        /* 가져온 개수가 일치하는지 */
        assertEquals(size,result01.size());

        /* 페이지에 해당하는 요소들을 가져왔는지 */
        assertEquals(targets.get(0).getId(), result01.get(0).getId());
        assertEquals(targets.get(1).getId(), result01.get(1).getId());
        assertEquals(targets.get(2).getId(), result01.get(2).getId());
        assertEquals(targets.get(3).getId(), result01.get(3).getId());
        assertEquals(targets.get(4).getId(), result01.get(4).getId());


        /* When */
        Pageable pageRequest02 = PageRequest.of(1, size, Sort.by("actionAt").ascending());
        Page<PointEntity> page02 = this.qPointRepository.findPointPage(this.member01.getId(), pageRequest02);

        List<PointEntity> result02 = page02.getContent();

        /* Then */

        /* 가져온 개수가 일치하는지 */
        assertEquals(size,result02.size());

        /* 페이지에 해당하는 요소들을 가져왔는지 */
        assertEquals(targets.get(5).getId(), result02.get(0).getId());
        assertEquals(targets.get(6).getId(), result02.get(1).getId());
        assertEquals(targets.get(7).getId(), result02.get(2).getId());
        assertEquals(targets.get(8).getId(), result02.get(3).getId());
        assertEquals(targets.get(9).getId(), result02.get(4).getId());
    }

    @DisplayName("특정 유저의 포인트 내역은 정렬되서 가져온다.")
    @Test
    void findPointPageOrderingTest() {

        /* Given */
        List<PointEntity> targets = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            LocalDateTime now = LocalDateTime.now();
            targets.add(new PointEntity(null,PointStatusEnum.EARN,1000 * i,now.plusDays(i),now.plusYears(1),this.member01));
        }

        this.pointRepository.saveAll(targets);

        int size = 5;

        Sort property = Sort.by("actionAt");
        Sort asc = property.ascending();
        Sort desc = property.descending();

        /* When */
        Pageable pageRequest01 = PageRequest.of(0, size, asc);
        Page<PointEntity> page01 = this.qPointRepository.findPointPage(this.member01.getId(), pageRequest01);

        List<PointEntity> result01 = page01.getContent();

        /* Then */

        /* 가져온 개수가 일치하는지 */
        assertEquals(size,result01.size());

        /* 페이지에 해당하는 요소들을 가져왔는지 */
        assertEquals(targets.get(0).getActionAt(), result01.get(0).getActionAt());
        assertEquals(targets.get(1).getActionAt(), result01.get(1).getActionAt());
        assertEquals(targets.get(2).getActionAt(), result01.get(2).getActionAt());
        assertEquals(targets.get(3).getActionAt(), result01.get(3).getActionAt());
        assertEquals(targets.get(4).getActionAt(), result01.get(4).getActionAt());

        /* When */
        Pageable pageRequest02 = PageRequest.of(0, size, desc);
        Page<PointEntity> page02 = this.qPointRepository.findPointPage(this.member01.getId(), pageRequest02);

        List<PointEntity> result02 = page02.getContent();

        /* Then */

        /* 가져온 개수가 일치하는지 */
        assertEquals(size,result02.size());

        /* 페이지에 해당하는 요소들을 가져왔는지 */
        assertEquals(targets.get(9).getActionAt(), result02.get(0).getActionAt());
        assertEquals(targets.get(8).getActionAt(), result02.get(1).getActionAt());
        assertEquals(targets.get(7).getActionAt(), result02.get(2).getActionAt());
        assertEquals(targets.get(6).getActionAt(), result02.get(3).getActionAt());
        assertEquals(targets.get(5).getActionAt(), result02.get(4).getActionAt());
    }

}
