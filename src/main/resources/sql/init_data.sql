
SET @STATUS_EARN = 1;
SET @STATUS_USED = 2;
SET @MEMBER_ID = 1;

INSERT INTO member (id,email,password) values (@MEMBER_ID,'apple@example.com','1234');

/* 1000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (1,@STATUS_EARN,1000,'2022-01-01 23:10:00' ,'2023-01-01 23:10:00' ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (1,@STATUS_EARN,1000,'2022-01-01 23:10:00' ,'2023-01-01 23:10:00' ,1,1);

/* 2000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (2,@STATUS_EARN,2000,'2022-01-02 23:10:00' ,'2023-01-02 23:10:00' ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (2,@STATUS_EARN,2000,'2022-01-02 23:10:00' ,'2023-01-02 23:10:00' ,2,2);

/* 3000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (3,@STATUS_EARN,3000,'2022-01-03 23:10:00' ,'2023-01-03 23:10:00' ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (3,@STATUS_EARN,3000,'2022-01-03 23:10:00' ,'2023-01-03 23:10:00' ,3,3);

/* 3300 사용 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (4,@STATUS_USED,-3300,'2022-01-04 23:10:00' ,null ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (4,@STATUS_USED,-1000,'2022-01-04 23:10:00' ,'2023-01-01 23:10:00' ,4,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (5,@STATUS_USED,-2000,'2022-01-04 23:10:00' ,'2023-01-02 23:10:00' ,4,2);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (6,@STATUS_USED,-300,'2022-01-04 23:10:00' ,'2023-01-03 23:10:00' ,4,3);

/* 1000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (5,@STATUS_EARN,1000,'2022-01-05 23:10:00' ,'2023-01-05 23:10:00' ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (7,@STATUS_EARN,1000,'2022-01-05 23:10:00' ,'2023-01-05 23:10:00' ,5,7);


/* 아래는 만료기간 테스트를 위한 시나리오 데이터 */

/* [과거] 3000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (6,@STATUS_EARN,3000,'2020-01-01 23:10:00' ,'2021-01-01 23:10:00' ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (8,@STATUS_EARN,3000,'2020-01-01 23:10:00' ,'2021-01-01 23:10:00' ,6,8);

/* [과거] 5000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (7,@STATUS_EARN,5000,'2020-01-02 23:10:00' ,'2021-01-02 23:10:00' ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (9,@STATUS_EARN,5000,'2020-01-02 23:10:00' ,'2021-01-02 23:10:00' ,7,9);

/* [과거] 4500 사용 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (8,@STATUS_USED,-4500,'2020-01-03 23:10:00' ,null ,@MEMBER_ID);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (10,@STATUS_USED,-3000,'2020-01-03 23:10:00' ,'2021-01-01 23:10:00' ,8,8);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (11,@STATUS_USED,-1500,'2022-01-03 23:10:00' ,'2021-01-02 23:10:00' ,8,9);
