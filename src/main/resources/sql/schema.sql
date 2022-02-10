drop table if exists member;

drop table if exists point;

drop table if exists point_detail;

create table member (
                        id bigint not null auto_increment,
                        email varchar(100) not null,
                        password varchar(50) not null,
                        point integer not null,
                        primary key (id)
) engine=InnoDB;

create table point (
                       id bigint not null auto_increment,
                       action_at datetime not null,
                       amount integer not null,
                       expire_at datetime,
                       status integer,
                       member_id bigint,
                       primary key (id)
) engine=InnoDB;

create table point_detail (
                              id bigint not null auto_increment,
                              action_at datetime not null,
                              amount integer not null,
                              expire_at datetime not null,
                              status integer,
                              point_id bigint,
                              point_detail_id bigint,
                              primary key (id)
) engine=InnoDB;

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table point
    add constraint FKbet7cyy000fgj8pm7pbuuur46
        foreign key (member_id)
            references member (id);

alter table point_detail
    add constraint FK4brdf2y4u57umaq9w9ejv8qj7
        foreign key (point_id)
            references point (id);

alter table point_detail
    add constraint FKnt9dbq3hy5dxc0e6dhh5ubx7n
        foreign key (point_detail_id)
            references point_detail (id);