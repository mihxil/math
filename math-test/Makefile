
URL:=mihxil/math-test:latest
VERSION:=$(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

target/math-test-$(VERSION).jar:
	(cd ../mihxil-configuration/ ; mvn  install ) ; (cd ../mihxil-math/ ; mvn install ) ; mvn

docker: Dockerfile
	docker build -t $(URL) --build-arg VERSION=$(VERSION) .

localrun: target/math-test-$(VERSION).jar
	java -jar $< prefs


runro:
	docker run -v $(HOME):/root:ro -it  $(URL) prefs

run:
	docker run -v $(HOME):/root -it  $(URL)

runprefs:
	docker run -v $(HOME):/root -it  $(URL) prefs

explore:
	docker run  -$(HOME):/root:ro -it --entrypoint /bin/bash $(URL)
