CREATE KEYSPACE IF NOT EXISTS linux_logs WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1};
DROP TABLE IF EXISTS linux_logs.logs;
CREATE TABLE IF NOT EXISTS linux_logs.logs (id int PRIMARY KEY, datetime timestamp, hostname text, process text, message text, priority int);

INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (1, '2020-11-20T00:00:00', 'localhost', 'root', 'dummy message', 0);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (2, '2020-11-20T00:00:00', 'localhost', 'root', 'dummy message', 1);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (3, '2020-11-20T01:00:00', 'localhost', 'root', 'dummy message', 1);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (4, '2020-11-20T01:00:00', 'localhost', 'root', 'dummy message', 1);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (5, '2020-11-20T02:00:00', 'localhost', 'root', 'dummy message', 2);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (6, '2020-11-20T02:00:00', 'localhost', 'root', 'dummy message', 3);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (7, '2020-11-20T02:00:00', 'localhost', 'root', 'dummy message', 2);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (8, '2020-11-20T03:00:00', 'localhost', 'root', 'dummy message', 3);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (9, '2020-11-20T04:00:00', 'localhost', 'root', 'dummy message', 4);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (10, '2020-11-20T04:00:00', 'localhost', 'root', 'dummy message', 5);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (11, '2020-11-20T04:00:00', 'localhost', 'root', 'dummy message', 5);
INSERT INTO linux_logs.logs (id, datetime, hostname, process, message, priority)
values (12, '2020-11-20T05:00:00', 'localhost', 'root', 'dummy message', 6);