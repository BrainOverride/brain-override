# 🤝 협업 가이드 (CONTRIBUTING)

> 팀 삼두엽이 **현업 개발팀처럼** 일해 보기 위한 규칙과 설명서입니다.
> 셋 다 처음이라는 전제로, "무엇을 하라"와 함께 **"왜 그렇게 하는지"**를 적었습니다.

`CONTRIBUTING.md`는 오픈소스 프로젝트에서 "이 저장소에 기여하는 방법"을 적는 표준 파일 이름입니다. 저장소 루트에 두면 GitHub이 PR을 만들 때 이 파일 링크를 자동으로 보여줍니다.

---

## 목차

1. [프로젝트 구조 이해하기](#1-프로젝트-구조-이해하기)
2. [Git 핵심 개념 5분 정리](#2-git-핵심-개념-5분-정리)
3. [Day 1 처음 세팅](#3-day-1-처음-세팅)
4. [브랜치 전략](#4-브랜치-전략)
5. [하루 작업 흐름](#5-하루-작업-흐름)
6. [커밋 규칙](#6-커밋-규칙)
7. [PR과 머지 규칙](#7-pr과-머지-규칙)
8. [코드 리뷰 하는 법](#8-코드-리뷰-하는-법)
9. [충돌(Conflict) 해결하기](#9-충돌conflict-해결하기)
10. [코드 스타일](#10-코드-스타일)
11. [자주 만나는 에러와 해결법](#11-자주-만나는-에러와-해결법)
12. [팀 소통 규칙](#12-팀-소통-규칙)

---

## 1. 프로젝트 구조 이해하기

```
brain-override/
└── src/
    ├── Main.java       # 프로그램 시작점
    ├── core/           # 🔒 공통 뼈대: 모든 게임이 기대는 부분
    ├── games/          # 🎮 미니게임 6종: 각자 맡아서 만드는 부분
    └── app/            # 🧩 조립과 진행: 메뉴, 게임 모드
```

### 폴더 = 패키지

Java에서는 **폴더 하나가 패키지 하나**입니다. `src/core/Player.java` 파일의 첫 줄은 반드시 이렇게 시작합니다.

```java
package core;
```

다른 패키지의 클래스를 쓰려면 import합니다.

```java
package games;

import core.MiniGame;
import core.GameResult;
```

### 폴더를 왜 셋으로 나눴나

| 패키지 | 들어가는 것 | 비유 | 바꿔도 되나 |
| --- | --- | --- | --- |
| `core` | 추상 클래스, 공통 데이터 (`MiniGame`, `Difficulty`, `Player`, `GameResult`, `ConsoleUtil`) | 건물의 **기둥** | ❌ Day 1 이후 전원 합의 필요 |
| `games` | 구체 미니게임 6개 | 각 **방의 인테리어** | ✅ 담당자가 자유롭게 |
| `app` | 메뉴(`BrainCenter`), 게임 모드 | 건물의 **복도와 엘리베이터** | ⚠️ 담당자(나혜빈)가 관리 |

기둥(`core`)을 한 명이 마음대로 바꾸면, 그 기둥에 기대 있던 6개 방이 한꺼번에 무너집니다. 예를 들어 `playRound()`의 매개변수를 하나 추가하면 6개 게임 클래스가 전부 컴파일 에러가 납니다. 그래서 `core`는 Day 1 오전에 셋이 함께 정하고, 그 뒤로는 바꾸기 전에 꼭 공유합니다.

### 파일별 담당자

| 파일 | 담당 |
| --- | --- |
| `core/MiniGame.java`, `core/GameResult.java`, `core/ConsoleUtil.java` | 심소민 |
| `core/Player.java`, `core/Difficulty.java`, `core/EasyMode.java` 외 난이도 2개 | 정종일 |
| `games/StroopTest.java`, `games/CardMatch.java` | 심소민 |
| `games/MentalMath.java`, `games/tictactoe/` | 정종일 |
| `games/NumberBaseball.java`, `games/MazeEscape.java`, `games/MazeData.java` | 나혜빈 |
| `games/GameRegistry.java`, `app/`, `Main.java` | 나혜빈 |

**파일 하나에 담당자 한 명**이 원칙입니다. 두 사람이 같은 파일을 동시에 고치면 충돌(9절)이 나기 때문입니다. 남의 파일을 고쳐야 하면 담당자에게 먼저 말합니다.

### 의존 방향: import는 한쪽으로만

```
app  ──→  games  ──→  core
```

- `games`의 클래스는 `core`를 import할 수 있습니다.
- `core`의 클래스는 `games`나 `app`을 **import하지 않습니다.**

**왜?** Java는 서로 import해도 컴파일은 됩니다. 문제는 설계입니다. 만약 `core`의 `Player`가 `games.CardMatch`를 알고 있다면, 게임을 하나 추가하거나 이름을 바꿀 때마다 `core`를 고쳐야 합니다. 그러면 "게임을 추가해도 수정 지점 0곳"이라는 우리 설계의 핵심 주장이 깨집니다. `core`는 **부모 타입만 알고, 구체적인 자식은 모르게** 해야 다형성이 의미가 있습니다.

### `GameRegistry`: 구체 클래스 이름이 등장하는 유일한 곳

```java
// games/GameRegistry.java (가이드: 이런 모양)
public class GameRegistry {
    public static List<MiniGame> all() {
        return List.of(new NumberBaseball(), new CardMatch(), /* ... 6개 */);
    }
}
```

메뉴(`BrainCenter`)는 `GameRegistry.all()`이 주는 `List<MiniGame>`만 받아서 씁니다. 리스트에 든 것이 숫자야구인지 카드 뒤집기인지 몰라도 `game.play()`만 부르면 각자 알맞게 동작합니다. 이것이 발표에서 말할 **다형성의 효과**입니다.

---

## 2. Git 핵심 개념 5분 정리

| 용어 | 한 줄 설명 | 비유 |
| --- | --- | --- |
| **저장소(Repository)** | 프로젝트 파일과 모든 변경 기록이 담긴 곳 | 프로젝트 폴더 + 타임머신 |
| **커밋(Commit)** | 변경 내용을 하나의 기록으로 저장 | 게임의 세이브 포인트 |
| **브랜치(Branch)** | 원본을 건드리지 않고 따로 작업하는 갈래 | 원본 문서의 복사본 |
| **푸시(Push)** | 내 컴퓨터의 커밋을 GitHub에 올림 | 클라우드에 업로드 |
| **풀(Pull)** | GitHub의 최신 변경을 내 컴퓨터로 가져옴 | 클라우드에서 다운로드 |
| **PR(Pull Request)** | "내 브랜치를 합쳐 주세요"라는 요청 + 리뷰 공간 | 결재 요청서 |
| **머지(Merge)** | 두 브랜치의 변경을 하나로 합침 | 결재 승인 후 원본에 반영 |
| **충돌(Conflict)** | 두 사람이 같은 줄을 다르게 고쳐서 Git이 자동으로 못 합치는 상황 | 같은 문장을 둘이 다르게 고친 상태 |

**로컬 vs 원격**: 내 컴퓨터에 있는 저장소가 **로컬**, GitHub에 있는 저장소가 **원격(origin)**입니다. 커밋은 로컬에만 저장되고, 푸시해야 팀원이 볼 수 있습니다.

---

## 3. Day 1 처음 세팅

### 3-1. 전원: 개발 환경

| 항목 | 설정 | 이유 |
| --- | --- | --- |
| JDK | 17 이상 (`java -version`으로 확인) | `Map.of()`, `List.of()` 등을 쓰기 위해. 셋이 같은 버전이어야 "내 컴퓨터에선 되는데" 문제가 없음 |
| IntelliJ 인코딩 | `Settings → Editor → File Encodings` 에서 전부 **UTF-8** | 한글 주석과 출력이 깨지지 않도록. 한 명이라도 다르면 커밋할 때 한글이 깨짐 |
| Git 사용자 정보 | 아래 명령어 1회 | 커밋에 "누가 했는지" 기록 |

```bash
git config --global user.name "심소민"
git config --global user.email "GitHub에 가입한 이메일"
```

GitHub 이메일과 같아야 잔디(기여 그래프)에 반영됩니다.

### 3-2. 저장소 담당자(심소민) 1회

1. GitHub Organization에서 `brain-override` 저장소 생성 (**Public**, README 체크 해제)
   - Public이어야 무료 플랜에서 브랜치 보호(4번)를 쓸 수 있습니다.
2. 뼈대(폴더 구조, `README.md`, `CONTRIBUTING.md`, `.gitignore`, PR 템플릿)를 `main`에 첫 커밋으로 올리기
3. `dev` 브랜치 만들어 푸시
   ```bash
   git switch -c dev
   git push -u origin dev
   ```
4. **Settings → Branches → Add branch protection rule** 에서 `main`과 `dev` 각각 설정
   - ✅ Require a pull request before merging
   - ✅ Require approvals: **1**
   - 이렇게 하면 실수로 `main`이나 `dev`에 직접 푸시하는 것을 GitHub이 막아줍니다.
5. **Settings → General → Pull Requests** 에서 `Allow squash merging`만 체크, `Automatically delete head branches` 체크
6. 팀원 2명을 Organization 멤버로 초대 (Write 권한)

### 3-3. 팀원 전원

```bash
git clone https://github.com/<organization>/brain-override.git
cd brain-override
git switch dev
```

IntelliJ로 폴더를 열고 `src` 우클릭 → `Mark Directory as → Sources Root`.

> `.idea/` 폴더(IntelliJ 설정)는 `.gitignore`로 올리지 않습니다. 사람마다 설정이 달라서, 올리면 매번 충돌이 나기 때문입니다. 그래서 각자 Sources Root 지정을 한 번씩 해야 합니다.

---

## 4. 브랜치 전략

```
main   ●──────────────────────────────●─────────────●     ← 발표용 완성본
        \                            /             /
dev      ●──●──────●────────●───────●─────●───────●       ← 통합 테스트
             \    /  \     /  \    /     /
feat/...      ●──●    ●───●    ●──●     ...               ← 각자 작업
```

| 브랜치 | 역할 | 누가 직접 푸시? |
| --- | --- | --- |
| `main` | 발표 때 시연할 **완성본**. 언제 실행해도 돌아가야 함 | ❌ 아무도 (PR로만) |
| `dev` | 각자 만든 기능을 **모아서 함께 돌려보는** 곳 | ❌ 아무도 (PR로만) |
| `feat/<기능>` | 내 작업 공간. 마음껏 실험 | ✅ 본인만 |
| `fix/<버그>` | 버그 수정 | ✅ 본인만 |
| `docs/<문서>` | 문서 수정 | ✅ 본인만 |

### 브랜치 이름 규칙

`종류/짧은-설명` 형식, 영어 소문자와 하이픈(-)만 씁니다.

```
feat/card-match          카드 뒤집기 구현
feat/core-player         Player 클래스 구현
fix/stroop-color-reset   스트룹 색 초기화 버그 수정
docs/readme-run          README 실행 방법 수정
```

**왜 `main` 직접 수정을 막나?** `main`은 "언제나 돌아가는 버전"이어야 합니다. 발표 10분 전에 누군가 실수로 컴파일 안 되는 코드를 올리면 시연이 불가능해집니다. `dev`를 한 겹 두는 이유도 같습니다. 합쳐 보기 전에는 서로의 코드가 같이 돌아가는지 알 수 없기 때문입니다.

### `dev` → `main` 머지 시점

| 시점 | 조건 |
| --- | --- |
| Day 2 저녁 | 1차 통합 플레이에서 메뉴 → 게임 → 결과 화면까지 끝까지 돌아감 |
| Day 3 발표 전 | 최종 리허설 통과 |

---

## 5. 하루 작업 흐름

**아래 순서를 그대로 따라 하면 됩니다.** 처음 며칠은 이 섹션을 띄워두고 작업하세요.

### ① 작업 시작 전: 최신 `dev` 받기

```bash
git switch dev
git pull origin dev
```

**왜?** 어제 팀원이 합친 코드를 받지 않고 작업하면, 오래된 버전 위에서 작업하게 되어 나중에 충돌이 커집니다.

### ② 내 작업 브랜치 만들기

```bash
git switch -c feat/card-match
```

`-c`는 "새로 만들고(create) 이동"이라는 뜻입니다. 이미 만든 브랜치로 이동할 때는 `-c` 없이 `git switch feat/card-match`.

> `git switch`가 안 되면 Git 버전이 오래된 것입니다. `git checkout -b feat/card-match`로 대신하세요.

### ③ 코드 작성 → 단독 실행 테스트

메뉴(`BrainCenter`)가 완성되기 전에도 내 게임을 테스트할 수 있도록, **각 게임 클래스 맨 아래에 테스트용 `main()`**을 둡니다.

```java
// CardMatch.java 맨 아래 (테스트용)
public static void main(String[] args) {
    new CardMatch().play(new Player("테스트"), new NormalMode());
}
```

IntelliJ에서 이 `main` 옆의 ▶ 버튼을 누르면 이 게임만 실행됩니다. Java는 클래스마다 `main()`을 가질 수 있고, 어떤 클래스를 실행하느냐에 따라 시작점이 정해집니다.

### ④ 커밋 (작게, 자주)

```bash
git status                              # 무엇이 바뀌었는지 확인
git add src/games/CardMatch.java        # 커밋할 파일 선택
git commit -m "feat: 카드 뒤집기 판 출력 구현"
```

**왜 작게 자주?** 커밋은 세이브 포인트입니다. 하루치를 커밋 하나로 만들면, 중간에 망가졌을 때 돌아갈 곳이 없습니다. "기능 하나 돌아가면 커밋"이 기준입니다.

> `git add .` (모든 파일)는 편하지만, 실수로 다른 파일까지 올라갈 수 있습니다. 처음에는 파일을 하나씩 지정하는 습관을 들이세요.

### ⑤ GitHub에 올리기

```bash
git push -u origin feat/card-match     # 처음 한 번
git push                               # 그다음부터
```

`-u`는 "이 로컬 브랜치를 원격의 같은 이름 브랜치와 연결"하라는 뜻입니다. 한 번 연결하면 이후로는 `git push`만 쳐도 됩니다.

### ⑥ PR 만들기 → 리뷰 → 머지 (7절)

### ⑦ 머지 후 정리

```bash
git switch dev
git pull origin dev
git branch -d feat/card-match          # 다 쓴 로컬 브랜치 삭제
```

다음 작업은 다시 ①부터, **새 브랜치**로 시작합니다.

**왜 머지된 브랜치를 다시 쓰면 안 되나?** Squash 머지(7절)는 내 커밋 여러 개를 하나로 뭉쳐서 `dev`에 넣습니다. 그래서 내 브랜치의 커밋 기록과 `dev`의 기록이 달라지고, 같은 브랜치에서 이어서 작업하면 이미 합친 내용이 다시 충돌로 나타납니다.

### 작업 중에 팀원 코드가 `dev`에 합쳐졌다면

```bash
git switch dev
git pull origin dev
git switch feat/card-match
git merge dev                    # dev의 최신 내용을 내 브랜치로 가져옴
```

**PR을 올리기 직전에 한 번은 꼭** 해서, 충돌을 내 브랜치에서 미리 해결합니다.

---

## 6. 커밋 규칙

### 형식

```
<종류>: <무엇을 했는지 한 줄>
```

| 종류 | 언제 | 예시 |
| --- | --- | --- |
| `feat` | 새 기능 | `feat: 숫자야구 Hard 거짓 힌트 구현` |
| `fix` | 버그 수정 | `fix: 미로 탈출 소문자 입력 처리` |
| `refactor` | 동작은 같고 코드 구조만 개선 | `refactor: 카드 출력 메서드 분리` |
| `docs` | 문서 | `docs: README 실행 방법 추가` |
| `test` | 테스트 코드 | `test: 삼목 승리 판정 확인용 main 추가` |
| `chore` | 설정, 기타 | `chore: .gitignore에 out 폴더 추가` |

### 좋은 커밋 메시지 vs 나쁜 메시지

| ❌ 나쁜 예 | ✅ 좋은 예 | 이유 |
| --- | --- | --- |
| `수정` | `fix: 스트룹 출력 후 색 초기화 누락 수정` | 무엇을 고쳤는지 나중에 찾을 수 있음 |
| `작업중` | `feat: 암산 스피드 문제 생성 로직` | 미완성이어도 "어디까지 했는지"를 적음 |
| `final_진짜최종` | `feat: 카드 뒤집기 Hard 섞기 완성` | 최종인지는 Git 기록이 알려줌 |

**왜 형식을 맞추나?** 3일 뒤 `git log`를 봤을 때 기능 추가인지 버그 수정인지 한눈에 보여야 합니다. 현업에서는 이 형식(Conventional Commits)으로 변경 기록을 자동 생성하기도 합니다.

---

## 7. PR과 머지 규칙

### 7-1. PR 만드는 법

1. 브랜치를 푸시하면 GitHub 저장소 화면에 **"Compare & pull request"** 버튼이 뜹니다.
2. **base: `dev`** ← compare: `feat/내브랜치` 인지 확인합니다. (`main`으로 잘못 보내는 실수가 가장 흔합니다.)
3. 템플릿(`.github/pull_request_template.md`)이 자동으로 채워지면 빈칸을 작성합니다.
4. 오른쪽 **Reviewers**에서 리뷰어를 지정합니다.

### 7-2. 머지 규칙 ⭐

| # | 규칙 | 이유 |
| --- | --- | --- |
| 1 | `main`, `dev`에는 **PR로만** 반영한다 | 모든 변경에 리뷰가 한 번은 들어가도록 |
| 2 | **승인 1명 이상**이 있어야 머지한다 | 내 눈에 안 보이는 실수를 다른 눈이 잡도록 |
| 3 | `core/` 파일을 바꾸는 PR은 **나머지 2명 모두 승인** | 모든 게임이 영향을 받기 때문 |
| 4 | **머지 버튼은 PR 작성자가** 누른다 | 내 코드가 합쳐지는 순간까지 내가 책임지도록 |
| 5 | 머지 방식은 **Squash and merge** | 내 커밋 여러 개가 `dev`에는 1개로 깔끔하게 남음 |
| 6 | PR은 **작게**: 변경 300줄 이하, 기능 1개 | 큰 PR은 리뷰어가 제대로 읽지 못함 |
| 7 | PR 올리기 전 **`dev`를 내 브랜치에 머지하고 전체 컴파일 확인** | 충돌과 통합 버그를 내 브랜치에서 먼저 해결 |
| 8 | 리뷰 요청 받으면 **2시간 안에** 응답 | 3일 일정에서 리뷰 대기가 가장 큰 병목 |
| 9 | 머지 후 단톡방에 **"dev에 ○○ 머지했어요"** 공유 | 다른 사람이 `git pull` 할 타이밍을 알도록 |

### 7-3. 리뷰어 지정 순서

| PR 작성자 | 리뷰어 |
| --- | --- |
| 심소민 | 정종일 |
| 정종일 | 나혜빈 |
| 나혜빈 | 심소민 |

돌아가면서 리뷰하면 셋 다 모든 코드를 한 번씩 보게 됩니다. 발표 Q&A에서 "남의 게임" 질문을 받아도 답할 수 있습니다.

---

## 8. 코드 리뷰 하는 법

### 리뷰어 체크리스트

- [ ] **실행해 봤나?** 브랜치를 받아서 직접 돌려본다. 코드만 읽고 승인하지 않는다.
  ```bash
  git fetch origin
  git switch feat/card-match
  ```
  IntelliJ에서 해당 게임 클래스의 테스트용 `main()`을 ▶ 실행
- [ ] **인터페이스를 지켰나?** `playRound()`가 `GameResult`를 반환하는가, `core`를 몰래 바꾸지 않았는가
- [ ] **설명서와 같은가?** `docs/games/`의 규칙과 난이도 설정대로 동작하는가
- [ ] **잘못된 입력에 죽지 않나?** 문자, 빈 입력, 범위 밖 숫자를 넣어본다
- [ ] **캡슐화를 지켰나?** 필드가 `public`으로 열려 있지 않은가
- [ ] **남은 디버그 코드가 없나?** 테스트용 `System.out.println()`, 주석 처리된 코드 덩어리

### 코멘트 말투

리뷰는 **코드에 대한 의견**이지 사람에 대한 평가가 아닙니다.

| ❌ | ✅ |
| --- | --- |
| "이거 틀렸어요" | "여기서 빈 문자열이 들어오면 `NumberFormatException`이 날 것 같아요. 입력 검증을 추가하면 어떨까요?" |
| "왜 이렇게 짰어요?" | "이 부분을 이렇게 하신 이유가 궁금해요! 혹시 `Set`을 쓰면 더 짧아질까요?" |

코멘트 앞에 머리말을 붙이면 중요도가 바로 보입니다.

- `[필수]` 머지 전에 꼭 고쳐야 함 (버그, 인터페이스 위반)
- `[제안]` 고치면 좋지만 작성자가 판단
- `[질문]` 이해가 안 돼서 묻는 것

### 작성자는

- 코멘트마다 **고쳤으면 "반영했습니다 + 커밋 링크"**, 안 고치면 이유를 답글로 남깁니다.
- 고친 커밋을 같은 브랜치에 푸시하면 PR에 자동으로 추가됩니다. PR을 새로 만들 필요가 없습니다.

---

## 9. 충돌(Conflict) 해결하기

충돌은 **실패가 아니라 일상**입니다. 현업 개발자도 매일 겪습니다. 겁먹지 말고 순서대로 하면 됩니다.

### 언제 생기나

두 사람이 **같은 파일의 같은 줄**을 다르게 고치고 합치려 할 때 생깁니다. 파일 담당자를 나눈 것도 충돌을 줄이기 위해서지만, `GameRegistry.java`처럼 여러 사람의 게임이 등록되는 파일에서는 생길 수 있습니다.

### 해결 순서

```bash
git switch feat/card-match
git merge dev
# CONFLICT (content): Merge conflict in src/games/GameRegistry.java
```

1. 충돌난 파일을 열면 이렇게 보입니다.
   ```java
   <<<<<<< HEAD
   return List.of(new NumberBaseball(), new CardMatch());       // 내 브랜치 내용
   =======
   return List.of(new NumberBaseball(), new MentalMath());      // dev에서 온 내용
   >>>>>>> dev
   ```
2. **둘 중 무엇을 남길지 사람이 정합니다.** 이 경우는 둘 다 필요하므로 합칩니다.
   ```java
   return List.of(new NumberBaseball(), new CardMatch(), new MentalMath());
   ```
3. `<<<<<<<`, `=======`, `>>>>>>>` 표시 줄을 **모두 지웁니다.** 하나라도 남으면 컴파일 에러가 납니다.
4. 전체를 실행해서 확인한 뒤 커밋합니다.
   ```bash
   git add src/games/GameRegistry.java
   git commit -m "chore: dev 머지 충돌 해결"
   git push
   ```

> **IntelliJ를 쓰면** 충돌 파일에서 `Resolve` 버튼이 뜨고, 왼쪽(내 것) · 오른쪽(dev) · 가운데(결과) 3단 화면에서 클릭으로 고를 수 있어 훨씬 쉽습니다.

**헷갈리면 멈추고 물어보세요.** 남의 코드가 걸린 충돌을 혼자 해결하다가 그 코드를 지워버리는 것이 가장 위험합니다. `git merge --abort`를 치면 머지 시작 전 상태로 돌아갑니다.

---

## 10. 코드 스타일

### 이름 짓기

| 대상 | 규칙 | 예시 |
| --- | --- | --- |
| 클래스, 인터페이스 | `PascalCase` (명사) | `CardMatch`, `GameResult`, `AIStrategy` |
| 메서드 | `camelCase` (동사로 시작) | `playRound()`, `calcScore()`, `loseLife()` |
| 변수, 필드 | `camelCase` | `failStreak`, `baseScore` |
| 상수 | `UPPER_SNAKE_CASE` + `static final` | `TIME_LIMIT`, `MAX_LIVES` |
| 패키지 | 전부 소문자, 밑줄 없음 | `core`, `games.tictactoe` |
| 파일 | `public` 클래스 이름과 **정확히 같게** | `CardMatch` → `CardMatch.java` |
| boolean 메서드 | `is`, `has`로 시작 | `isAlive()`, `isSuccess()` |

### 접근 제어자 (캡슐화의 핵심)

| 제어자 | 접근 가능 범위 | 이 프로젝트에서 |
| --- | --- | --- |
| `private` | 그 클래스 안에서만 | `Player`의 `lives`, `score`, `history`. 모든 필드는 기본적으로 `private` |
| (없음, default) | 같은 패키지 안에서만 | `tictactoe` 패키지 안에서만 쓰는 `BoardUtil` 메서드 |
| `protected` | 같은 패키지 + 자식 클래스 | `MiniGame.playRound()`: 자식이 구현하고, 같은 패키지의 `Difficulty`가 호출 |
| `public` | 어디서나 | `play()`, `calcScore()`, getter |

**원칙: 일단 `private`으로 시작하고, 필요할 때만 넓힌다.** 처음부터 `public`으로 열어두면, 누가 어디서 값을 바꿨는지 추적할 수 없게 됩니다. 이것이 캡슐화를 하는 이유입니다.

### `final`의 두 가지 쓰임

| 위치 | 의미 | 이 프로젝트에서 |
| --- | --- | --- |
| 메서드 앞 | 자식이 오버라이드할 수 없음 | `MiniGame.play()`: 공통 흐름을 자식이 실수로 바꾸지 못하게 |
| 필드/변수 앞 | 한 번 정하면 바꿀 수 없음 | `GameResult`의 모든 필드, 상수 |

### 그 외

- 들여쓰기 스페이스 4칸 (IntelliJ 기본값), 중괄호는 같은 줄에서 열기 `if (x) {`
- **매직 넘버 금지**: `if (count >= 5)` 대신 `private static final int GOAL = 5;`를 두고 `if (count >= GOAL)`
- **오버라이드할 때는 `@Override`를 꼭 붙입니다.** 메서드 이름을 오타 내면 컴파일러가 잡아줍니다. 안 붙이면 오타 난 메서드가 "새 메서드"로 조용히 만들어지고, 부모의 것이 호출되는 버그가 생깁니다.

---

## 11. 자주 만나는 에러와 해결법

### Java

| 에러 | 원인 | 해결 |
| --- | --- | --- |
| `class X is public, should be declared in a file named X.java` | 파일 이름과 클래스 이름이 다름 | 파일 이름을 클래스 이름과 똑같이 (대소문자까지) |
| `cannot find symbol` | import 누락, 오타, 패키지 선언 누락 | 파일 첫 줄 `package` 확인, 다른 패키지면 `import` 추가 |
| `X is not abstract and does not override abstract method playRound` | 부모의 abstract 메서드를 구현 안 함 | `@Override protected GameResult playRound(Difficulty d)` 구현. 매개변수 타입까지 같아야 함 |
| `InputMismatchException` | `nextInt()`에 문자를 입력함 | `nextInt()` 대신 `ConsoleUtil.readInt()` 사용 (아래 Scanner 함정 참고) |
| `NumberFormatException` | `Integer.parseInt("abc")` | `try-catch`로 잡고 재입력 요청 |
| `unreported exception InterruptedException` | `Thread.sleep()`은 예외 처리를 요구함 | `ConsoleUtil.sleep(ms)` 사용 (안에서 try-catch 처리) |
| `NullPointerException` | 객체를 만들지 않고 메서드 호출 | `new`를 했는지, 리스트에 null이 들어가지 않았는지 확인 |
| `java.io.Console`과 이름 충돌 | 클래스 이름을 `Console`로 지음 | 우리는 `ConsoleUtil`로 이름을 정해서 피했음 |
| 한글이 `???`로 나옴 | 콘솔 인코딩이 UTF-8이 아님 | README 2절의 `chcp 65001`, `-Dstdout.encoding=UTF-8` |

### Scanner 함정 (전원 필독)

```java
int n = sc.nextInt();        // "3" 입력 후 Enter
String s = sc.nextLine();    // ← 입력을 기다리지 않고 빈 문자열이 들어옴!
```

`nextInt()`는 숫자만 가져가고 Enter(줄바꿈)는 남겨둡니다. 그다음 `nextLine()`이 남은 줄바꿈을 읽고 바로 끝납니다. 그래서 우리는 **모든 입력을 `nextLine()`으로 받고 필요하면 숫자로 변환**합니다. 이 처리를 `ConsoleUtil.readInt()` 한 곳에 모아서, 6개 게임이 같은 버그를 반복하지 않게 합니다.

또 `Scanner`는 **프로그램 전체에서 하나만** 만듭니다(`ConsoleUtil` 안에). 여러 개를 만들고 하나를 `close()`하면 `System.in` 자체가 닫혀서 이후 입력이 전부 실패합니다.

### Git

| 상황 | 원인 | 해결 |
| --- | --- | --- |
| `! [rejected] ... (fetch first)` | 원격에 내가 안 받은 커밋이 있음 | `git pull` 후 다시 `git push` |
| `GH006: Protected branch update failed` | `main`이나 `dev`에 직접 푸시함 | 정상입니다! 보호 규칙이 막은 것. 작업 브랜치로 옮겨 PR로 올리기 |
| `dev`에서 실수로 작업해버림 (커밋 전) | 브랜치 확인을 안 함 | `git stash` → `git switch -c feat/xxx` → `git stash pop` |
| 커밋 메시지 오타 (푸시 전) | | `git commit --amend -m "올바른 메시지"` |
| `out/`, `.class` 파일이 `git status`에 뜸 | 컴파일 결과물 | `.gitignore`에 있는지 확인. 결과물은 올리지 않음 |

**작업 전 습관**: `git status`와 `git branch`로 **지금 어느 브랜치에 있는지** 확인하세요. 실수의 대부분이 엉뚱한 브랜치에서 작업해서 생깁니다.

---

## 12. 팀 소통 규칙

### 데일리 스탠드업 (매일 오전, 10분)

현업 개발팀이 매일 아침 하는 짧은 회의입니다. 한 사람당 3가지만 말합니다.

1. **어제 한 것**: "카드 뒤집기 판 출력까지 했어요"
2. **오늘 할 것**: "Hard 섞기랑 PR 올리기"
3. **막힌 것**: "화면 지우기가 IntelliJ 콘솔에서 안 돼요"

**왜?** 3일짜리 프로젝트에서는 한 사람이 반나절 막혀 있는 것을 늦게 알면 일정 전체가 밀립니다. "막힌 것"을 말하는 것이 이 회의의 핵심입니다.

### 질문 규칙: 30분 룰

혼자 30분 고민해도 안 풀리면 **바로 단톡방에 질문**합니다. 질문할 때는 이렇게 쓰면 답이 빨리 옵니다.

```
[질문] 카드 뒤집기 화면 지우기
- 하려던 것: 틀린 카드 1.5초 보여주고 지우기
- 한 것: ConsoleUtil.clearScreen() 호출
- 결과: IntelliJ Run 콘솔에서 안 지워짐 (에러 메시지는 없음)
- 브랜치: feat/card-match
```

### 단톡방 말머리

| 말머리 | 용도 |
| --- | --- |
| `[머지]` | "dev에 카드 뒤집기 머지했어요. pull 해주세요" |
| `[리뷰요청]` | PR 링크와 함께 |
| `[질문]` | 막혔을 때 |
| `[core변경]` | 공통 뼈대를 바꾸자는 제안 (전원 동의 필요) |

---

## 부록: 명령어 치트시트

| 하고 싶은 것 | 명령어 |
| --- | --- |
| 지금 상태 확인 | `git status` |
| 지금 브랜치 확인 | `git branch` |
| 최신 dev 받기 | `git switch dev` → `git pull origin dev` |
| 새 브랜치 만들기 | `git switch -c feat/이름` |
| 파일 커밋 | `git add 파일` → `git commit -m "feat: 설명"` |
| 처음 푸시 | `git push -u origin feat/이름` |
| 이후 푸시 | `git push` |
| dev 최신 내용을 내 브랜치로 | `git merge dev` (내 브랜치에서) |
| 머지 취소 | `git merge --abort` |
| 커밋 기록 보기 | `git log --oneline --graph` |
| 다 쓴 브랜치 삭제 | `git branch -d feat/이름` |
