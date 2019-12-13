CREATE TABLE ja_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45),
    email VARCHAR(70),
    username VARCHAR(100)
);

create TABLE ja_session(id Int AUTO_INCREMENT PRIMARY KEY,
                        creation_time timestamp DEFAULT CURRENT_TIMESTAMP,
                        title VARCHAR(200),
                        room_id INT,
                        start_at datetime,
                        end_at datetime,
                        status tinyint,
                        up_vote int default 0,
                        down_vote int default 0,
                        owner_id int,
                        foreign key (owner_id) references ja_user(id)
);

CREATE TABLE ja_session_user(session_id int,user_id int,
foreign key (session_id) references ja_session(id),
foreign key (user_id) references ja_user(id)
);