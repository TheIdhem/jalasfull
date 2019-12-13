CREATE Table ja_event_log (id INT AUTO_INCREMENT PRIMARY KEY,creation_time timestamp DEFAULT CURRENT_TIMESTAMP, event_type tinyint not null,session_id int, foreign key (session_id) references ja_session(id));