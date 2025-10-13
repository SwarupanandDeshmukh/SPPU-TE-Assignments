DELIMITER $$

CREATE OR REPLACE PROCEDURE Fine_calculate(
	IN in_Roll_no INT,
	IN in_Name_of_book VARCHAR(20))
	
BEGIN
	DECLARE fine_amt INT DEFAULT 0;
	DECLARE issue_date DATE;
	DECLARE days_diff INT;
	DECLARE book_status CHAR(1);
	
	SELECT Date_of_issue,Status INTO issue_date,book_status FROM Borrower WHERE Roll_no = in_Roll_no AND Name_of_book = in_Name_of_book;
	
	if issue_date IS NULL OR book_status !='I' THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Book is not issued..';
	END IF;
	
	SET days_diff = DATEDIFF(CURDATE(),issue_date);
	
	IF days_diff > 15 AND days_diff <=30 THEN
		SET fine_amt = (days_diff - 15) * 5;
	ELSEIF days_diff > 30 THEN
		SET fine_amt = (days_diff - 30) * 50;
	END IF;
	
	UPDATE Borrower SET Status = 'R' WHERE Roll_no = in_Roll_no AND Name_of_book = in_Name_of_book;
	
	IF fine_amt > 0 THEN
		INSERT INTO Fine(Roll_no,Date_of_fine,Amt) VALUES(in_Roll_no,CURDATE(),fine_amt);
	END IF;
	
END$$
	
DELIMITER ;

Use te31411_db;

CALL Fine_calculate(102,'DBMS');


