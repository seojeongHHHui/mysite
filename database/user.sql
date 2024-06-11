desc user;

-- join
insert into user values(null, '관리자', 'admin@mysite.com', password('1234'), 'female', current_date(), 'ADMIN');

-- login
select no, name from user where email='hui@gmail.com' and password=password('qwer');

-- select
select * from user;

select no, name, email, gender from user where no=2;

-- role 추가
alter table user add column role enum('ADMIN', 'USER') not null default 'USER';

update user set role='ADMIN' where no=1;

