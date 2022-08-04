CREATE TABLE IF NOT EXISTS students
(
	id SERIAL NOT NULL PRIMARY KEY,
	first_name character varying(64) NOT NULL,
	last_name character varying(64) NOT NULL,
	personnel_number integer UNIQUE,
	group_id integer
);

CREATE TABLE IF NOT EXISTS groups
(
	id SERIAL NOT NULL PRIMARY KEY,
	name character varying(16) NOT NULL
);

ALTER TABLE students ADD FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS courses
(
    id SERIAL NOT NULL PRIMARY KEY,
    name character varying(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS teachers
(
    id SERIAL NOT NULL PRIMARY KEY,
    first_name character varying (64) NOT NULL,
    last_name character varying (64) NOT NULL,
    personnel_number integer UNIQUE
);

CREATE TABLE IF NOT EXISTS courses_teachers
(
    course_id integer NOT NULL,
    teacher_id integer NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON UPDATE CASCADE ON DELETE SET NULL,
    UNIQUE (course_id, teacher_id)
);

CREATE TABLE IF NOT EXISTS schedules
(
	id SERIAL NOT NULL PRIMARY KEY,
	date DATE NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS lectures
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

INSERT INTO schedules (date)
SELECT generate_series('2020-01-01'::date,'2030-01-01'::date,'1 day'::interval) ON CONFLICT DO NOTHING;
