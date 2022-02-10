INSERT INTO member (id,email,password,point) values (1,'apple@example.com','1234',0);

/* 1000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (1,'1',1000,'2022-02-07 23:10:00' ,'2023-02-07 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (1,'1',1000,'2022-02-07 23:10:00' ,'2023-02-07 23:10:00' ,1,1);

/* 2000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (2,'1',2000,'2022-02-08 23:10:00' ,'2023-02-08 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (2,'1',2000,'2022-02-08 23:10:00' ,'2023-02-08 23:10:00' ,2,2);

/* 3000 적립 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (3,'1',3000,'2022-02-09 23:10:00' ,'2023-02-09 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (3,'1',3000,'2022-02-09 23:10:00' ,'2023-02-09 23:10:00' ,3,3);

/* 3000 사용 */
INSERT INTO point (id,status,amount,action_At,expire_At,member_id) values (4,'2',-3300,'2022-02-10 23:10:00' ,'2023-02-10 23:10:00' ,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (4,'2',-1000,'2022-02-10 23:10:00' ,'2023-02-07 23:10:00' ,4,1);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (5,'2',-2000,'2022-02-10 23:10:00' ,'2023-02-08 23:10:00' ,4,2);
INSERT INTO point_detail (id,status,amount,action_At,expire_At,point_id, point_detail_id) values (6,'2',-300,'2022-02-10 23:10:00' ,'2023-02-09 23:10:00' ,4,3);



