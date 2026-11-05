\set ON_ERROR_STOP on
SET client_encoding = 'UTF8';
BEGIN;

CREATE TABLE public.company_documents (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    document_code varchar(30) NOT NULL UNIQUE,
    title varchar(200) NOT NULL,
    department varchar(60) NOT NULL,
    content text NOT NULL,
    effective_date date NOT NULL,
    tags jsonb NOT NULL DEFAULT '[]'::jsonb
);

INSERT INTO public.company_documents
    (document_code, title, department, content, effective_date, tags)
VALUES
    ('DOC-001', '연차 휴가 신청 안내', '인사팀',
     '연차 휴가는 사용 예정일 3영업일 전까지 신청하고 팀장 승인을 받아야 합니다. 긴급한 사유가 있으면 사후 신청할 수 있습니다.',
     '2026-01-01', '["인사", "휴가"]'),
    ('DOC-002', '출장비 정산 기준', '재무팀',
     '국내 출장 숙박비는 1박당 최대 10만 원까지 실비로 정산합니다. 출장 종료 후 7일 이내에 영수증과 출장 보고서를 제출해야 합니다.',
     '2026-03-01', '["출장", "정산"]'),
    ('DOC-003', '개발 서버 이용 지침', '개발팀',
     '개발 서버에는 샘플 데이터만 저장합니다. 배포 전 코드 리뷰를 받고, 작업 내용을 이슈에 기록해야 합니다.',
     '2026-06-01', '["개발", "서버"]');

CREATE TABLE public.products (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sku varchar(30) NOT NULL UNIQUE,
    product_name varchar(120) NOT NULL,
    category varchar(60) NOT NULL,
    price numeric(12, 2) NOT NULL CHECK (price >= 0),
    stock_quantity integer NOT NULL CHECK (stock_quantity >= 0),
    is_active boolean NOT NULL DEFAULT true,
    description text NOT NULL,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO public.products
    (sku, product_name, category, price, stock_quantity, is_active, description)
VALUES
    ('PRD-001', '무선 키보드', '컴퓨터 주변기기', 59000.00, 25, true,
     '블루투스로 연결하는 무선 키보드입니다. 최대 3대의 기기를 등록할 수 있으며 제품 보증 기간은 1년입니다.'),
    ('PRD-002', '27인치 모니터', '디스플레이', 249000.00, 12, true,
     'QHD 해상도를 지원하는 27인치 모니터입니다. HDMI와 DisplayPort 입력을 지원하며 제품 보증 기간은 2년입니다.'),
    ('PRD-003', 'USB-C 허브', '컴퓨터 주변기기', 39000.00, 0, false,
     'HDMI, USB-A, USB-C 충전 포트를 제공하는 허브입니다. 현재 재고가 없어 판매가 중단된 샘플 상품입니다.');

COMMENT ON TABLE public.company_documents IS '지식기반 연동 테스트용 가상 사내 문서 3건';
COMMENT ON TABLE public.products IS '지식기반 연동 테스트용 가상 상품 3건';
COMMIT;
