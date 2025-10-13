DELIMITER $$
CREATE OR REPLACE TRIGGER library_update
BEFORE UPDATE ON Library
FOR EACH ROW
BEGIN
    INSERT INTO LibraryAudit (
        action, trigger_time, book_id, title, author, p_year
    ) VALUES (
        'UPDATE', 'BEFORE', OLD.book_id, OLD.title, OLD.author, OLD.p_year
    );
END $$
DELIMITER ;

