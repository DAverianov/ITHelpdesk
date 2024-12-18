ALTER TABLE person add sex varchar(20);
ALTER TABLE person add id_card varchar(11);
CREATE INDEX person_id_card ON person USING btree(id_card);