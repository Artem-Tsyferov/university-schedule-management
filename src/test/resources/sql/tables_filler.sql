INSERT INTO courses (name) VALUES
('Mathematics'),
('Biology'),
('Java'),
('History'),
('Physics'),
('Chemistry'),
('Fitness');

INSERT INTO teachers (first_name, last_name, personnel_number) VALUES
('Joe', 'Black', 111),
('Jonathan', 'Joestar', 222),
('Arnold', 'Shults', 333),
('Tom', 'Jerry', 444),
('Ronald', 'McDonald', 555);

INSERT INTO courses_teachers VALUES
(1, 1),
(1, 3),
(1, 5),
(2, 2),
(2, 4),
(2, 6),
(3, 7),
(3, 2),
(3, 4),
(3, 6),
(4, 1),
(5, 4);

INSERT INTO groups (name) VALUES
('AB-12'),
('BC-34'),
('DE-56');

INSERT INTO students (first_name, last_name, personnel_number, group_id) VALUES
('John', 'Doe', 111, 1),
('John', 'Dorian', 222, 1),
('Kenny', 'McCormick', 333, 1),
('Lucy', 'Lee', 444, 2),
('Samanta', 'Johnson', 555, 2),
('Kevin', 'Tompson', 666, 3),
('Boris', 'Johnson', 777, 3),
('Steve', 'Jobs', 888, 3);

INSERT INTO schedules (date) VALUES
('2022-07-31'),
('2022-08-01'),
('2022-07-20');

INSERT INTO lectures (course_id, teacher_id, group_id, room_number, date, time_of_start, time_of_end) VALUES
(1, 1, 1, 330, '2022-07-31', '08:00', '10:00'),
(2, 2, 1, 432, '2022-07-31', '10:30', '12:30'),
(3, 1, 1, 342, '2022-07-31', '13:00', '15:00'),
(4, 3, 1, 324, '2022-07-31', '15:30', '17:30'),

(5, 1, 1, 345, '2022-08-01', '08:00', '10:00'),
(6, 3, 1, 432, '2022-08-01', '10:30', '12:30'),
(7, 3, 1, 342, '2022-08-01', '13:00', '15:00'),
(1, 4, 1, 324, '2022-08-01', '15:30', '17:30'),

(1, 1, 2, 111, '2022-07-20', '10:30', '12:00');

INSERT INTO users (login, password, role, person_id) VALUES
('John', '$2a$10$pWZR9RC11g7cIigl/jJswu8zIP1/RmHqhLRJZgIYlx/JhtO3ADBCu', 'STUDENT', 1),
('Dorian', 'bcbe3365e6ac95ea2c0343a2395834dd', 'STUDENT', 2),
('Joe', '310dcbbf4cce62f762a2aaa148d556bd', 'TEACHER', 1),
('JOJO', '550a141f12de6341fba65b0ad0433500', 'TEACHER', 2)
