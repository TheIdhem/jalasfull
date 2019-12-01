Alter table ja_event_log add column checked boolean default false;
Alter table ja_event_log add column owner_id int;
Alter table ja_event_log add foreign key (owner_id) references ja_user(id);