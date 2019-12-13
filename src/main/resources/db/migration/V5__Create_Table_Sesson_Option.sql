CREATE Table ja_session_option (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_time timestamp DEFAULT CURRENT_TIMESTAMP,
    start_at datetime,
    end_at datetime,
    session_id int,
    foreign key (session_id) references ja_session(id)
);

CREATE Table ja_vote (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_time timestamp DEFAULT CURRENT_TIMESTAMP,
    option_id int,
    user_id int,
    vote_type tinyint,
    foreign key (option_id) references ja_session_option(id),
    foreign key (user_id) references ja_user(id)
);
