DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS courses_teachers CASCADE;
DROP TABLE IF EXISTS schedules CASCADE;
DROP TABLE IF EXISTS lectures CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE students
(
	id SERIAL NOT NULL PRIMARY KEY,
	first_name character varying(64) NOT NULL,
	last_name character varying(64) NOT NULL,
	personnel_number integer UNIQUE,
	group_id integer
);

CREATE TABLE groups
(
	id SERIAL NOT NULL PRIMARY KEY,
	name character varying(16) NOT NULL
);

ALTER TABLE students ADD FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE ON DELETE SET NULL;

CREATE TABLE courses
(
    id SERIAL NOT NULL PRIMARY KEY,
    name character varying(16) NOT NULL
);

CREATE TABLE teachers
(
    id SERIAL NOT NULL PRIMARY KEY,
    first_name character varying (64) NOT NULL,
    last_name character varying (64) NOT NULL,
    personnel_number integer UNIQUE
);

CREATE TABLE courses_teachers
(
    course_id integer NOT NULL,
    teacher_id integer NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (course_id, teacher_id)
);

CREATE TABLE schedules
(
	id SERIAL NOT NULL PRIMARY KEY,
	date DATE NOT NULL UNIQUE
);

CREATE TABLE lectures
(
	id SERIAL NOT NULL PRIMARY KEY,
	course_id integer,
	teacher_id integer,
	group_id integer,
	room_number integer,
	date DATE,
	time_of_start time without time zone,
	time_of_end time without time zone,
	FOREIGN KEY (course_id) REFERENCES courses (id) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (date) REFERENCES schedules (date) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users
(
	id SERIAL NOT NULL,
	login character varying(64) NOT NULL PRIMARY KEY,
	password character varying(255) NOT NULL,
	role character varying(16),
	person_id integer
);