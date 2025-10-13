DELIMITER $$

CREATE OR REPLACE PROCEDURE Copy_to_new(IN Student_id INT)
BEGIN
    DECLARE new_sid INT;
    DECLARE new_sname VARCHAR(20);
    DECLARE new_marks INT;
    DECLARE done INT DEFAULT FALSE;
    DECLARE record_found INT DEFAULT 0;

    DECLARE my_cur CURSOR FOR
        SELECT s_id, s_name, s_marks FROM O_RollCall WHERE s_id = Student_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN my_cur;

    read_loop: LOOP
        FETCH my_cur INTO new_sid, new_sname, new_marks;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SET record_found = 1;

        IF EXISTS (SELECT 1 FROM N_RollCall WHERE s_id = new_sid) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Record already exists in N_RollCall';
        ELSE
            INSERT INTO N_RollCall VALUES (new_sid, new_sname, new_marks);
            SELECT CONCAT('Record for Student ID ', new_sid, ' successfully inserted.') AS Message;
        END IF;
    END LOOP;

    CLOSE my_cur;

    IF record_found = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Student ID not found in O_RollCall';
    END IF;
END $$

DELIMITER ;

CALL Copy_to_new(10);

