ALTER TABLE book ADD FULLTEXT INDEX ft_index (title, author) WITH PARSER ngram;

-- Inserting CommonCodeGroup data


INSERT INTO `category_trait` (weight, category_code, trait_code)
VALUES
    (2, '0201_01', '0101_01'), -- 한글 -> E
    (2, '0201_02', '0101_03'), -- 수학 -> S
    (2, '0201_03', '0101_05'), -- 과학 -> T
    (2, '0201_04', '0101_02'), -- 역사 -> I
    (2, '0201_05', '0101_06'), -- 문화 -> F
    (2, '0201_06', '0101_07'), -- 퀴즈 -> P
    (2, '0201_07', '0101_04'), -- 생활 -> N
    (2, '0201_08', '0101_08'), -- 안전 -> J
    (2, '0201_09', '0101_03'), -- 성교육 -> S

    (2, '0202_01', '0101_04'), -- 계절 -> N
    (2, '0202_02', '0101_03'), -- 산 -> S
    (2, '0202_03', '0101_07'), -- 바다 -> P
    (2, '0202_04', '0101_05'), -- 하늘 -> T
    (2, '0202_05', '0101_06'), -- 우주 -> F
    (2, '0202_06', '0101_08'), -- 식물 -> J

    (2, '0203_01', '0101_02'), -- 가족 -> I
    (2, '0203_02', '0101_01'), -- 친구 -> E
    (2, '0203_03', '0101_07'), -- 똥-방귀 -> P
    (2, '0203_04', '0101_08'), -- 잠자기 -> J
    (2, '0203_05', '0101_05'), -- 탈것 -> T
    (2, '0203_06', '0101_06'), -- 음식 -> F
    (2, '0203_07', '0101_03'), -- 놀이 -> S
    (2, '0203_08', '0101_04'), -- 그림 -> N
    (2, '0203_09', '0101_02'), -- 음악 -> I

    (2, '0204_01', '0101_03'), -- 명작동화 -> S
    (2, '0204_02', '0101_02'), -- 전래동화 -> I
    (2, '0204_03', '0101_01'), -- 창작동화 -> E
    (2, '0204_04', '0101_05'), -- 위인전 -> T
    (2, '0204_05', '0101_06'), -- 공주-왕자 -> F
    (2, '0204_06', '0101_07'), -- 모험 -> P
    (2, '0204_07', '0101_08'), -- 유명작가 -> J
    (2, '0204_08', '0101_04'), -- 브랜드전집 -> N

    (2, '0205_01', '0101_02'), -- 동물 -> I
    (2, '0205_02', '0101_03'), -- 곤충 -> S
    (2, '0205_03', '0101_05'), -- 파충류 -> T
    (2, '0205_04', '0101_07'), -- 공룡 -> P
    (2, '0205_05', '0101_04'), -- 새 -> N
    (2, '0205_06', '0101_06'), -- 반려동물 -> F
    (2, '0205_07', '0101_08'), -- 바다동물 -> J
    (2, '0205_08', '0101_01'); -- 물고기 -> E



INSERT INTO common_code_group (code_group, group_name, description, parent_code_group, disabled)
VALUES ('0100', '성향', '성향 분류', NULL, FALSE),
       ('0101', 'MBTI', 'MBTI 성향', '0100', FALSE),
       ('0200', '카테고리', '컨텐츠 카테고리', NULL, FALSE),
       ('0201', '배우기', '학습 카테고리', '0200', FALSE),
       ('0202', '자연', '자연 카테고리', '0200', FALSE),
       ('0203', '일상생활', '일상생활 카테고리', '0200', FALSE),
       ('0204', '동화', '동화 카테고리', '0200', FALSE),
       ('0205', '동물', '동물 카테고리', '0200', FALSE),
       ('0300', '행동', '사용자 행동', NULL, FALSE),
       ('0400', '권한', '사용자 권한', NULL, FALSE),
       ('0500', '성별', '사용자 성별', NULL, FALSE);

-- Inserting CommonCode data for MBTI
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0101_01', '0101', 'E', FALSE),
       ('0101_02', '0101', 'I', FALSE),
       ('0101_03', '0101', 'S', FALSE),
       ('0101_04', '0101', 'N', FALSE),
       ('0101_05', '0101', 'T', FALSE),
       ('0101_06', '0101', 'F', FALSE),
       ('0101_07', '0101', 'P', FALSE),
       ('0101_08', '0101', 'J', FALSE);

-- Inserting CommonCode data for 배우기 category
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0201_01', '0201', '한글', FALSE),
       ('0201_02', '0201', '수학', FALSE),
       ('0201_03', '0201', '과학', FALSE),
       ('0201_04', '0201', '역사', FALSE),
       ('0201_05', '0201', '문화', FALSE),
       ('0201_06', '0201', '퀴즈', FALSE),
       ('0201_07', '0201', '생활', FALSE),
       ('0201_08', '0201', '안전', FALSE),
       ('0201_09', '0201', '성교육', FALSE);

-- Inserting CommonCode data for 자연 category
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0202_01', '0202', '계절', FALSE),
       ('0202_02', '0202', '산', FALSE),
       ('0202_03', '0202', '바다', FALSE),
       ('0202_04', '0202', '하늘', FALSE),
       ('0202_05', '0202', '우주', FALSE),
       ('0202_06', '0202', '식물', FALSE);

-- Inserting CommonCode data for 일상생활 category
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0203_01', '0203', '가족', FALSE),
       ('0203_02', '0203', '친구', FALSE),
       ('0203_03', '0203', '똥-방귀', FALSE),
       ('0203_04', '0203', '잠자기', FALSE),
       ('0203_05', '0203', '탈것', FALSE),
       ('0203_06', '0203', '음식', FALSE),
       ('0203_07', '0203', '놀이', FALSE),
       ('0203_08', '0203', '그림', FALSE),
       ('0203_09', '0203', '음악', FALSE);

-- Inserting CommonCode data for 동화 category
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0204_01', '0204', '명작동화', FALSE),
       ('0204_02', '0204', '전래동화', FALSE),
       ('0204_03', '0204', '창작동화', FALSE),
       ('0204_04', '0204', '위인전', FALSE),
       ('0204_05', '0204', '공주-왕자', FALSE),
       ('0204_06', '0204', '모험', FALSE),
       ('0204_07', '0204', '유명작가', FALSE),
       ('0204_08', '0204', '브랜드전집', FALSE);

-- Inserting CommonCode data for 동물 category
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0205_01', '0205', '동물', FALSE),
       ('0205_02', '0205', '곤충', FALSE),
       ('0205_03', '0205', '파충류', FALSE),
       ('0205_04', '0205', '공룡', FALSE),
       ('0205_05', '0205', '새', FALSE),
       ('0205_06', '0205', '반려동물', FALSE),
       ('0205_07', '0205', '바다동물', FALSE),
       ('0205_08', '0205', '물고기', FALSE);

-- Inserting CommonCode data for 행동
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0300_01', '0300', '조회', FALSE),
       ('0300_02', '0300', '좋아요', FALSE),
       ('0300_03', '0300', '싫어요', FALSE),
       ('0300_04', '0300', '추천좋아요', FALSE);

-- Inserting CommonCode data for 권한
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0400_01', '0400', 'Admin', FALSE),
       ('0400_02', '0400', 'User', FALSE);

-- Inserting CommonCode data for 성별
INSERT INTO common_code (code, code_group, name, disabled)
VALUES ('0500_01', '0500', '남성', FALSE),
       ('0500_02', '0500', '여성', FALSE);

UPDATE common_code_group
SET created_at = NOW()
WHERE created_at IS NULL;

UPDATE common_code
SET created_at = NOW()
WHERE created_at IS NULL;


-- trait sql data (1회만 사용 한정 / 초기 세팅값)
-- I, E 테스트
-- 1번 질문
-- 1번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '정해진 시간에 잠을 자는 편인가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (1, '네! 정해진 시간에 자는 편이에요', '0101_08', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (1, '아니요! 매일 잠드는 시간이 항상 달라요', '0101_07', 20, '2024-10-22 15:00:00');

-- 2번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '어떤 친구들과 얘기해야 좀 더 쉽고 편안하게 얘기할 수 있나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (2, '친한 친구도 좋고 새로운 친구들과도 얘기하는 것도 편하고 쉬워요', '0101_01', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (2, '나랑 친한 친구들끼리만 얘기해야 더 편하고 쉬워요', '0101_02', 20, '2024-10-22 15:00:00');

-- 3번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '친구들에게 간식을 나눠 줄 때 어떻게 하나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (3, '모든 친구들에게 공평하게 나눠 주는게 좋아요', '0101_05', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (3, '나랑 친한 친구한테는 몇개 더 나눠 주는게 좋아요', '0101_06', 20, '2024-10-22 15:00:00');

-- 4번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '재밌는 책을 읽고 있을 때 상상력은 어떤가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (4, '책을 읽는 동안 머릿 속에서 다양한 상상의 나래가 펼쳐져요', '0101_04', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (4, '상상보다는 책의 내용을 집중해서 더 읽는거 같아요', '0101_03', 20, '2024-10-22 15:00:00');

-- 5번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '슬픈 책이나 영화를 볼 때 눈물이 많이 나는 편인가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (5, '슬프게 생각되지는 않아서 눈물이 잘 안나와요', '0101_05', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (5, '나도 같이 슬퍼져서 눈물이 많이 나요', '0101_06', 20, '2024-10-22 15:00:00');

-- 6번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '여러명의 친구와 노는 것이 좋나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (6, '네! 많은 친구들과 노는게 더 재밌고 좋아요', '0101_01', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (6, '아니요! 적은 친구들과 노는게 더 재밌고 좋아요', '0101_02', 20, '2024-10-22 15:00:00');

-- 7번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '숙제를 할 때 보통 어떻게 하는 편인가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (7, '빨리 시작해서 끝내고 그 뒤에 재밌게 노는 게 좋아요', '0101_08', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (7, '재밌게 놀면서 숙제는 하고 싶을 때 하고, 마지막에 하는 게 좋아요', '0101_07', 20, '2024-10-22 15:00:00');

-- 8번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '새로운 친구를 만날 때 어떻게 하는게 더 좋은가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (8, '내가 먼저 다가가서 말을 걸어서 친해지는게 더 좋아요', '0101_01', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (8, '친구가 먼저 다가와서 친해지는게 더 좋아요', '0101_02', 20, '2024-10-22 15:00:00');

-- 9번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '어떤 책의 내용이 더 재밌고 좋은가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (9, '상상으로만 일어날 수 있는 내용이 더 재밌고 좋아요', '0101_04', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (9, '현실에서 일어날 수 있는 내용이 더 재밌고 좋아요', '0101_03', 20, '2024-10-22 15:00:00');

-- 10번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '친구가 울고 있을 때 어떤 생각이 드나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (10, '궁금해서 친구에게 왜 울어?라고 물어볼거 같아요', '0101_05', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (10, '나도 같이 슬퍼져서 친구야 울지마라고 말할거 같아요', '0101_06', 20, '2024-10-22 15:00:00');

-- 11번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '소풍을 간다고 했을 때 준비를 어떻게 하는 게 좋은가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (11, '소풍에 필요한 물건들은 계획해서 꼼꼼히 챙기는 게 좋아요', '0101_08', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (11, '소풍 가기 전에 생각나는 것만 얼른 챙기는 게 더 좋아요', '0101_07', 20, '2024-10-22 15:00:00');

-- 12번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '친한 친구가 나에게 잘못 했을 때 어떻게 반응하나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (12, '일단 어떻게 화해 할 수 있는지 먼저 생각해요', '0101_05', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (12, '화가나는 기분이 들어서 화해하고 싶지 않아요', '0101_06', 20, '2024-10-22 15:00:00');

-- 13번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '쉬는 날 어떻게 보내는 게 더 좋은가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (13, '그날 계획을 세워놓고 즐겁고 재밌게 쉬는 게 좋아요', '0101_08', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (13, '특별한 계획 없이 즐겁고 재밌게 쉬는 게 좋아요', '0101_07', 20, '2024-10-22 15:00:00');

-- 14번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '친구한테 실수를 했던 경험을 떠올리며 후회를 하나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (14, '그 때 그러지말걸 하면서 자주 후회해요', '0101_04', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (14, '후회하기 보다는 경험 삼아 더 친구들한테 잘하고 싶어요', '0101_03', 20, '2024-10-22 15:00:00');

-- 15번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '그림이나 만화를 보면 나도 잘할 수 있겠다고 생각하나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (15, '나는 이 그림이나 만화보다 더 잘그릴 수 있다고 자신감이 생겨요', '0101_04', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (15, '나는 미술 능력이 부족하다고 생각해서 자신감은 크게 없어요', '0101_03', 20, '2024-10-22 15:00:00');

-- 16번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '새로운 놀이를 알게 됐을 때 호기심이 생기나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (16, '새로운 놀이를 배우는건 재밌고 흥미로워요', '0101_04', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (16, '내가 할 줄 아는 놀이가 더 편안해요', '0101_03', 20, '2024-10-22 15:00:00');

-- 17번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '새로운 또래 친구들과 놀이를 하는 게 쉬운가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (17, '새로운 친구들과 놀이를 하는건 어렵지 않아요', '0101_01', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (17, '나는 새로운 친구들과 놀이를 하는건 아직 어색해요', '0101_02', 20, '2024-10-22 15:00:00');

-- 18번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '새로운 친구들과 만나기 전의 기분이 어떤가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (18, '새로운 친구들을 사귀게 될 생각에 기분이 좋아요', '0101_01', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (18, '새로운 친구들과 사이좋게 어떻게 지내야할지 걱정 돼요', '0101_02', 20, '2024-10-22 15:00:00');

-- 19번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '게임에서 친구가 규칙을 어겼을 때 어떻게 할 것 같나요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (19, '왜 규칙을 지키지 않았는지 물어보고 다시 규칙대로 하자고 말할 거예요', '0101_05', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (19, '친구가 기분 나빠할까 봐 그냥 넘어가고 계속 같이 놀아요', '0101_06', 20, '2024-10-22 15:00:00');

-- 20번 질문
INSERT INTO trait_question (trait_code_group, content, disabled, created_at)
VALUES ('0101', '쉬는 날 어떻게 보내는 게 더 좋은가요?', true, '2024-10-22 15:00:00');

INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (20, '특별한 계획 없이 즐겁고 재밌게 쉬는 게 좋아요', '0101_07', 20, '2024-10-22 15:00:00');
INSERT INTO trait_answer (question_id, content, trait_code, point, created_at)
VALUES (20, '그날 계획을 세워놓고 즐겁고 재밌게 쉬는 게 좋아요', '0101_08', 20, '2024-10-22 15:00:00');


-- password: 123asdf!
INSERT INTO member (created_at, id, email, name, password, phone, role)
VALUES (now(), 1, 'asdf@asdf.com', 'asdf', '$2a$10$NTMERbRo6crBeceex6e/4OfV5v38qn8czDFmofHWmIsnRk4bfl/fG', '01000000000', 'member'),
       (now(), 2, 'asdfs@asdf.com', 'asdf', '$2a$10$NTMERbRo6crBeceex6e/4OfV5v38qn8czDFmofHWmIsnRk4bfl/fG', '01000000000', 'member');

INSERT INTO child (birth_date, created_at, deleted_at, id, parent_id, gender, name)
VALUES ('2020-01-01', now(), null, 1, 1, '0500_01', 'child1'),
       ('2021-01-01', now(), null, 2, 1, '0500_01', 'child2'),
       ('2019-01-01', now(), null, 3, 2, '0500_02', 'child3');

INSERT INTO child_trait_responses (answer_id, question_id, child_id, created_at, deleted_at)
VALUES (1, 1, 1, now(), null),
       (1, 2, 1, now(), null),
       (1, 3, 1, now(), null),
       (1, 4, 1, now(), null),
       (1, 5, 1, now(), null),
       (1, 6, 1, now(), null),
       (1, 7, 1, now(), null),
       (1, 8, 1, now(), null),
       (1, 9, 1, now(), null),
       (1, 10, 1, now(), null),
       (1, 11, 1, now(), null),
       (1, 12, 1, now(), null),
       (1, 13, 1, now(), null),
       (1, 14, 1, now(), null),
       (1, 15, 1, now(), null),
       (1, 16, 1, now(), null),
       (1, 17, 1, now(), null),
       (1, 18, 1, now(), null),
       (1, 19, 1, now(), null),
       (1, 20, 1, now(), null);

INSERT INTO child_trait (child_id, created_at, deleted_at, id, trait_group_code, trait_value)
VALUES (1, now(), null, 1, '0101', 'INTP'),
       (1, now(), null, 2, '0101', 'INTP'),
       (1, now(), null, 3, '0101', 'INFP'),
       (1, now(), null, 4, '0101', 'INFP'),
       (1, now(), null, 5, '0101', 'INTP'),
       (1, now(), null, 6, '0101', 'ENTP'),
       (2, now(), null, 7, '0101', 'INTP'),
       (2, now(), null, 8, '0101', 'INTP'),
       (2, now(), null, 9, '0101', 'INFP'),
       (2, now(), null, 10, '0101', 'INFP'),
       (2, now(), null, 11, '0101', 'INTP'),
       (2, now(), null, 12, '0101', 'ENTP'),
       (3, now(), null, 13, '0101', 'INFP'),
       (3, now(), null, 14, '0101', 'INTP'),
       (3, now(), null, 15, '0101', 'ENTP');

INSERT INTO trait_score_record (trait_score, child_id, created_at, deleted_at, id, trait_code)
VALUES (3, 1, now(), null, 1, '0101_01'),
       (3, 1, now(), null, 2, '0101_02'),
       (3, 1, now(), null, 3, '0101_03'),
       (3, 1, now(), null, 4, '0101_04'),
       (3, 1, now(), null, 5, '0101_05'),
       (3, 1, now(), null, 6, '0101_06'),
       (3, 1, now(), null, 7, '0101_07'),
       (3, 1, now(), null, 8, '0101_08'),
       (3, 2, now(), null, 9, '0101_01'),
       (3, 2, now(), null, 10, '0101_02'),
       (3, 2, now(), null, 11, '0101_03'),
       (3, 2, now(), null, 12, '0101_04'),
       (3, 2, now(), null, 13, '0101_05'),
       (3, 2, now(), null, 14, '0101_06'),
       (3, 2, now(), null, 15, '0101_07'),
       (3, 2, now(), null, 16, '0101_08'),
       (3, 3, now(), null, 17, '0101_01'),
       (3, 3, now(), null, 18, '0101_02'),
       (3, 3, now(), null, 19, '0101_03'),
       (3, 3, now(), null, 20, '0101_04'),
       (3, 3, now(), null, 21, '0101_05'),
       (3, 3, now(), null, 22, '0101_06'),
       (3, 3, now(), null, 23, '0101_07'),
       (3, 3, now(), null, 24, '0101_08');

INSERT INTO trait_score_daily_record (created_at, deleted_at, id, trait_score_record_id, trait_code, trait_score)
VALUES (now(), null, 1, 1, '0101_01', 3),
       (now(), null, 2, 2, '0101_02', 3),
       (now(), null, 3, 3, '0101_03', 3),
       (now(), null, 4, 4, '0101_04', 3),
       (now(), null, 5, 5, '0101_05', 3),
       (now(), null, 6, 6, '0101_06', 3),
       (now(), null, 7, 7, '0101_07', 3),
       (now(), null, 8, 8, '0101_08', 3),
       (now(), null, 9, 1, '0101_01', 3),
       (now(), null, 10, 2, '0101_02', 3),
       (now(), null, 11, 3, '0101_03', 3),
       (now(), null, 12, 4, '0101_04', 3),
       (now(), null, 13, 5, '0101_05', 3),
       (now(), null, 14, 6, '0101_06', 3),
       (now(), null, 15, 7, '0101_07', 3),
       (now(), null, 16, 8, '0101_08', 3),
       (now(), null, 17, 1, '0101_01', 3),
       (now(), null, 18, 2, '0101_02', 3),
       (now(), null, 19, 3, '0101_03', 3),
       (now(), null, 20, 4, '0101_04', 3),
       (now(), null, 21, 5, '0101_05', 3),
       (now(), null, 22, 6, '0101_06', 3),
       (now(), null, 23, 7, '0101_07', 3),
       (now(), null, 24, 8, '0101_08', 3),
       (now(), null, 25, 9, '0101_01', 3),
       (now(), null, 26, 10, '0101_02', 3),
       (now(), null, 27, 11, '0101_03', 3),
       (now(), null, 28, 12, '0101_04', 3),
       (now(), null, 29, 13, '0101_05', 3),
       (now(), null, 30, 14, '0101_06', 3),
       (now(), null, 31, 15, '0101_07', 3),
       (now(), null, 32, 16, '0101_08', 3),
       (now(), null, 33, 9, '0101_01', 3),
       (now(), null, 34, 10, '0101_02', 3),
       (now(), null, 35, 11, '0101_03', 3),
       (now(), null, 36, 12, '0101_04', 3),
       (now(), null, 37, 13, '0101_05', 3),
       (now(), null, 38, 14, '0101_06', 3),
       (now(), null, 39, 15, '0101_07', 3),
       (now(), null, 40, 16, '0101_08', 3),
       (now(), null, 41, 9, '0101_01', 3),
       (now(), null, 42, 10, '0101_02', 3),
       (now(), null, 43, 11, '0101_03', 3),
       (now(), null, 44, 12, '0101_04', 3),
       (now(), null, 45, 13, '0101_05', 3),
       (now(), null, 46, 14, '0101_06', 3),
       (now(), null, 47, 15, '0101_07', 3),
       (now(), null, 48, 16, '0101_08', 3),
       (now(), null, 49, 17, '0101_01', 3),
       (now(), null, 50, 18, '0101_02', 3),
       (now(), null, 51, 19, '0101_03', 3),
       (now(), null, 52, 20, '0101_04', 3),
       (now(), null, 53, 21, '0101_05', 3),
       (now(), null, 54, 22, '0101_06', 3),
       (now(), null, 55, 23, '0101_07', 3),
       (now(), null, 56, 24, '0101_08', 3);

INSERT INTO trait_record_history (point, child_id, created_at, deleted_at, action_code, trait_code)
VALUES (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 1, now(), null, '0300_01', '0501_06'),
       (3, 1, now(), null, '0300_01', '0501_03'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_07'),
       (3, 1, now(), null, '0300_01', '0501_01'),
       (3, 1, now(), null, '0300_01', '0501_02'),
       (3, 1, now(), null, '0300_01', '0501_04'),
       (3, 1, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_01'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_04'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_05'),
       (3, 2, now(), null, '0300_01', '0501_06'),
       (3, 2, now(), null, '0300_01', '0501_03'),
       (3, 2, now(), null, '0300_01', '0501_02'),
       (3, 2, now(), null, '0300_01', '0501_07'),
       (3, 2, now(), null, '0300_01', '0501_01');