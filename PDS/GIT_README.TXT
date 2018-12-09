git init
git remote add origin https://github.com/ilovekms/egov2.7.git

git config --global user.email "ilovekms@gmail.com"
git config --global user.name "ilovekms"

git pull origin master


git status
git add .
git commit -m "message"
git push origin  master


100M 이상 파일 
https://git-lfs.github.com/
https://rtyley.github.io/bfg-repo-cleaner/

java -jar bfg.jar --strip-blobs-bigger-than 100M .git
cd .git
--git reflog expire --expire=now --all && git gc --prune=now --aggressive
git push origin master

git push origin --force --all


기존명령:  HaHwul #> git push -u origin master
강제명령:  HaHwul #> git push -u origin +master
                                     git pull origin master --allow-unrelated-histories (위험)


git push --set-upstream origin master

git lfs fetch --all

개별파일 원복
git checkout  -- <파일> : 워킹트리의 수정된 파일을 index에 있는 것으로 원복
git checkout HEAD -- <파일명> : 워킹트리의 수정된 파일을 HEAD에 있는 것으로 원복(이 경우 --는 생략가능)
git checkout FETCH_HEAD -- <파일명> : 워킹트리의 수정된 파일의 내용을 FETCH_HEAD에 있는 것으로 원복? merge?(이 경우 --는 생략가능)

index 추가 취소
git reset -- <파일명> : 해당 파일을 index에 추가한 것을 취소(unstage). 워킹트리의 변경내용은 보존됨. (--mixed 가 default)
git reset HEAD <파일명> : 위와 동일

commit 취소
git reset HEAD^ : 최종 커밋을 취소. 워킹트리는 보존됨. (커밋은 했으나 push하지 않은 경우 유용)
git reset HEAD~2 : 마지막 2개의 커밋을 취소. 워킹트리는 보존됨.
git reset --hard HEAD~2 : 마지막 2개의 커밋을 취소. index 및 워킹트리 모두 원복됨.
git reset --hard ORIG_HEAD : 머지한 것을 이미 커밋했을 때,  그 커밋을 취소. (잘못된 머지를 이미 커밋한 경우 유용)
git revert HEAD : HEAD에서 변경한 내역을 취소하는 새로운 커밋 발행(undo commit). (커밋을 이미 push 해버린 경우 유용)

워킹트리 전체 원복
git reset --hard HEAD : 워킹트리 전체를 마지막 커밋 상태로 되돌림. 마지막 커밋이후의 워킹트리와 index의 수정사항 모두 사라짐. 
                                  (변경을 커밋하지 않았다면 유용)
git checkout HEAD . : ??? 워킹트리의 모든 수정된 파일의 내용을 HEAD로 원복.
git checkout -f : 변경된 파일들을 HEAD로 모두 원복(아직 커밋하지 않은 워킹트리와 index 의 수정사항 모두 사라짐. 신규추가 파일 제외)


* 참조 : reset 옵션
--soft : index 보존, 워킹트리 보존. 즉 모두 보존.
--mixed : index 취소, 워킹트리만 보존 (기본 옵션)
--hard : index 취소, 워킹트리 취소. 즉 모두 취소.

* untracked 파일 제거
git clean -f
git clean -f -d : 디렉토리까지 제거


출처: http://ecogeo.tistory.com/276 [아키텍트를 꿈꾸며 - 에코지오]