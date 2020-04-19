CREATE TABLE user(
	id INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(12) NOT NULL,
	elo INT NOT NULL,
	usertype TINYINT(1) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE identity(
	id INT NOT NULL,
	password VARCHAR(8) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (id)
		REFERENCES user(id)
);

CREATE TABLE history(
	game_id INT NOT NULL AUTO_INCREMENT,
	id_winner INT NOT NULL,
	id_1 INT NOT NULL,
	id_2 INT NOT NULL,
	id_3 INT NOT NULL,
	PRIMARY KEY (game_id),
	FOREIGN KEY (id_winner)
		REFERENCES user(id),
	FOREIGN KEY (id_1)
		REFERENCES user(id),
	FOREIGN KEY (id_2)
		REFERENCES user(id),
	FOREIGN KEY (id_3)
		REFERENCES user(id)
);

CREATE TABLE participants(
	game_idgame INT NOT NULL,
	user_id INT NOT NULL,
	elo_change INT,
	PRIMARY KEY (game_idgame, user_id),
	FOREIGN KEY (game_idgame)
		REFERENCES history(game_id),
	FOREIGN KEY (user_id)
		REFERENCES user(id)	
);

