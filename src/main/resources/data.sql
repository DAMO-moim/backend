INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (1, 'MBTI', 'INFP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (2, 'MBTI', 'ISFP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (3, 'MBTI', 'ENFP');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (4, 'age', '20대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (5, 'age', '30대');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (6, 'mood', '화목');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (7, 'mood', '발랄');

INSERT INTO category (category_id, category_name) VALUES (1, '스포츠');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (1, '축구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (2, '야구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (3, '농구', 1);

INSERT INTO category (category_id, category_name) VALUES (2, '개발');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (4, '인프런', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (5, '프론트엔드', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (6, '백엔드', 2);

INSERT INTO category (category_id, category_name) VALUES (3, '음악');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (7, 'K-POP', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (8, '클래식', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (9, '재즈', 3);

INSERT INTO category (category_id, category_name) VALUES (4, '게임/오락');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (10, 'RPG', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (11, 'AOS', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (12, '기타', 4);

INSERT INTO category (category_id, category_name) VALUES (5, '반려동물');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (13, '강아지', 5);

INSERT INTO category (category_id, category_name) VALUES (6, '문화/공연');
INSERT INTO category (category_id, category_name) VALUES (7, '영화');
INSERT INTO category (category_id, category_name) VALUES (8, '요리/제조');
INSERT INTO category (category_id, category_name) VALUES (9, '외국/언어');
INSERT INTO category (category_id, category_name) VALUES (10, '자기개발');
INSERT INTO category (category_id, category_name) VALUES (11, '독서');
INSERT INTO category (category_id, category_name) VALUES (12, '공예');
INSERT INTO category (category_id, category_name) VALUES (13, '주류');

INSERT INTO chat_room (chat_room_id, category_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);
