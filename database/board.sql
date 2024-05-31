select * from board;

-- select
select a.no, a.title, a.contents, a.hit, a.reg_date, a.g_no, a.o_no, a.depth, a.user_no, b.name
from board a, user b
where a.user_no=b.no
order by g_no desc, o_no asc;

-- modify
update board set title=?, contents=? where no=?;

-- insert new
insert into board values(null,?,?,?,?,?,?,?,?);
insert into board values(null,?,?,?,?, ifnull((select max(g_no) from board a)+1,1), ?,?,?);

-- insert reply
insert into board select null,?,?,?,?, g_no, (o_no+1), (depth+1), ? from board a where no=?;

-- reply update
update board set o_no = o_no+1 where g_no=? and o_no>=?;
update board set o_no = o_no+1 where g_no=(select g_no from board a where no=?) and o_no>=(select o_no+1 from board b where no=?);

-- paging
select * from board
order by g_no DESC, o_no ASC
limit (page-1)*5, 5;

select a.no, a.title, a.contents, a.hit, a.reg_date, a.g_no, a.o_no, a.depth, a.user_no, b.name
from board a, user b
where a.user_no=b.no
order by g_no desc, o_no asc
limit ?, 5;

-- getPageSection



