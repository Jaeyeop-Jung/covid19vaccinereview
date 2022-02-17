use covid19vaccinereview;

delete from `user` ;
delete from boards;
delete from post ;
delete from notification ;
delete from comment ;
delete from postimage ;
delete from profileimage ;


alter table `user`  auto_increment = 1;
alter table post  auto_increment = 1;
alter table postimage  auto_increment = 1;
alter table profileimage  auto_increment = 1;
alter table comment  auto_increment = 1;
ALTER table notification auto_increment = 1;
alter table boards auto_increment = 1;


INSERT into `user`(email, password, nickname, loginprovider , `role` , profileimage_id , date_created, last_updated)
values ("testuser1@test.com", "testuser1", "testuser1", "ORIGINAL", "ROLE_USER", null,"2020-01-01","2020-01-01");

INSERT into `user`(email, password, nickname, loginprovider , `role` , profileimage_id , date_created, last_updated)
values ("testuser2@test.com", "testuser2", "testuser2", "ORIGINAL", "ROLE_USER", null,"2020-01-01","2020-01-01");

INSERT into `user`(email, password, nickname, loginprovider , `role` , profileimage_id , date_created, last_updated)
values ("testuser3@test.com", "testuser3", "testuser3", "ORIGINAL", "ROLE_USER", null,"2020-01-01","2020-01-01");

INSERT into `user`(email, password, nickname, loginprovider , `role` , profileimage_id , date_created, last_updated)
values ("testuser4@test.com", "testuser4", "testuser4", "ORIGINAL", "ROLE_USER", null,"2020-01-01","2020-01-01");

INSERT into `user`(email, password, nickname, loginprovider , `role` , profileimage_id , date_created, last_updated)
values ("testuser5@test.com", "testuser5", "testuser5", "ORIGINAL", "ROLE_USER", null,"2020-01-01","2020-01-01");





INSERT into boards(vaccine_types, ordinal_number)
values ("MODERNA", 1);

INSERT into boards(vaccine_types, ordinal_number)
values ("PFIZER", 1);

INSERT into boards(vaccine_types, ordinal_number)
values ("ASTRAZENECA", 1);

INSERT into boards(vaccine_types, ordinal_number)
values ("JANSSEN", 1);



INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (1, 1, "testpost1", "testpost1", 10, 10, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (2, 2, "testpost2", "testpost2", 20, 20, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (3, 3, "testpost3", "testpost3", 30, 30, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (4, 4, "testpost4", "testpost4", 40, 40, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (1, 1, "testpost5", "testpost5", 50, 50, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (2, 2, "testpost6", "testpost6", 60, 60, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (3, 3, "testpost7", "testpost7", 70, 70, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (4, 4, "testpost8", "testpost8", 80, 80, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (1, 1, "testpost9", "testpost9", 90, 90, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (2, 2, "testpost10", "testpost10", 100, 100, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (3, 3, "testpost11", "testpost11", 110, 110, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (4, 4, "testpost12", "testpost12", 120, 120, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (1, 1, "testpost13", "testpost13", 130, 130, "2020-01-01","2020-01-01");

INSERT into post(user_id, boards_id, title, content, like_count, view_count, date_created, last_updated)
values (2, 2, "testpost14", "testpost14", 140, 140, "2020-01-01","2020-01-01");

