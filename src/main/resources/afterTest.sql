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