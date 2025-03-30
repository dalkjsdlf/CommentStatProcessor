#!/bin/bash

echo "📦 빌드 및 실행 환경 구성 중..."

# 1. 빌드 실행 (shadowJar로 fat JAR 생성)
./gradlew shadowJar || {
  echo "❌ 빌드 실패. 오류를 확인해주세요."
  exit 1
}

# 2. execute 디렉토리 생성
mkdir -p execute

# 3. 하위 디렉토리 존재하지 않으면 생성
for dir in input result logs; do
  if [ ! -d "execute/$dir" ]; then
    echo "📁 execute/$dir 디렉토리가 없어 생성합니다"
    mkdir -p "execute/$dir"
  else
    echo "✅ execute/$dir 디렉토리가 이미 존재합니다"
  fi
done

# 4. JAR 파일 복사
cp build/libs/CommentStatProcessor-0.0.1-SNAPSHOT.jar execute/

# 5. 샘플 comments.csv가 존재할 경우 복사
if [ -f "src/test/resources/comments.csv" ]; then
  cp src/test/resources/comments.csv execute/input/comments.csv
  echo "✅ 샘플 input/comments.csv 복사 완료"
fi

# 6. run.sh 작성
cat <<EOF > execute/run.sh
#!/bin/bash
java -jar CommentStatProcessor-0.0.1-SNAPSHOT.jar \\
  --input=input/comments.csv \\
  --output=result/output.txt \\
  --loglevel=INFO
EOF

chmod +x execute/run.sh

echo "🎉 모든 준비가 완료되었습니다!"
echo "👉 실행은: cd execute && ./run.sh"