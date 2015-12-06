use amazon;
create table usertable(userid varchar(10) primary key, password varchar(32) not null);
create table grouptable(userid varchar(10) not null, groupid varchar(20) not null, primary key (userid, groupid));
alter table grouptable add constraint FK_USERID foreign key(userid) references usertable(userid);
commit;
grant all privileges on *.* to 'root'@'localhost' identified by 'root123' with grant option;

insert into usertable values ('ahmad', 'ahmad123');
insert into grouptable values ('ahmad', 'User');
insert into usertable values ('azizi', 'azizi123');
insert into grouptable values ('azizi', 'User');
insert into usertable values ('abdullah', 'abdullah123');

