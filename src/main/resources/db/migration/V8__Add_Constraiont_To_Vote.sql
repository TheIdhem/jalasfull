ALTER TABLE ja_vote
ADD CONSTRAINT delete_on_cascade
FOREIGN KEY (option_id) REFERENCES ja_session_option (id)
ON DELETE CASCADE;