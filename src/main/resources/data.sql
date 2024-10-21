-- Inserting CommonCodeGroup data
INSERT INTO common_code_group (code_group, group_name, description, parent_code_group, disabled)
VALUES
    ('0100', '성향', '성향 분류', NULL, FALSE),
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
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0101_01', '0101', 'E'),
    ('0101_02', '0101', 'I'),
    ('0101_03', '0101', 'S'),
    ('0101_04', '0101', 'N'),
    ('0101_05', '0101', 'T'),
    ('0101_06', '0101', 'F'),
    ('0101_07', '0101', 'P'),
    ('0101_08', '0101', 'J');

-- Inserting CommonCode data for 배우기 category
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0201_01', '0201', '한글'),
    ('0201_02', '0201', '수학'),
    ('0201_03', '0201', '과학'),
    ('0201_04', '0201', '역사'),
    ('0201_05', '0201', '문화'),
    ('0201_06', '0201', '퀴즈'),
    ('0201_07', '0201', '생활'),
    ('0201_08', '0201', '안전'),
    ('0201_09', '0201', '성교육');

-- Inserting CommonCode data for 자연 category
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0202_01', '0202', '계절'),
    ('0202_02', '0202', '산'),
    ('0202_03', '0202', '바다'),
    ('0202_04', '0202', '하늘'),
    ('0202_05', '0202', '우주'),
    ('0202_06', '0202', '식물');

-- Inserting CommonCode data for 일상생활 category
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0203_01', '0203', '가족'),
    ('0203_02', '0203', '친구'),
    ('0203_03', '0203', '똥-방귀'),
    ('0203_04', '0203', '잠자기'),
    ('0203_05', '0203', '탈것'),
    ('0203_06', '0203', '음식'),
    ('0203_07', '0203', '놀이'),
    ('0203_08', '0203', '그림'),
    ('0203_09', '0203', '음악');

-- Inserting CommonCode data for 동화 category
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0204_01', '0204', '명작동화'),
    ('0204_02', '0204', '전래동화'),
    ('0204_03', '0204', '창작동화'),
    ('0204_04', '0204', '위인전'),
    ('0204_05', '0204', '공주-왕자'),
    ('0204_06', '0204', '모험'),
    ('0204_07', '0204', '유명작가'),
    ('0204_08', '0204', '브랜드전집');

-- Inserting CommonCode data for 동물 category
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0205_01', '0205', '동물'),
    ('0205_02', '0205', '곤충'),
    ('0205_03', '0205', '파충류'),
    ('0205_04', '0205', '공룡'),
    ('0205_05', '0205', '새'),
    ('0205_06', '0205', '반려동물'),
    ('0205_07', '0205', '바다동물'),
    ('0205_08', '0205', '물고기');

-- Inserting CommonCode data for 행동
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0300_01', '0300', '조회'),
    ('0300_02', '0300', '좋아요'),
    ('0300_03', '0300', '싫어요'),
    ('0300_04', '0300', '추천좋아요');

-- Inserting CommonCode data for 권한
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0400_01', '0400', 'Admin'),
    ('0400_02', '0400', 'User');

-- Inserting CommonCode data for 성별
INSERT INTO common_code (code, code_group, name)
VALUES
    ('0500_01', '0500', '남성'),
    ('0500_02', '0500', '여성'),
    ('0500_03', '0500', 'Others');
