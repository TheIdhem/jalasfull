CREATE Table ja_notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_time timestamp DEFAULT CURRENT_TIMESTAMP,
    type tinyint,
    user_id int,
    foreign key (user_id) references ja_user(id) on delete cascade
);