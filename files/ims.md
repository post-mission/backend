# 프로젝트에서의 이슈 -1)
#postmission/issue

## CORS 이슈
* cors 관련 에러가 떴다.
* 분명 spring에서 cors설정을 해주었는데 왜 오류가 났는 지 몰랐다.
```
Access to XMLHttpRequest at 'http://i6a302.p.ssafy.io:8080/login' from origin 'http://localhost:3000' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```
* 관련 에러는 위와 같았다.

* 문제 해결은 아래의 링크의 도움을 받았다.
	* 링크 : ( [java - Cors Error when using CorsFilter and spring security - Stack Overflow](https://stackoverflow.com/questions/66060750/cors-error-when-using-corsfilter-and-spring-security) )
* config에 아래와 같은 설정을 추가해주었다.
```
config.setAllowedOrigins(List.of("http://localhost:3000","http://i6a302.p.ssafy.io"));
```

## 로그인 error 이슈
* 어쩔 때는 에러 코드가 500, 어쩔 때는 에러 코드가 401이 나왔다.
	* 401은 내가 return 시킨 에러코드이다.
	* 500은 나면 안되는 에러코드였다.
	* 알고보니, 아이디가 틀리면 500, 비밀번호가 틀리면 401이 났던 것이다.
* 즉, 아이디가 틀렸을 때 에러 처리가 안 돼있엇다.

## 헤더에 token값이 반환되지 않는 에러
* ( [Axios Response header의 값이 없는 경우에 대해](https://bogmong.tistory.com/5) )
	* 헤더값으로 token key - value 값이 반환되지 않았었다.
* config.addExposedHeader(“Authorization”); 을 추가해주었다.
	* 참조 링크 : ( [Spring 스프링의 CORS설정](https://emgc.tistory.com/131) ) 

## gitlab 을 clone할 때 자꾸 인증에러 나는 문제
* user settings > access tokens 에서 acess token을 발급 뒤 아이디 비밀번호에 둘다 access token을 입력하니까 동작했다.
	* _FEdgsLqV1zp5UkTdxq2

## This version of npm is compatible with lockfileVersion@1, but package-lock.json was generated for lockfileVersion@2. I'll try to do my best with it! 에러
* [MyNotepad :: npm install 에서 에러가 날 때 해결방법](https://mynotepad.tistory.com/552)
* package-lock.json 을 삭제하니까 동작했다.

## react 배포시 invalid~ 에러
* react 배포 단계에서 발생하는 문제
* 로컬에서는 정상적으로 빌드돼서 실행되지만, EC2에서 실행시 `Invalid Host header`  라는 텍스트만 화면에 출력되면서 정상적으로 실행이 안되는 현상이 발생
* [Invalid host header 오류](https://bytrustu.tistory.com/73)
* [REACT Proxying API Requests in Development : 네이버 블로그](https://m.blog.naver.com/qhdqhdekd261/222015506512)

* 실행화면
[image:95B97FD3-08F6-4619-AD75-9D2A200501AA-3778-000000A7E7C5ADF9/9000543D-1374-4398-9658-ADD024EE8AD8.png]

* 구글링을 해 본 결과, react에서 도메인을 통해 접근 시 발생할 수 있는 에러임을 확인
* npm run start 하는 것으로는 방법 못 찾겠어서, npm run build 해서 build 파일 생성하고, 3000번 들어왔을 때 build 파일 출력하는 식으로 해결했다.
	* ** Devops 작성 파일 확인 **
* 뭐 설정하다가 접속하니까 돼 있었다.
* [Nginx React 프로젝트 배포하는 방법](https://sihus.tistory.com/31)
* sudo vi /etc/nginx/sites-enabled/default
```
server {
   listen 3000;
   location / {
      root /home/ubuntu/app-deploy/build;
      index index.html;
      try_files $uri $uri/ /index.html;
   }
}
```

## ** invalid host header 최종 해결 **
* invalid host header 에러는 pacakage.json에 proxy를 명시해주면 생긴다.
* nginx 를 통해 80 to 3000으로 연결하면 없어지지 않을까 생각했다.
* [Nginx로 React를 배포하는 방법](https://codechacha.com/ko/deploy-react-with-nginx/)
* 위와 같이 build 파일과 nginx를 연결해준뒤, 80 to 3000을 해준다.

그러고 나서도 안됐었는데, git pull 과정에서 front 파일이 합쳐지지 않은 것이 원인이었다.
해당 폴더를 삭제하고 다시 pull을 받고 build를 해주니까 정상적으로 실행이 됐다.

# 프로젝트 세팅 관련

## S3

### 버킷 생성
* [AWS S3 생성하기](https://velog.io/@hanif/AWS-S3-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0)
* https://velog.io/@doohyunlm/AWS-S3-%EB%B2%84%ED%82%B7-%EA%B6%8C%ED%95%9C-%EC%84%A4%EC%A0%95
	* s3 권한설정
* ACL 권한설정
	* [github - For an Amazon S3 bucket deplolyent from Guithub how do I fix the error AccessControlListNotSupported: The bucket does not allow ACLs? - Stack Overflow](https://stackoverflow.com/questions/70333681/for-an-amazon-s3-bucket-deplolyent-from-guithub-how-do-i-fix-the-error-accesscon)
* S3 관련
	* [SpringBoot & AWS S3 연동하기](https://jojoldu.tistory.com/300)

## Jenkins

* [AWS Jenkins를 활용한 Docker x SpringBoot CI/CD 구축](https://velog.io/@haeny01/AWS-Jenkins%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-Docker-x-SpringBoot-CICD-%EA%B5%AC%EC%B6%95)

___
## 젠킨스 설치
* [jenkins 설치하기 (ubuntu 20.04)](https://velog.io/@ifthenelse/jenkins-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0-ubuntu-20.04)

### 설치 명령어
```
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | 
  sudo apt-key add -
sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > \
/etc/apt/sources.list.d/jenkins.list'
sudo apt-get update
sudo apt-get install jenkins
```

* 2020년 4월자로 정책이 바뀌어서 설치 방법이 바뀐 것 같다
	* ([Installing Jenkins on Ubuntu tells me “Package ‘jenkins’ has no installation candidate” - Server Fault](https://serverfault.com/questions/1034893/installing-jenkins-on-ubuntu-tells-me-package-jenkins-has-no-installation-can))
### 포트 변경
```
sudo vi /etc/default/jenkins

#아래 부분을 사용할 포트로 변경
HTTP_PORT=8080
```
* 현재의 jenkins port는 8090으로 설정
___
## 젠킨스 - gitlab 연동 
* [📒 Jenkins에 GitLab 저장소 연동](https://velog.io/@hmyanghm/Jenkins%EC%97%90-GitLab-%EC%A0%80%EC%9E%A5%EC%86%8C-%EC%97%B0%EB%8F%99)
* ssh 접속 위한 키 생성
	* ec2에서 키 생성
	* 해당 키를 gitlab에 등록
* [📒 Jenkins에 GitLab 저장소 연동](https://velog.io/@hmyanghm/Jenkins%EC%97%90-GitLab-%EC%A0%80%EC%9E%A5%EC%86%8C-%EC%97%B0%EB%8F%99)
	* gitlab - jenkins를 연동

[image:CAFCD21D-A8CE-4427-86DA-659C96CE4A2A-604-0000007EDAF172C1/89E760BA-9B9A-454D-9BAA-3D50AC4E14AC.png]
	* credential 이 계속 오류 났었는데, https - token 관련으로 설정하니까 됐다.
___
## 자동배포
### mv
* [리눅스 mv 명령어 - 파일 및 디렉토리 이동 또는 이름 바꾸기 - JooTC](https://jootc.com/p/202106033724)
* mv ./S06P12A302/backend/postmission/build/libs/postmission-0.0.1-SNAPSHOT.jar ./S06P12A302/
### 
* gradle, post build task 플러그인 설치
* gradle version upgrade 방법
	* [apt - How do I upgrade Gradle? - Ask Ubuntu](https://askubuntu.com/questions/932083/how-do-i-upgrade-gradle)
```
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt upgrade gradle
```

### 참고 블로그
* [젠킨스(Jenkins) | Gradle, Jar 빌드&배포하기](https://kitty-geno.tistory.com/91)
* [SpringBoot Jenkins, SpringBoot, Gradle 사용 Jar로 빌드, 배포 : 네이버 블로그](https://m.blog.naver.com/varkiry05/221336183565)

# nginx
#postmission/devops

## nginx 삭제
* ( [ubuntu에 Nginx 설치 정리 + Node js](https://qteveryday.tistory.com/317) )

## nginx 설명
* [Nginx를 사용하여 프록시 서버 만들기](https://velog.io/@jeff0720/2018-11-18-2111-%EC%9E%91%EC%84%B1%EB%90%A8-iojomvsf0n)

## 80 to 3000
* sudo vi /etc/nginx/sites-available/default
* ( [A simple nginx reverse proxy for serving multiple Node.js apps from subfolders](https://flaviocopes.com/nginx-reverse-proxy/) )

___
## nginx 80 to 3000 동작 X
* cd /etc/nginx/sites-available
```
server{
        listen 80;
        server_name http://i6a302.p.ssafy.io/;
        location / {
                proxy_pass http://127.0.0.1:3000;
        }
}
```

* hostname -i 
	* 172.26.3.59
* sudo ln -s /etc/nginx/sites-available/node-server /etc/nginx/sites-enabled/
* ( [Nginx를 사용하여 프록시 서버 만들기](https://velog.io/@jeff0720/2018-11-18-2111-%EC%9E%91%EC%84%B1%EB%90%A8-iojomvsf0n) )
* ** 동작을 안한다.. **
___
## nginx 80 to 3000 동작 O
* [Nginx로 React를 배포하는 방법](https://codechacha.com/ko/deploy-react-with-nginx/)

## nohup 사용법
* ps -ef | grep ~~~.jar 파일
	* 그러면 실행중인 프로세스 숫자 나온다.
* kill -9 프로세스 넘버
[image:AAF296DE-D6AC-4710-9FFB-99CCCAF075CC-601-0000012A3EE8FB86/F7BCA6C4-46BC-4FF1-A254-3753E8BDECB1.png]
참조 : ( [Shell Script nohup으로 실행한 프로세스 종료](https://velog.io/@jekim5418/Shell-Script-nohup%EC%9C%BC%EB%A1%9C-%EC%8B%A4%ED%96%89%ED%95%9C-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4-%EC%A2%85%EB%A3%8C) )

### & 포트 죽이기
* netstat -nap | grep 8080
* fuser -k -n tcp 8080
___
* sudo netstat -tnlp|grep 8080
* kill -9 pid
* ( [리눅스 포트 번호로 특정 프로세스 죽이기](https://banbanmumani.github.io/2017/12/19/%EB%A6%AC%EB%88%85%EC%8A%A4%ED%8F%AC%ED%8A%B8%EB%A1%9C%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%EC%A3%BD%EC%9D%B4%EA%B8%B0/))