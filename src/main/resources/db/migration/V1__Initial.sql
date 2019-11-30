create TABLE ja_session(id Int AUTO_INCREMENT PRIMARY KEY, creation_time Date, title VARCHAR(200), start_at DATE, end_at DATE,status tinyint,up_vote int default 0,down_vote int default 0);
CREATE TABLE ja_user (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45),email VARCHAR(70));

CREATE TABLE ja_session_user(session_id int,user_id int,
foreign key (session_id) references ja_session(id),
foreign key (user_id) references ja_user(id)
);