INSERT INTO member (id,email,password) values (1,'apple@example.com','1234');

/* 1000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (1,'1',1000,'2022-01-01 23:10:00' ,'2023-01-01 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (1,'1',1000,'2022-01-01 23:10:00' ,'2023-01-01 23:10:00' ,1,1);

/* 2000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (2,'1',2000,'2022-01-02 23:10:00' ,'2023-01-02 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (2,'1',2000,'2022-01-02 23:10:00' ,'2023-01-02 23:10:00' ,2,2);

/* 3000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (3,'1',3000,'2022-01-03 23:10:00' ,'2023-01-03 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (3,'1',3000,'2022-01-03 23:10:00' ,'2023-01-03 23:10:00' ,3,3);

/* 3300 사용 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (4,'2',-3300,'2022-01-04 23:10:00' ,null ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (4,'2',-1000,'2022-01-04 23:10:00' ,'2023-01-01 23:10:00' ,4,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (5,'2',-2000,'2022-01-04 23:10:00' ,'2023-01-02 23:10:00' ,4,2);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (6,'2',-300,'2022-01-04 23:10:00' ,'2023-01-03 23:10:00' ,4,3);

/* 1000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (5,'1',1000,'2022-01-05 23:10:00' ,'2023-01-05 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (7,'1',1000,'2022-01-05 23:10:00' ,'2023-01-05 23:10:00' ,5,7);
