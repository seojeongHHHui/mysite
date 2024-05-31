desc user;

-- join
insert into user values(null, '관리자', 'admin@mysite.com', password('1234'), 'female', current_date());

-- login
select no, name from user where email='hui@gmail.com' and password=password('qwer');

-- select
select * from user;

select no, name, email, gender from user where no=2;


select * from guestbook;
select * from user;
