#!/bin/bash
java -jar CommentStatProcessor-0.0.1-SNAPSHOT.jar \
  --input=input/comments.csv \
  --output=result/result.txt \
  --loglevel=INFO
