CREATE Table ja_comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_time timestamp DEFAULT CURRENT_TIMESTAMP,
    content VARCHAR(500),
    sender_id int,
    session_id int,
    parent_comment_id int,
    foreign key (parent_comment_id) references ja_comment(id) on delete cascade,
    foreign key (sender_id) references ja_user(id),
    foreign key (session_id) references ja_session(id)
);