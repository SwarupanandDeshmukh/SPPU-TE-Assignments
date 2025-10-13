DELIMITER $$

CREATE OR REPLACE FUNCTION fun_Grade(IN in_roll) RETURNS VARCHAR(20)

BEGIN
	DECLARE stud_total_marks INT;
	DECLARE stud_name VARCHAR(20);
	DECLARE out_class VARCHAR(20);
	
	SELECT name, total_marks INTO stud_name,stud_total_marks FROM Stud_marks WHERE Roll = in_roll;
	
	if stud_total_marks >=990 AND stud_total_marks <=1500 THEN
		SET out_class = "DISTINCTION";
	ELSEIF stud_total_marks >=900 AND stud_total_marks <=989 THEN
		SET out_class = "FIRST_CLASS";
	ELSEIF stud_total_marks>=825 AND stud_total_marks <=899 THEN
		SET out_class = "HIGHER_SECOND_CLASS";
	ELSE
		SET out_class = "PASS";
	END IF;
	
	

END$$


DELIMITER ;

Use te31411_db;

SET @var_roll = 2;

CALL proc_Grade(@var_roll,@var_stud_name,@var_out_class);
INSERT INTO Result(Roll,name,class) VALUES(@var_roll,@var_stud_name,@var_out_class);
