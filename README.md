# 📊 CommentStatProcessor

CSV 파일로부터 댓글 데이터를 읽어들여, 텍스트 내 포함된 학교명을 추출하고 정제된 학교명 기준으로 통계를 제공하는 **Pure Java 기반 애플리케이션**입니다.

---

## 📌 목차

- [🚀 사용법](#-사용법)
- [⚙️ 요구사항](#️-요구사항)
- [🔁 시퀀스 다이어그램](#-시퀀스-다이어그램)
- [💡 기술 의사결정](#-기술-의사결정)
- [🏫 학교명 추출 방안](#-학교명-추출-방안)
- [📂 프로젝트 구조](#-프로젝트-구조)

---

## 🚀 사용법

👉 자세한 실행 및 빌드 방법은 아래 링크에서 확인해주세요:  
🔗 [🔧 사용법 바로가기](https://thoracic-catamaran-22f.notion.site/1bf435d0a24b80e2bba4d54cbf2d349d?pvs=4)

---

## ⚙️ 요구사항

👉 프로젝트 실행 및 개발 환경에 필요한 요구사항은 아래 링크를 참고해주세요:  
🔗 [📄 요구사항 바로가기](https://thoracic-catamaran-22f.notion.site/1bc435d0a24b8054ac26dad2cfefb38c?pvs=4)

---

## 🔁 시퀀스 다이어그램

👉 주요 흐름과 기능 실행 순서를 알고 싶다면 아래 다이어그램을 확인해주세요:  
🔗 [📈 시퀀스 다이어그램 보기](https://thoracic-catamaran-22f.notion.site/1bc435d0a24b80d0ae0df2b78d9c0568?pvs=4)

---


## 💡 기술 의사결정

👉 이 프로젝트에서 선택한 기술 스택, 구조, 처리 방안 등은 아래 링크에 정리되어 있습니다:  
🔗 [💬 기술 의사결정 보기](https://thoracic-catamaran-22f.notion.site/1bc435d0a24b80b892d0dbf55402340a?pvs=4)

---

## 🏫 학교명 추출 방안

👉 정규표현식, 약어 처리, 정식명칭 매핑 등 **학교명 추출 로직의 세부 사항**은 아래 문서에 정리되어 있습니다:  
🔗 [🏫 학교명 추출 방안 보기](https://thoracic-catamaran-22f.notion.site/1bd435d0a24b80d487d6eac4454135c9?pvs=4)

---

## 📂 프로젝트 구조
````
📦 comment-stat-processor
├── common
│   ├── exception
│   ├── file
│   └── constant
├── domain
│   └── schoolstat
│       ├── reader
│       ├── processor
│       ├── extractor
│       ├── store
│       └── extern
├── execute
│   ├── input/
│   ├── result/
│   ├── logs/
│   └── run.sh
└── resources
├── logback.xml
├── school_names.csv
└── official_school_list.csv
---

> 본 프로젝트는 **학교명 추출 정확성 향상**과 **성능 개선**을 목표로 지속 개선되고 있으며, 병렬 처리, 로깅, 테스트 커버리지, API 연동 기반의 동적 데이터 활용 등 다양한 기술이 통합되어 있습니다.