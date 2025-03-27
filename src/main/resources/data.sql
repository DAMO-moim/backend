INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (1, 'MBTI', 'INFP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (2, 'MBTI', 'ISFP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (3, 'MBTI', 'ENFP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (4, 'MBTI', 'ESFP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (5, 'MBTI', 'INFJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (6, 'MBTI', 'ISFJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (7, 'MBTI', 'ENFJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (8, 'MBTI', 'ESFJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (9, 'MBTI', 'INTP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (10, 'MBTI', 'ISTP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (11, 'MBTI', 'ENTP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (12, 'MBTI', 'ESTP');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (13, 'MBTI', 'INTJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (14, 'MBTI', 'ISTJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (15, 'MBTI', 'ENTJ');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (16, 'MBTI', 'ESTJ');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (17, 'age', '10대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (18, 'age', '20대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (19, 'age', '30대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (20, 'age', '40대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (21, 'age', '50대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (22, 'age', '60대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (23, 'age', '70대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (24, 'age', '80대');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (25, 'age_range', '10대 - 20대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (26, 'age_range', '20대 - 30대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (27, 'age_range', '30대 - 40대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (28, 'age_range', '40대 - 50대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (29, 'age_range', '50대 - 60대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (30, 'age_range', '60대 - 70대');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (31, 'age_range', '70대 - 80대');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (32, 'mood', '차분한');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (33, 'mood', '활발한');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (34, 'mood', '고급진');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (35, 'mood', '편안한');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (36, 'mood', '친구같은');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (37, 'mood', '우아한');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (38, 'mood', '시끌벅적');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (39, 'place', '실내');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (40, 'place', '실외');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (41, 'place', '실내 / 실외');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (42, 'region', '서울');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (43, 'region', '경기도');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (44, 'region', '전라도');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (45, 'region', '강원도');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (46, 'region', '충청도');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (47, 'region', '경상도');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (48, 'region', '제주도');

INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (49, 'fee', '유');
INSERT INTO tag (tag_id, tag_type, tag_name) VALUES (50, 'fee', '무');


INSERT INTO category (category_id, category_name) VALUES (1, '스포츠');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (1, '축구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (2, '야구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (3, '농구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (4, '배드민턴', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (5, '볼링', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (6, '당구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (7, '테니스', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (8, '탁구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (9, '런닝', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (10, '배구', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (11, '수영', 1);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (12, '그외', 1);


INSERT INTO category (category_id, category_name) VALUES (2, '언어');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (13, '영어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (14, '일본어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (15, '중국어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (16, '프랑스어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (17, '스페인어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (18, '러시아어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (19, '독일어', 2);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (20, '그외', 2);

INSERT INTO category (category_id, category_name) VALUES (3, '악기');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (21, '피아노', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (22, '기타', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (23, '바이올린', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (24, '플룻', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (25, '첼로', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (26, '드럼', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (27, '트럼펫', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (28, '아코디언', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (29, '가야금', 3);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (30, '그외', 3);

INSERT INTO category (category_id, category_name) VALUES (4, '댄스');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (31, '줌바', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (32, '힙합', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (33, '왈츠', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (34, '탭댄스', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (35, '탱고', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (36, 'K-POP', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (37, '현대무용', 4);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (38, '그외', 4);


INSERT INTO category (category_id, category_name) VALUES (5, '반려동물');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (39, '강아지', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (40, '고양이', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (41, '햄스터', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (42, '조류', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (43, '미어캣', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (44, '프레디독', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (45, '뱀', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (46, '도마뱀', 5);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (47, '그외', 5);


INSERT INTO category (category_id, category_name) VALUES (6, '사교');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (48, '지역', 6);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (49, '결혼', 6);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (50, '맛집', 6);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (51, '주류', 6);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (52, '그외', 6);

INSERT INTO category (category_id, category_name) VALUES (7, '요리/레시피');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (53, '한식', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (54, '양식', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (55, '중식', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (56, '일식', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (57, '베이킹/제과', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (58, '핸드드립', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (59, '와인', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (60, '조주', 7);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (61, '그외', 7);

INSERT INTO category (category_id, category_name) VALUES (8, '게임/오락');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (62, '보드게임', 8);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (63, '온라인게임', 8);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (64, '카드게임', 8);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (65, '그외', 8);

INSERT INTO category (category_id, category_name) VALUES (9, '사진/영상');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (66, '야외', 9);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (67, '실내', 9);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (68, '카메라', 9);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (69, '영상', 9);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (70, '그외', 9);

INSERT INTO category (category_id, category_name) VALUES (10, '독서');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (71, '인문학', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (72, '심리학', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (73, '철학', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (74, '역사', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (75, '시사/경제', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (76, '작문/글쓰기', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (77, '책/독서', 10);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (78, '그외', 10);

INSERT INTO category (category_id, category_name) VALUES (11, '노래');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (79, '발라드', 11);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (80, '랩/힙합', 11);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (81, '클래식', 11);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (82, '재즈', 11);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (83, 'R&B', 11);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (84, '인디밴드', 11);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (85, '그외', 11);

INSERT INTO category (category_id, category_name) VALUES (12, '자동차');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (86, '기아', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (87, '쌍용', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (88, '벤츠', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (89, '현대', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (90, 'BMW', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (91, '마세라티', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (92, '볼보', 12);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (93, '그외', 12);

INSERT INTO category (category_id, category_name) VALUES (13, '여행');
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (94, '국내', 13);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (95, '해외', 13);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (96, '산', 13);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (97, '바다', 13);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (98, '계곡', 13);
INSERT INTO sub_category (sub_category_id, sub_category_name, category_id) VALUES (99, '그외', 13);

INSERT INTO chat_room (chat_room_id, category_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(12, 12),
(13, 13);

