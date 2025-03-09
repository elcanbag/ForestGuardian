
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');



INSERT INTO FOREST (id, name, forest_token) VALUES (1, 'Amazon Forest', 'AMZ123');
INSERT INTO FOREST (id, name, forest_token) VALUES (2, 'Black Forest', 'BLK456');
INSERT INTO FOREST (id, name, forest_token) VALUES (3, 'Siberian Taiga', 'SIB789');
INSERT INTO FOREST (id, name, forest_token) VALUES (4, 'Congo Rainforest', 'CNG101');
INSERT INTO FOREST (id, name, forest_token) VALUES (5, 'Borneo Rainforest', 'BOR202');


INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (1, 'Fire', '2025-03-08T12:00:00', 'AMZ123');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (2, 'Deforestation', '2025-03-07T14:30:00', 'AMZ123');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (3, 'Illegal Logging', '2025-03-06T16:45:00', 'BLK456');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (4, 'Landslide', '2025-03-05T09:15:00', 'SIB789');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (5, 'Heavy Rainfall', '2025-03-04T22:00:00', 'CNG101');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (6, 'Drought', '2025-03-03T18:20:00', 'BOR202');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (7, 'Fire', '2025-03-02T11:10:00', 'AMZ123');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (8, 'Flood', '2025-03-01T08:30:00', 'SIB789');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (9, 'Deforestation', '2025-02-28T17:45:00', 'BLK456');
INSERT INTO ALERT (id, alert_type, timestamp, forest_token) VALUES (10, 'Storm', '2025-02-27T20:15:00', 'CNG101');
