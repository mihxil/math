
URL:=mihxil/math-test:latest

target/test-0.1-SNAPSHOT.jar:
	(cd ../mihxil-configuration/ ; mvn  install ) ; (cd ../mihxil-math/ ; mvn install ) ; mvn

docker: Dockerfile
	docker build -t $(URL) .

localrun: target/test-0.1-SNAPSHOT.jar
	java -jar $< prefs


runro:
	docker run -v $(HOME):/root:ro -it  $(URL) prefs

run:
	docker run -v $(HOME):/root -it  $(URL)

runprefs:
	docker run -v $(HOME):/root -it  $(URL) prefs

explore:
	docker run  -$(HOME):/root:ro -it --entrypoint /bin/bash $(URL)
